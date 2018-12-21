package com.portfolio_app.mvvm_sample.service.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.portfolio_app.mvvm_sample.service.model.database.Users;
import com.portfolio_app.mvvm_sample.service.model.database.UsersDAO;
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
 * Simple helper class to save/load UserList to/from SQLlite database
 */
@Database(entities = {Users.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsersDAO userModel();
}
