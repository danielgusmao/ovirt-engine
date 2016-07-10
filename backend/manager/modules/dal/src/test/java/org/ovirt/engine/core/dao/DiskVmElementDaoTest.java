package org.ovirt.engine.core.dao;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.ovirt.engine.core.common.businessentities.VmDeviceId;
import org.ovirt.engine.core.common.businessentities.storage.DiskVmElement;
import org.ovirt.engine.core.compat.Guid;

public class DiskVmElementDaoTest extends BaseReadDaoTestCase<VmDeviceId, DiskVmElement, DiskVmElementDao>{

    private static final int NUM_OF_DISKS_ATTACHED_TO_VM = 4;
    private static final int NUM_OF_DISKS_PLUGGED_TO_VM = 3;

    private static final Guid PLUGGED_DISK_ID = FixturesTool.LUN_DISK_ID;
    private static final Guid UNPLUGGED_DISK_ID = FixturesTool.IMAGE_GROUP_ID_2;

    @Test
    public void testGetAllForVm() {
        List<DiskVmElement> dves = dao.getAllForVm(FixturesTool.VM_RHEL5_POOL_57);
        assertThat(dves.size(), is(NUM_OF_DISKS_ATTACHED_TO_VM));
    }

    @Test
    public void testGetAllPluggedToVm() {
        List<DiskVmElement> dves = dao.getAllPluggedToVm(FixturesTool.VM_RHEL5_POOL_57);
        assertThat(dves.size(), is(NUM_OF_DISKS_PLUGGED_TO_VM));
    }

    @Test
    public void testVmElementDiskPluggedStatus() {
        DiskVmElement dvePlugged = dao.get(new VmDeviceId(PLUGGED_DISK_ID, FixturesTool.VM_RHEL5_POOL_57));
        assertTrue(dvePlugged.isPlugged());
    }

    @Test
    public void testVmElementDiskUnpluggedStatus() {
        DiskVmElement dveUnplugged = dao.get(new VmDeviceId(UNPLUGGED_DISK_ID, FixturesTool.VM_RHEL5_POOL_57));
        assertFalse(dveUnplugged.isPlugged());
    }

    @Test
    public void testNUllVmElementForFloatingDisk() {
        List<DiskVmElement> allDves = dao.getAll();
        assertTrue(allDves.stream().noneMatch(dve -> dve.getDiskId().equals(FixturesTool.FLOATING_DISK_ID)));
    }

    @Override
    protected VmDeviceId getExistingEntityId() {
        return new VmDeviceId(PLUGGED_DISK_ID, FixturesTool.VM_RHEL5_POOL_57);
    }

    @Override
    protected DiskVmElementDao prepareDao() {
        return dbFacade.getDiskVmElementDao();
    }

    @Override
    protected VmDeviceId generateNonExistingId() {
        return new VmDeviceId(Guid.newGuid(), Guid.newGuid());
    }

    @Override
    protected int getEntitiesTotalCount() {
        return 5;
    }
}
