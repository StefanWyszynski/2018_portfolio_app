package com.portfolio_app.mvvm_sample.service.repository.state;

import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.mvvm_sample.di.MVVMFragmentComponent;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.repository.DBUserListHelper;
import com.portfolio_app.mvvm_sample.service.repository.MVVMRepository;
/*
 * Copyright 2018, The Portfolio project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Stefan Wyszynski
 *
 */

/**
 * The state is for loading json from database and convert it to UserList
 */
public class DataProcessingStateLoader extends DataProcessingState {
    public DataProcessingStateLoader(MVVMRepository repository) {
        super(repository);
    }

    @Override
    public void execute(MVVMFragmentComponent mvvmFragmentComponent) {
        DataProcessingStateDownloader dataProcessingStateDownloader = new DataProcessingStateDownloader(repository);

        // if there is no file and this is current week then downloadUserList it
        DBUserListHelper dbDataHelper = repository.getDbUserListHelper();
        boolean dataAvailableToLoad = dbDataHelper.isDataAvailableToLoad();
        if (!dataAvailableToLoad) {
            dataProcessingStateDownloader.execute(mvvmFragmentComponent);
            return;
        }

        // if download period has passed
        if (dbDataHelper.needToBeRedownloaded()) {
            // then downloadUserList json
            dataProcessingStateDownloader.execute(mvvmFragmentComponent);
            return;
        } else {
            // file was downloadUserList recently so check if exists
            if (dataAvailableToLoad) {
                String json = dbDataHelper.convertUsersBlobToJSONString();
                repository.setValue(DownloadResult.loaded(UserList.fromJson(json)));
            } else {
                // if this is current week then we can downloadUserList
                dataProcessingStateDownloader.execute(mvvmFragmentComponent);
            }
        }
    }
}