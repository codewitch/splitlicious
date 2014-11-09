package com.venmo.android.splitlicious.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by thomasjeon on 6/30/14.
 */
public class FriendLab {

    private ArrayList<Person> mFriends;

    private static FriendLab sFriendLab;
    private Context mAppContext;

    private FriendLab(Context appContext) {
        mAppContext = appContext;
        mFriends = new ArrayList<Person>();
        fillTempFriends();
    }

    public static FriendLab get(Context c) {
        if (sFriendLab == null) {
            sFriendLab = new FriendLab(c.getApplicationContext());
        }
        return sFriendLab;
    }

    public ArrayList<Person> getFriends() {
        return mFriends;
    }

    public Person getFriend(UUID id) {
        for (Person f : mFriends) {
            if (f.getId().equals(id))
                return f;
        }
        return null;
    }

    private void fillTempFriends(){
        Random r = new Random();
        int size = RandomPeopleData.FIRST_NAMES.length;
        int pfsize = RandomPeopleData.PROFILE_PIC_URLS.length;

        for (int i = 0; i < 20; i++) {
            Person f = new Person();
            f.setFirstName(RandomPeopleData.FIRST_NAMES[r.nextInt(size)]);
            f.setLastName(RandomPeopleData.LAST_NAMES[r.nextInt(size)]);
            f.setProfilePictureUrl(RandomPeopleData.PROFILE_PIC_URLS[r.nextInt(pfsize)]);
            mFriends.add(f);
        }
    }
}
