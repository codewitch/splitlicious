package com.venmo.android.splitlicious;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by thomasjeon on 6/28/14.
 */
public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    protected int getFragmentContainerResId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(getFragmentContainerResId());

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                .add(getFragmentContainerResId(), fragment)
                .commit();
        }
    }
}
