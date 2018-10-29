package com.portfolio_app.mvvm_sample.service.repository.state;

import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.repository.DBUserListHelper;
import com.portfolio_app.mvvm_sample.service.repository.MVVMRepository;

/**
 * The state is for loading json from database and convert it to UserList
 */
public class DataProcessingStateLoader extends DataProcessingState {
    public DataProcessingStateLoader(MVVMRepository repository) {
        super(repository);
    }

    @Override
    public void execute() {
        DataProcessingStateDownloader dataProcessingStateDownloader = new DataProcessingStateDownloader();

        // if there is no file and this is current week then downloadUserList it
        DBUserListHelper dbDataHelper = repository.getDBUserListHelper();
        boolean dataAvailableToLoad = dbDataHelper.isDataAvailableToLoad();
        if (!dataAvailableToLoad) {
            dataProcessingStateDownloader.execute();
            return;
        }

        // if download period has passed
        if (dbDataHelper.needToBeRedownloaded()) {
            // then downloadUserList json
            dataProcessingStateDownloader.execute();
            return;
        } else {
            // file was downloadUserList recently so check if exists
            if (dataAvailableToLoad) {
                // decode from blob to json and finally to DaysContainer
                String json = dbDataHelper.convertUsersBlobToJSONString();
                repository.setValue(new DownloadResult<>(UserList.fromJson(json), DownloadResult.ResultStatus.LOADED));
            } else {
                // if this is current week then we can downloadUserList
                dataProcessingStateDownloader.execute();
            }
        }
    }
}