package g.project.giftthingapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

/**
 * Created by MarcP on 4/3/2018.
 */

public class fFriendlist_Item extends Fragment implements View.OnClickListener {

    //Views
    private LinearLayout itemBox;
    private TextView friend_name;
    private TextView friend_pos;
    private Button addFriendButton;

    //variables
    private String uID;
    private int friendCount;

    //Use this function to generate a new instance of fFriendlist_Item
    public static fFriendlist_Item newInstance(String uID, String name, int friendCount, String pos, boolean isFriend) {
        fFriendlist_Item myFragment = new fFriendlist_Item();

        Bundle args = new Bundle();
        args.putString("uID", uID);
        args.putString("name", name);
        args.putString("pos", pos);
        args.putInt("friendCount", friendCount);
        args.putBoolean("isFriend",isFriend);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        uID = getArguments().getString("uID");
        friendCount = getArguments().getInt("friendCount");

        //Initialize display views
        itemBox = this.getView().findViewById(R.id.layout_itemBox);
        friend_name = this.getView().findViewById(R.id.friend_name);
        friend_pos = this.getView().findViewById(R.id.item_number);
        addFriendButton = this.getView().findViewById(R.id.add_friend_button);

        //Set onClickListeners
        itemBox.setOnClickListener(this);
        addFriendButton.setOnClickListener(this);

        //Set displays with display information
        friend_name.setText(getArguments().getString("name"));
        friend_pos.setText(getArguments().getString("pos"));

        if(getArguments().getBoolean("isFriend"))
            addFriendButton.setVisibility(View.GONE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friendlist_item, container, false);
    }

    @Override
    public void onClick(View v) {

        FragmentManager fm = ((MainActivity) fFriendlist_Item.this
                .getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //When item is clicked, load profile fragment with friend information
        int i = v.getId();
        if (i == R.id.layout_itemBox) {
            ft.replace(R.id.fragment_place, fProfile.newInstance(getArguments().getString("uID")));
            ft.commit();
        }
        else if(i == R.id.add_friend_button){
            System.out.println("pushed");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Profile/User/" + MainActivity.userProfile.getUid() + "/friendsList/" + MainActivity.userProfile.getNumberOfFriends());
            myRef.setValue(uID);

            myRef = database.getReference("Profile/User/" + uID + "/friendsList/" + friendCount);
            myRef.setValue(MainActivity.userProfile.getUid());

            ft.replace(R.id.fragment_place, new fFriendList());
            ft.commit();
        }
    }
}
