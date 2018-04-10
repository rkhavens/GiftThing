package g.project.giftthingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fFriendList extends Fragment implements View.OnClickListener {

    //Create Views
    private LinearLayout friendsLayout;

    //List for storing uID of each of the current users friends
    private ArrayList<String> friendsList;

    private int index = 0;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fFriendList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        //Initialize views
        friendsLayout = this.getView().findViewById(R.id.friendLayout);

        //Get friend uIDs
        friendsList = MainActivity.userProfile.getFriendsList();

        //Connect to current users friends list in Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profile/User/" + MainActivity.userProfile.getUid() + "/friendsList");

        //Will update view every time current user's friends list is updated in
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsLayout.removeAllViews();
                getFriendInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);

        // Set the adapter
        return view;
    }

    //Draw and display a single friend item in friendLayout
    public void drawListItem(String uID, String name)
    {
        index++;

        FragmentManager fm = ((MainActivity)fFriendList.this
                .getActivity()).getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();

        fFriendlist_Item fragment = fFriendlist_Item.newInstance(uID, name, Integer.toString(index));
        ft.add(R.id.friendLayout, fragment);

        ft.commit();
    }

    //grab display information from each friend in current user's friend list
    public void getFriendInfo()
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Get display information from users friendslist
        for(int i = 0; i < friendsList.size(); i++) {

            DatabaseReference myRef = database.getReference("Profile/User/" + friendsList.get(i));

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                Profile user = new Profile();

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(Profile.class);
                    drawListItem(user.getUid(), user.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    @Override
    public void onClick(View v){
        /*switch (v.getId()){
            case R.id.profile_edit_fab:
                Snackbar.make(v, "EDIT ALL THE THINGS!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            default:
                break;
        }*/
    }
}