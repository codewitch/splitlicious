package com.venmo.android.splitlicious;

/**
 * Created by thomasjeon on 7/16/14.
 */
public enum VenmoConstants {
    APP_ID("1805"), APP_NAME("Splitlicious"), ;

    private String mValue;

    private VenmoConstants(String value) {
        mValue = value;
    }

    public String getValue(){
        return mValue;
    }
}
