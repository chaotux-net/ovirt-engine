package org.ovirt.engine.ui.common.widget.uicommon.vm;

import org.ovirt.engine.core.common.businessentities.VM;
import org.ovirt.engine.ui.common.CommonApplicationConstants;
import org.ovirt.engine.ui.common.CommonApplicationMessages;
import org.ovirt.engine.ui.common.gin.AssetProvider;
import org.ovirt.engine.ui.common.widget.form.FormBuilder;
import org.ovirt.engine.ui.common.widget.form.FormItem;
import org.ovirt.engine.ui.common.widget.form.GeneralFormPanel;
import org.ovirt.engine.ui.common.widget.label.StringValueLabel;

public class VmSnapshotInfoGeneral extends GeneralFormPanel {

    private final static CommonApplicationConstants constants = AssetProvider.getConstants();
    private final static CommonApplicationMessages messages = AssetProvider.getMessages();

    private final FormBuilder formBuilder;

    private StringValueLabel definedMemory = new StringValueLabel();
    private StringValueLabel minAllocatedMemory = new StringValueLabel();
    private StringValueLabel cpuInfo = new StringValueLabel();

    public VmSnapshotInfoGeneral() {

        formBuilder = new FormBuilder(this, 1, 3);

        formBuilder.addFormItem(new FormItem(constants.definedMemoryVm(), definedMemory, 0, 0), 3, 9);
        formBuilder.addFormItem(new FormItem(constants.physMemGauranteedVm(), minAllocatedMemory, 1, 0), 3, 9);
        formBuilder.addFormItem(new FormItem(constants.numOfCpuCoresVm(), cpuInfo, 2, 0), 3, 9);

        formBuilder.setRelativeColumnWidth(0, 12);
        setWidth("100%"); //$NON-NLS-1$
    }

    public void update(VM vm) {
        setVisible(vm != null);

        if (vm == null) {
            return;
        }

        definedMemory.setValue(vm.getVmMemSizeMb() + constants.mb());
        minAllocatedMemory.setValue(vm.getMinAllocatedMem() + constants.mb());
        cpuInfo.setValue(messages.cpuInfoLabel(vm.getNumOfCpus(), vm.getNumOfSockets(), vm.getCpuPerSocket()));
    }

}
