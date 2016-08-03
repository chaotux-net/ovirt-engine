package org.ovirt.engine.core.bll.gluster;

import java.util.List;
import java.util.stream.Collectors;

import org.ovirt.engine.core.bll.QueriesCommandBase;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterBrickEntity;
import org.ovirt.engine.core.common.businessentities.gluster.StorageDevice;
import org.ovirt.engine.core.common.config.Config;
import org.ovirt.engine.core.common.config.ConfigValues;
import org.ovirt.engine.core.common.queries.IdQueryParameters;

public class GetUnusedGlusterBricksQuery<P extends IdQueryParameters> extends QueriesCommandBase<P> {

    public GetUnusedGlusterBricksQuery(P parameters) {
        super(parameters);
    }

    @Override
    protected void executeQueryCommand() {
        List<StorageDevice> storageDevicesInHost =
                getDbFacade().getStorageDeviceDao().getStorageDevicesInHost(getParameters().getId());
        getQueryReturnValue().setReturnValue(getUnUsedBricks(storageDevicesInHost));
    }

    private List<StorageDevice> getUnUsedBricks(List<StorageDevice> storageDevicesInHost) {
        List<GlusterBrickEntity> usedBricks =
                getDbFacade().getGlusterBrickDao().getGlusterVolumeBricksByServerId(getParameters().getId());
        return storageDevicesInHost.stream().filter
                (storageDevice -> storageDevice.getMountPoint() != null
                    && !storageDevice.getMountPoint().isEmpty()
                    && (storageDevice.getMountPoint()
                            .startsWith(Config.getValue(ConfigValues.GlusterDefaultBrickMountPoint))
                    || storageDevice.isGlusterBrick())
                    && !isBrickUsed(usedBricks, storageDevice.getMountPoint()))
                .collect(Collectors.toList());
    }

    private boolean isBrickUsed(List<GlusterBrickEntity> usedBricks, String mountPoint) {
        // Checks if the given mount point is already part of any Gluster brick directory.
        // Brick directory may be any directory inside the mount points, so we are using brickDir.startsWith()
        return usedBricks.stream().anyMatch(brick -> brick.getBrickDirectory().startsWith(mountPoint));
    }

}
