package com.venmo.android.splitlicious.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by thomasjeon on 7/2/14.
 */
public class BillLab {

    private ArrayList<Bill> mBills;

    private static BillLab sBillLab;
    private Context mAppContext;

    private BillLab(Context appContext) {
        mAppContext = appContext;
        mBills = new ArrayList<Bill>();
    }

    public static BillLab get(Context c) {
        if (sBillLab == null) {
            sBillLab = new BillLab(c.getApplicationContext());
        }
        return sBillLab;
    }

    public Bill addNewBill() {
        Bill newBill = new Bill();
        mBills.add(newBill);
        return newBill;
    }

    //TODO: test this
    public boolean removeBill(UUID billToRemoveId) {
        for (Bill b : mBills) {
            if (b.getId().equals(billToRemoveId))
                return mBills.remove(b);
        }
        return false;
    }

    public ArrayList<Bill> getBills() {
        return mBills;
    }

    public Bill getBill(UUID id) {
        for (Bill b : mBills) {
            if (b.getId().equals(id))
                return b;
        }
        return null;
    }

}
