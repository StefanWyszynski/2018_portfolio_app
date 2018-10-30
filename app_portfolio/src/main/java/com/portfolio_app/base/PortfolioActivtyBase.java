package com.portfolio_app.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.portfolio_app.R;


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
public abstract class PortfolioActivtyBase extends AppCompatActivity {

    public void putFragment(Fragment fragment, boolean useBackStack) {
        putFragment(fragment, useBackStack, false, 0, 0);
    }

    public void putFragment(Fragment fragment) {
        putFragment(fragment, false, true, R.anim.fade_in, R.anim.fade_out);
    }

    public void putFragment(Fragment fragment, boolean useBackStack, boolean useAnimation, int animationIn,
                            int animationOut) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment findedFragment = supportFragmentManager.findFragmentById(R.id.fragment_container);
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        if (useAnimation) {
            fragmentTransaction.setCustomAnimations(animationIn, animationOut, animationIn, animationOut);
        }
        if (findedFragment == null) {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }

        if (useBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
