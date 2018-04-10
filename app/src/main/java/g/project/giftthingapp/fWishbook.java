package g.project.giftthingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import g.project.giftthingapp.dummy.DummyContent.DummyItem;


public class fWishbook extends Fragment {

    //list of wishbook display information
    ArrayList<Wishlist> wishlists;

    //param argument names
    private static final String ARG_UID = "user-id";

    //param
    private String uID;

    //Views
    private LinearLayout wishlistLayout;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fWishbook() {
    }

    public static fWishbook newInstance(String uID) {
        fWishbook fragment = new fWishbook();
        Bundle args = new Bundle();
        args.putString(ARG_UID, uID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            uID = getArguments().getString(ARG_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishbook, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //initialize list
        wishlists = new ArrayList<>();

        //initialize views
        wishlistLayout = this.getView().findViewById(R.id.wishlist_layout);

        //Connect to current wishlist in Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profile/User/" + uID + "/wishlistList");

        //Will update view every time current user's friends list is updated in
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<Wishlist>> genericTypeIndicator =new GenericTypeIndicator<ArrayList<Wishlist>>(){};

                wishlists = dataSnapshot.getValue(genericTypeIndicator);
                //wishlists = dataSnapshot.getValue(ArrayList<>.class);
                drawListItems();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    public void drawListItems()
    {
        for(int i = 0; i < wishlists.size(); i++) {
            FragmentManager fm = ((MainActivity) fWishbook.this
                    .getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Wishlist list = wishlists.get(i);

            fWishbookItem fragment = fWishbookItem.newInstance(list, uID, i);
            ft.add(R.id.wishlist_layout, fragment);

            ft.commit();
        }
    }

    //grab display information from each friend in current user's friend list
    public void getListInfo()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Get display information from wishlist items
        for(int i = 0; i < wishlists.size(); i++) {

            //get reference to single item on list
            DatabaseReference myRef = database.getReference("Profile/User/" + uID + "/wishlistList/" + Integer.toString(i));

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                Wishlist list = new Wishlist();

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    list = dataSnapshot.getValue(Wishlist.class);
                    //drawListItem(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }
}
