package org.ovirt.engine.core.bll.storage.disk.image;

import java.util.Collections;
import java.util.List;

import org.ovirt.engine.core.bll.CommandBase;
import org.ovirt.engine.core.bll.InternalCommandAttribute;
import org.ovirt.engine.core.bll.NonTransactiveCommandAttribute;
import org.ovirt.engine.core.bll.SerialChildCommandsExecutionCallback;
import org.ovirt.engine.core.bll.SerialChildExecutingCommand;
import org.ovirt.engine.core.bll.context.CommandContext;
import org.ovirt.engine.core.bll.tasks.interfaces.CommandCallback;
import org.ovirt.engine.core.bll.utils.PermissionSubject;
import org.ovirt.engine.core.common.action.CopyDataCommandParameters;
import org.ovirt.engine.core.common.action.CopyImageGroupVolumesDataCommandParameters;
import org.ovirt.engine.core.common.action.VdcActionParametersBase.EndProcedure;
import org.ovirt.engine.core.common.action.VdcActionType;
import org.ovirt.engine.core.common.businessentities.LocationInfo;
import org.ovirt.engine.core.common.businessentities.VdsmImageLocationInfo;
import org.ovirt.engine.core.common.businessentities.storage.DiskImage;
import org.ovirt.engine.core.compat.Guid;

@InternalCommandAttribute
@NonTransactiveCommandAttribute
public class CopyImageGroupVolumesDataCommand<T extends CopyImageGroupVolumesDataCommandParameters>
        extends CommandBase<T> implements SerialChildExecutingCommand {
    public CopyImageGroupVolumesDataCommand(T parameters, CommandContext cmdContext) {
        super(parameters, cmdContext);
        setStoragePoolId(getParameters().getStoragePoolId());
    }

    @Override
    protected void executeCommand() {
        List<DiskImage> images = getDiskImageDao()
                .getAllSnapshotsForImageGroup(getParameters().getImageGroupID());
        ImagesHandler.sortImageList(images);
        getParameters().setImageIds(ImagesHandler.getDiskImageIds(images));
        persistCommand(getParameters().getParentCommand(), getCallback() != null);
        setSucceeded(true);
    }

    @Override
    public List<PermissionSubject> getPermissionCheckSubjects() {
        return Collections.emptyList();
    }

    @Override
    public CommandCallback getCallback() {
        return new SerialChildCommandsExecutionCallback();
    }

    @Override
    public boolean performNextOperation(int completedChildren) {
        if (completedChildren == getParameters().getImageIds().size()) {
            return false;
        }

        Guid imageId = getParameters().getImageIds().get(completedChildren);
        log.info("Starting child command {} of {}, image '{}'",
                completedChildren + 1, getParameters().getImageIds().size(), imageId);

        copyVolumeData(imageId);
        return true;
    }

    private void copyVolumeData(Guid image) {
        CopyDataCommandParameters parameters = new CopyDataCommandParameters(getParameters().getStoragePoolId(),
                buildImageLocationInfo(getParameters().getSrcDomain(), getParameters().getImageGroupID(),
                        image),
                buildImageLocationInfo(getParameters().getDestDomain(), getParameters().getImageGroupID(),
                        image), false);

        parameters.setEndProcedure(EndProcedure.COMMAND_MANAGED);
        parameters.setParentCommand(getActionType());
        parameters.setParentParameters(getParameters());
        runInternalAction(VdcActionType.CopyData, parameters);
    }

    @Override
    public void handleFailure() {
    }

    private LocationInfo buildImageLocationInfo(Guid domId, Guid imageGroupId, Guid imageId) {
        return new VdsmImageLocationInfo(domId, imageGroupId, imageId);
    }
}

