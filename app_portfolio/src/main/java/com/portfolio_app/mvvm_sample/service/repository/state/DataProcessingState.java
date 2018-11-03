package com.portfolio_app.mvvm_sample.service.repository.state;

import com.portfolio_app.mvvm_sample.di.MVVMFragmentComponent;
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
 */
public abstract class DataProcessingState {
    protected MVVMRepository repository;

    public DataProcessingState(MVVMRepository repository) {
        this.repository = repository;
    }

    public abstract void execute(MVVMFragmentComponent mvvmFragmentComponent);
}