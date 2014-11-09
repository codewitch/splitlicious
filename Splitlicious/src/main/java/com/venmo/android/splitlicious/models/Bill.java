package com.venmo.android.splitlicious.models;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by thomasjeon on 7/2/14.
 *
 * a model of one bill
 */
public class Bill {

    private UUID mId;
    private String mBillTitle;
    private float mBillAmount;
    private float mTipAmount;

    //map of friends involved and their portion of the bill
    private LinkedHashMap<UUID, Float> mSplitGroup;
    private LinkedHashMap<UUID, Float> mTempSplitGroup;

    public Bill() {
        mId = UUID.randomUUID();
        mSplitGroup = new LinkedHashMap<UUID, Float>();
        mTempSplitGroup = new LinkedHashMap<UUID, Float>();

        mBillTitle = "Splitlicious";
        mBillAmount = 0;
        mTipAmount = 0;
    }

    /*Bill Splitting functions*/
    public float getBillTotal() { return roundToMoney(mBillAmount + mTipAmount); }

    public void setPersonLevel(UUID p, float newLevel) {
        //loop to preserved initial insert order
        for (Map.Entry<UUID, Float> entry : mTempSplitGroup.entrySet()) {
            UUID personId = entry.getKey();
            Float personLevel = entry.getValue();

            if(personId.equals(p)){
                mTempSplitGroup.put(personId, newLevel);
            }else {
                mTempSplitGroup.put(personId, personLevel);
            }
        }
    }

    public void setEveryonesLevel(float newLevel) {
        for (Map.Entry<UUID, Float> entry : mTempSplitGroup.entrySet()) {
            mTempSplitGroup.put(entry.getKey(), newLevel);
        }
    }

    public float roundToMoney(float amount){
        return Math.round((amount)*100)/100;
    }


    /*Bill Amount Functions*/

    public float getBillAmount() {
        return mBillAmount;
    }

    public void setBillAmount(float billAmount) {
        setTipAmount(billAmount*getTipPercentage());
        mBillAmount = (billAmount < 0) ? 0.00f : billAmount;
    }

    public float getTipAmount() {
        return mTipAmount;
    }

    public void setTipAmount(float tipAmount) {
        mTipAmount = (tipAmount < 0) ? 0.00f : tipAmount;
    }

    public float getTipPercentage(){
        if ( mBillAmount <= 0 ) {
            return 0.15f;
        }
        return mTipAmount/mBillAmount;
    }

    public float getPersonAmount(UUID personUUID){
        //get person percentage
        float total = 0.0f;
        float personLevel = 0.0f;
        for (Map.Entry<UUID, Float> entry : mSplitGroup.entrySet()) {
            UUID personId = entry.getKey();
            Float level = entry.getValue();
            total = total + level;
            if(personId.equals(personUUID)) {
                personLevel = level;
            }
        }
        return roundToMoney(getBillTotal() * personLevel/total);
    }

    /*Person Adding Functions*/

    public boolean isPersonOnBill(UUID personUUID) {
        return mTempSplitGroup.containsKey(personUUID);
    }

    public void addPersonToBill(UUID personUUID) {
        if (!isPersonOnBill(personUUID))
            mTempSplitGroup.put(personUUID, new Float("0"));
    }

    public void removePersonFromBill(UUID personUUID) {
        mTempSplitGroup.remove(personUUID);
    }

    public void removeAllPersons(){ mTempSplitGroup.clear(); }

    public int getPersonCount() {
        return mTempSplitGroup.size();
    }


    //commit hashmap changes
    public void commit() {
        mSplitGroup = new LinkedHashMap<UUID, Float>(mTempSplitGroup);
    }

    //revert hashmap to last commit
    public void revert() {
        mTempSplitGroup= new LinkedHashMap<UUID, Float>(mSplitGroup);
    }


    /*generic gettter, setters, and toString*/
    @Override
    public String toString() {
        return mBillTitle;
    }

    public LinkedHashMap<UUID, Float> getSplitGroup() {
        return mSplitGroup;
    }

    public Set<UUID> getPersonIds() {
        return mSplitGroup.keySet();
    }

    public UUID getId() {
        return mId;
    }

    public String getBillTitle() {
        return mBillTitle;
    }

    public void setBillTitle(String billTitle) {
        mBillTitle = billTitle;
    }

}
