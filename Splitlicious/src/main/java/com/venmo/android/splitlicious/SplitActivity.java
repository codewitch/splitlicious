package com.venmo.android.splitlicious;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.UUID;


public class SplitActivity extends SingleFragmentActivity
    implements SplitFragment.Callbacks, AddFriendsListFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new SplitFragment();
    }

    public void openAddFriendsList(UUID billId) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment addFriendsListFragment = AddFriendsListFragment.newInstance(billId);

        ft.replace(getFragmentContainerResId(), addFriendsListFragment)
            .addToBackStack(null)
            .commit();
    }

    public void closeAddFriendsList() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }
}
