package com.venmo.android.splitlicious;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.venmo.android.splitlicious.api.VenmoLibrary;
import com.venmo.android.splitlicious.api.VenmoWebViewActivity;
import com.venmo.android.splitlicious.layouts.SplitliciousButton;
import com.venmo.android.splitlicious.layouts.TipEditText;
import com.venmo.android.splitlicious.layouts.makeramen.RoundedImageView;
import com.venmo.android.splitlicious.layouts.SplitBarView;
import com.venmo.android.splitlicious.models.Bill;
import com.venmo.android.splitlicious.models.BillLab;
import com.venmo.android.splitlicious.models.Person;
import com.venmo.android.splitlicious.models.FriendLab;
import com.venmo.android.splitlicious.api.Pablo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitFragment extends Fragment {

    private static final int[] COLOR_WHEEL = {R.color.yellow, R.color.blue, R.color.red, R.color.purple};
    private static final String DOLLAR_REGEX = "^[0-9]*(\\.[0-9]{0,2})?$";

/*View member variables*/
    private EditText mBillAmountField;
    private TipEditText mTipAmountField;

    private Button mConnectVenmoButton;

    private RelativeLayout mSplitSectionWrapperRelativeLayout;
    private LinearLayout mSplitSectionLinearLayout;
    private View mAddFriendButton;

    private SplitliciousButton mSplitFinishButton;
    private LinearLayout mSplitFinishView;

    //Model member variables
    private Person mSelf;
    private Bill mBill;

    private Callbacks mCallbacks;

    public static SplitFragment newInstance() {
        Bundle args = new Bundle();
        //args.putSerializable();

        SplitFragment fragment = new SplitFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get self, bill, and friend list
        mSelf = Person.setSelf(getActivity(), getString(R.string.self_first_name), getString(R.string.self_last_name), getString(R.string.self_profile_picture_url));
        mBill = BillLab.get(getActivity()).addNewBill();

        //Auto add yourself to bill
        mBill.addPersonToBill(mSelf.getId());
        mBill.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_split, parent, false);

        initBillAmountSection(v);

        mConnectVenmoButton = (Button)v.findViewById(R.id.connect_venmo_button);
        Runnable connectVenmoRunnable = new Runnable() {
            @Override
            public void run() {
                //TODO: connect to venmo with oauth
            }
        };
        mConnectVenmoButton.setOnTouchListener(new SplitliciousButtonListener(mConnectVenmoButton, connectVenmoRunnable, 0.5f));

        /*mSplitSectionWrapperRelativeLayout = (RelativeLayout)v.findViewById(R.id.split_bill_section_wrapper);

        mSplitSectionLinearLayout = (LinearLayout)v.findViewById(R.id.split_bill_section);
        initSplitSection(inflater, parent);


        Runnable openAddFriendsListRunnable = new Runnable() {
            @Override
            public void run() {
                mCallbacks.openAddFriendsList(mBill.getId());
            }
        };
        mAddFriendButton = v.findViewById(R.id.add_friends_buttonImageButton);
        mAddFriendButton.setOnTouchListener(new SplitliciousButtonListener(mAddFriendButton, openAddFriendsListRunnable, 0.3f));

        mSplitFinishView = (LinearLayout)v.findViewById(R.id.split_finish_view);

        mSplitFinishButton = (SplitliciousButton)v.findViewById(R.id.split_finish_button);
        Runnable splitFinishRunnable = new Runnable() {
            @Override
            public void run() {
                //TODO: split the bill
                //animate the button up (change the height)
                Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.button_slide_up);
                slideUp.setFillAfter(true);
                slideUp.setFillEnabled(true);
                mSplitFinishView.startAnimation(slideUp);
                mSplitFinishButton.startAnimation(slideUp);
            }
        };
        mSplitFinishButton.setOnTouchListener(new SplitliciousButtonListener(mSplitFinishButton, splitFinishRunnable, 0.3f));
*/
        return v;
    }

    //initialize top bar with bill amount with listeners and filters
    private void initBillAmountSection(View v){
        //Bill amount view
        mBillAmountField = (EditText)v.findViewById(R.id.bill_amount);
        mBillAmountField.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        //dollar format filter
                        String result =
                                dest.subSequence(0, dstart)
                                        + source.toString()
                                        + dest.subSequence(dend, dest.length());

                        Matcher matcher = Pattern.compile(DOLLAR_REGEX).matcher(result);

                        if (!matcher.matches()) return dest.subSequence(dstart, dend);

                        return null;
                    }
                }
        });
        mBillAmountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence c, int i, int i2, int i3) {
                if(!mBillAmountField.getText().toString().equals("")) {
                    mBill.setBillAmount(Float.parseFloat(mBillAmountField.getText().toString()));
                    updateSplitViews();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        //Tip amount view
        mTipAmountField = (TipEditText)v.findViewById(R.id.tip_amount);
        mTipAmountField.setOnTouchListener(new SplitliciousTipTouchListener(mTipAmountField, mBill.getId(), getActivity()));
        //TODO: update bars when tip changes
    }



    //go through added people on mBills
    private void initSplitSection(LayoutInflater inflater, ViewGroup parent) {
        int colorCount = 0;

        LinkedHashMap<UUID, Float> addedPersons = mBill.getSplitGroup();

        for (Map.Entry<UUID, Float> entry : addedPersons.entrySet()) {
            addPersonToSplitView(inflater, parent, entry.getKey(), COLOR_WHEEL[colorCount]);
            colorCount ++;
            if(colorCount > 3){ colorCount = 0; }
        }

        addFriendsButtonToSplitView(inflater, parent, mBill.getPersonCount());

        mBill.commit();
    }

    private void addPersonToSplitView(LayoutInflater inflater, ViewGroup parent, UUID personId, int color) {

        //get person to add
        Person personToAdd = FriendLab.get(getActivity()).getFriend(personId);
        if (personId.equals(mSelf.getId())){
            personToAdd = mSelf;
        }

        //inflate main linear layout
        LinearLayout userLinearLayout = (LinearLayout)inflater.inflate(R.layout.split_section_user_linear_layout, mSplitSectionLinearLayout, false);
        userLinearLayout.setId(R.id.split_section_LinearLayout);
        mSplitSectionLinearLayout.addView(userLinearLayout);

        //bar
        SplitBarView userBarAmount = (SplitBarView)userLinearLayout.getChildAt(0);
        userBarAmount.setId(R.id.split_section_bar_TextView);
        userBarAmount.setText("$" + String.format("%.2f", mBill.getPersonAmount(personId)));
        userBarAmount.setBorderColor(color);
        userBarAmount.setOnTouchListener(new SplitliciousBarTouchListener(userBarAmount, mBill.getId(), personId, getActivity()));

        ImageView userBarLine = (ImageView)userLinearLayout.getChildAt(1);
        userBarLine.setId(R.id.split_section_bar_ImageView);

        LinearLayout imageLinearLayout = (LinearLayout)userLinearLayout.getChildAt(2);
        imageLinearLayout.setId(R.id.split_section_image_LinearLayout);
        imageLinearLayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //TODO: Set this person as payer (star the person)
            }
        });

        //profile pic
        RoundedImageView userImage = (RoundedImageView)imageLinearLayout.getChildAt(0);
        userImage.setId(R.id.split_section_image_ImageView);
        userImage.setBorderColor(getResources().getColor(color));

        Pablo.with(getActivity())
                .load(personToAdd.getProfilePictureUrl())
                .placeholder(R.drawable.no_image)
                .fit()
                .centerCrop()
                .into(userImage);

        //profile Name
        TextView userTextView = (TextView)imageLinearLayout.getChildAt(1);
        userTextView.setText(personToAdd.getFirstName());
    }

    private void addFriendsButtonToSplitView(LayoutInflater inflater, ViewGroup parent, int numPersonOnView) {
        View addFriendsButtonLinearLayout;
        if(numPersonOnView < 2){
            addFriendsButtonLinearLayout = inflater.inflate(R.layout.split_section_add_friends_bar, mSplitSectionLinearLayout, false);
            mSplitSectionLinearLayout.addView(addFriendsButtonLinearLayout);
        }else {
            addFriendsButtonLinearLayout = inflater.inflate(R.layout.split_section_add_friends_side, mSplitSectionWrapperRelativeLayout, false);
            mSplitSectionWrapperRelativeLayout.addView(addFriendsButtonLinearLayout);
        }
    }

    private void updateSplitViews(){

        //update bar amounts
        int index = 0;
        int indexMax = mSplitSectionLinearLayout.getChildCount();

        for (Map.Entry<UUID, Float> entry : mBill.getSplitGroup().entrySet()) {
            UUID personId = entry.getKey();

            if(index < indexMax) {
                //TODO: crash where you add people to existing bill
                if (mSplitSectionLinearLayout.getChildAt(index).getId() != R.id.add_friends_button_LinearLayout){
                    SplitBarView userBarAmount = (SplitBarView) mSplitSectionLinearLayout.getChildAt(index).findViewById(R.id.split_section_bar_TextView);
                    mBill.setPersonLevel(personId, userBarAmount.getBarLevel());
                    mBill.commit();
                    userBarAmount.setText("$" + String.format("%.2f", mBill.getPersonAmount(personId)));
                }
            }

            index++;
        }

        //edit tip amount
        String newTipAmount = "$" + String.format("%.2f", mBill.getTipAmount());
        mTipAmountField.setTipAmount(newTipAmount);

    }

    public void splitEven(){

        float evenLevel = 0.0f;

        for (int i = 0; i < mSplitSectionLinearLayout.getChildCount(); i++){
            SplitBarView userBarAmount = (SplitBarView)mSplitSectionLinearLayout.getChildAt(i).findViewById(R.id.split_section_bar_TextView);
            userBarAmount.setBarTop(userBarAmount.getBarEvenLevel());
            userBarAmount.invalidate();

            evenLevel = userBarAmount.getBarEvenLevel();
        }

        mBill.setEveryonesLevel(evenLevel);
        mBill.commit();
    }

    /*Callbacks!*/
    public interface Callbacks {
        void openAddFriendsList(UUID billId);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
