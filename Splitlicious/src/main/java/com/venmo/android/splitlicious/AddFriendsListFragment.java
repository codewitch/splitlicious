package com.venmo.android.splitlicious;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.venmo.android.splitlicious.layouts.SplitliciousButton;
import com.venmo.android.splitlicious.models.Bill;
import com.venmo.android.splitlicious.models.BillLab;
import com.venmo.android.splitlicious.models.Person;
import com.venmo.android.splitlicious.models.FriendLab;
import com.venmo.android.splitlicious.api.Pablo;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by thomasjeon on 6/30/14.
 */
public class AddFriendsListFragment extends ListFragment{

    private static final String TAG = "AddFriendsListFragment";
    public static final String EXTRA_BILL_ID =
            "com.venmo.android.splitlicious.bill_id";

    private ArrayList<Person> mFriends;
    private Bill mBill;

    private EditText mSearchBarEditText;
    private SplitliciousButton mAddFriendsFinishButton;

    private Callbacks mCallbacks;
    private boolean mCancelledAddFriends = true;

    public static AddFriendsListFragment newInstance(UUID billId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BILL_ID, billId);

        AddFriendsListFragment fragment = new AddFriendsListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID billId = (UUID)getArguments().getSerializable(EXTRA_BILL_ID);
        mBill = BillLab.get(getActivity()).getBill(billId);
        mFriends = FriendLab.get(getActivity()).getFriends();

        FriendAdapter adapter =
                new FriendAdapter(mFriends);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friendslist_list, container, false);

        mSearchBarEditText = (EditText)v.findViewById(R.id.add_friends_search_EditText);
        mSearchBarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //TODO: only display search matching people
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        Runnable closeAddFriendsListRunnable = new Runnable() {
            @Override
            public void run() {
                mCancelledAddFriends = false;
                mCallbacks.closeAddFriendsList();
            }
        };
        mAddFriendsFinishButton = (SplitliciousButton)v.findViewById(R.id.add_friends_finish_button);
        mAddFriendsFinishButton.setOnTouchListener(new SplitliciousButtonListener(mAddFriendsFinishButton, closeAddFriendsListRunnable, 0.3f));

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Person f = ((FriendAdapter)getListAdapter()).getItem(position);
        //add/remove to bill
        if (mBill.isPersonOnBill(f.getId())){
            mBill.removePersonFromBill(f.getId());
        } else {
            mBill.addPersonToBill(f.getId());
        }
    }

    private class FriendAdapter extends ArrayAdapter<Person> {

        public FriendAdapter(ArrayList<Person> friends) {
            super(getActivity(), 0, friends);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_friend, null);
            }

            //Configure the view for this Friend
            Person f = getItem(position);

            ImageView friendImageView = (ImageView) convertView.findViewById(R.id.friend_list_item_pictureImageView);
            Pablo.with(getActivity())
                    .load(f.getProfilePictureUrl())
                    .placeholder(R.drawable.no_image)
                    .fit()
                    .centerCrop()
                    .into(friendImageView);

            CheckedTextView itemCheckedTextView =
                    (CheckedTextView)convertView.findViewById(R.id.friend_list_item_CheckedTextView);
            itemCheckedTextView.setText(f.getFirstName() + " " + f.getLastName());

            //TODO: Check if this implementation is okay
            ((ListView)parent).setItemChecked(position, mBill.isPersonOnBill(f.getId()));

            return convertView;
        }
    }


    /*Callbacks!*/
    public interface Callbacks {
        void closeAddFriendsList();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FriendAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        if (mCancelledAddFriends) {
            mBill.revert();
        }else {
            mBill.commit();
        }
    }
}
