package g.project.giftthingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fFriendList extends Fragment implements View.OnClickListener {

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //Create Views
    private LinearLayout friendsLayout;
    private LinearLayout resultsLayout;
    private LinearLayout resultsHeader;
    private TextView searchFriendText;
    private Button searchFriendButton;

    //List for storing uID of each of the current users friends
    private ArrayList<String> friendsList;
    private ArrayList<String> searchResults;

    private int index = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        //Initialize views
        friendsLayout = this.getView().findViewById(R.id.friend_layout);
        resultsLayout = this.getView().findViewById(R.id.results_layout);
        resultsHeader = this.getView().findViewById(R.id.results_header);
        searchFriendText = this.getView().findViewById(R.id.search_friend_text);
        searchFriendButton = this.getView().findViewById(R.id.search_friend_button);

        //On Clicks
        searchFriendButton.setOnClickListener(this);

        //Get friend uIDs
        friendsList = MainActivity.userProfile.getFriendsList();

        //Connect to current users friends list in Firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Profile/User/" + MainActivity.userProfile.getUid() + "/friendsList");

        //Will update view every time current user's friends list is updated in
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsLayout.removeAllViews();
                getFriendInfo(friendsList, false);
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
        return view;
    }

    //Draw and display a single friend item in friendLayout
    public void drawListItem(String uID, String name, boolean search)
    {
        index++;
        boolean isFriend = true;

        //if drawing search results, check if a result is already a friend of current user
        if(search) {
            isFriend = false;
            for (int i = 0; i < friendsList.size(); i++) {
                if (uID.equals(friendsList.get(i))) {
                    isFriend = true;
                    i = friendsList.size() + 1;
                }
            }
        }

        FragmentManager fm = ((MainActivity)fFriendList.this
                .getActivity()).getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();

        fFriendlist_Item fragment = fFriendlist_Item.newInstance(uID, name, Integer.toString(index), isFriend);
        if(search)
            ft.add(R.id.results_layout,fragment);
        else
            ft.add(R.id.friend_layout, fragment);

        ft.commit();
    }

    //grab display information from each friend in current user's friend list
    public void getFriendInfo(ArrayList<String> list, final boolean search)
    {
        //Get display information from users friendslist
        for(int i = 0; i < list.size(); i++) {

            myRef = database.getReference("Profile/User/" + list.get(i));

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                Profile user = new Profile();

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(Profile.class);
                    drawListItem(user.getUid(), user.getName(), search);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    public void searchForFriend(){
        String query =  searchFriendText.getText().toString().toUpperCase().replace(" ", "");

        myRef = database.getReference("Profile/Usernames/" + query);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                searchResults = new ArrayList<>();

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    searchResults.add(childSnapshot.getValue(String.class));
                }
                getFriendInfo(searchResults, true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.search_friend_button) {
            resultsHeader.setVisibility(View.VISIBLE);
            resultsLayout.removeAllViews();
            searchForFriend();
        }
    }
}