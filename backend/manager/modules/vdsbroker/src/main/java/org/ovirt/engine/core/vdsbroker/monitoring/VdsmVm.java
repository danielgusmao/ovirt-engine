package org.ovirt.engine.core.vdsbroker.monitoring;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.ovirt.engine.core.common.businessentities.VmBalloonInfo;
import org.ovirt.engine.core.common.businessentities.VmDynamic;
import org.ovirt.engine.core.common.businessentities.VmGuestAgentInterface;
import org.ovirt.engine.core.common.businessentities.VmJob;
import org.ovirt.engine.core.common.businessentities.VmNumaNode;
import org.ovirt.engine.core.common.businessentities.VmStatistics;
import org.ovirt.engine.core.common.businessentities.network.VmNetworkInterface;
import org.ovirt.engine.core.common.businessentities.storage.LUNs;

/**
 * This class represents the internal data of a VM, including {@link VmDynamic}, {@link VmStatistics} and
 * {@link VmGuestAgentInterface}, to manage data received from the VM's host.
 */
public class VdsmVm {

    private VmDynamic vmDynamic;
    private VmStatistics vmStatistics;
    private List<VmGuestAgentInterface> vmGuestAgentInterfaces;
    /** Timestamp on the dynamic data we get from VDSM */
    private Double timestamp;

    // A map represents VM's LUN disks (LUN ID -> LUNs object)
    private Map<String, LUNs> lunsMap;
    private List<VmJob> vmJobs;
    private List<VmNetworkInterface> interfaceStatistics;
    private VmBalloonInfo vmBalloonInfo;
    private List<VmNumaNode> vNumaNodeStatisticsList;

    public VdsmVm(Double timestamp) {
        this.lunsMap = Collections.<String, LUNs>emptyMap();
        this.timestamp = timestamp;
    }

    public VmDynamic getVmDynamic() {
        return vmDynamic;
    }

    public VdsmVm setVmDynamic(VmDynamic vmDynamic) {
        this.vmDynamic = vmDynamic;
        return this;
    }

    public VmStatistics getVmStatistics() {
        return vmStatistics;
    }

    public VdsmVm setVmStatistics(VmStatistics vmStatistics) {
        this.vmStatistics = vmStatistics;
        return this;
    }

    public List<VmGuestAgentInterface> getVmGuestAgentInterfaces() {
        return vmGuestAgentInterfaces;
    }

    public VdsmVm setVmGuestAgentInterfaces(List<VmGuestAgentInterface> vmGuestAgentInterfaces) {
        this.vmGuestAgentInterfaces = vmGuestAgentInterfaces;
        return this;
    }

    public Map<String, LUNs> getLunsMap() {
        return lunsMap;
    }

    public VdsmVm setLunsMap(Map<String, LUNs> lunsMap) {
        this.lunsMap = lunsMap;
        return this;
    }

    public List<VmJob> getVmJobs() {
        return vmJobs;
    }

    public VdsmVm setVmJobs(List<VmJob> vmJobs) {
        this.vmJobs = vmJobs;
        return this;
    }

    public List<VmNetworkInterface> getInterfaceStatistics() {
        return this.interfaceStatistics;
    }

    public VdsmVm setInterfaceStatistics(List<VmNetworkInterface> interfaceStatistics) {
        this.interfaceStatistics = interfaceStatistics;
        return this;
    }

    public VmBalloonInfo getVmBalloonInfo() {
        return vmBalloonInfo;
    }

    public VdsmVm setVmBalloonInfo(VmBalloonInfo vmBalloonInfo) {
        this.vmBalloonInfo = vmBalloonInfo;
        return this;
    }

    public List<VmNumaNode> getvNumaNodeStatisticsList() {
        return vNumaNodeStatisticsList;
    }

    public VdsmVm setvNumaNodeStatisticsList(List<VmNumaNode> vNumaNodeStatisticsList) {
        this.vNumaNodeStatisticsList = vNumaNodeStatisticsList;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                vmDynamic,
                vmGuestAgentInterfaces,
                vmStatistics,
                lunsMap,
                interfaceStatistics,
                vNumaNodeStatisticsList
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VdsmVm)) {
            return false;
        }
        VdsmVm other = (VdsmVm) obj;
        return Objects.equals(vmDynamic, other.vmDynamic)
                && Objects.equals(vmGuestAgentInterfaces, other.vmGuestAgentInterfaces)
                && Objects.equals(vmStatistics, other.vmStatistics)
                && Objects.equals(vNumaNodeStatisticsList, other.vNumaNodeStatisticsList)
                && Objects.equals(lunsMap, other.lunsMap)
                && Objects.equals(interfaceStatistics, other.interfaceStatistics);
    }

    public Double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Double timestamp) {
        this.timestamp = timestamp;
    }
}
