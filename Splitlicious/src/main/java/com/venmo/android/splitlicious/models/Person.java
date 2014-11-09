package com.venmo.android.splitlicious.models;

import android.content.Context;

import java.util.UUID;

/**
 * Created by thomasjeon on 6/30/14.
 */
public class Person {

    private UUID mId;
    private String mFirstName;
    private String mLastName;
    private String mProfilePictureUrl;

    private Context mAppContext;
    private static Person sSelfPerson;

    public Person() {
        //Generate unique identifier
        mId = UUID.randomUUID();
    }

    public Person(String firstName, String lastName, String profilePictureUrl){
        mId = UUID.randomUUID();
        mFirstName = firstName;
        mLastName = lastName;
        mProfilePictureUrl = profilePictureUrl;
    }


    /*GET SET yourself*/
    private Person(Context appContext) {
        mAppContext = appContext;
        mId = UUID.randomUUID();
    }

    public static Person getSelf(Context c) {
        if (sSelfPerson == null) {
            sSelfPerson = new Person(c.getApplicationContext());
        }
        return sSelfPerson;
    }

    public static Person setSelf(Context c, String firstName, String lastName, String profilePictureUrl) {
        getSelf(c);
        sSelfPerson.setFirstName(firstName);
        sSelfPerson.setLastName(lastName);
        sSelfPerson.setProfilePictureUrl(profilePictureUrl);
        return sSelfPerson;
    }

    @Override
    public String toString() {
        return mFirstName;
    }

    public UUID getId() {
        return mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getProfilePictureUrl() {
        return mProfilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        mProfilePictureUrl = profilePictureUrl;
    }

}
