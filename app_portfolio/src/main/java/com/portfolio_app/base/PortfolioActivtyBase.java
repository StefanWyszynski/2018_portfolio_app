package com.portfolio_app.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.portfolio_app.R;

/**
 * @author Stefan Wyszynski
 */
public abstract class PortfolioActivtyBase extends AppCompatActivity {

    public void putFragment(Fragment fragment, boolean useBackStack) {
        putFragment(fragment, useBackStack, false, 0, 0);
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
