package g.project.giftthingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import g.project.giftthingapp.dummy.DummyContent.DummyItem;


public class fWishbook extends Fragment implements View.OnClickListener {


    //Firebase
    FirebaseDatabase database;

    //list of wishbook display information
    ArrayList<Wishlist> wishlists;
    private boolean needsRefresh;

    //param argument names
    private static final String ARG_UID = "user-id";

    //param
    private String uID;

    //Views
    private LinearLayout wishlistLayout;
    private LinearLayout wishlistCreationForm;
    private EditText addName;
    private EditText addDescription;
    private CardView newWishlistButton;
    private Button addWishlistButton;


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
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //initialize list
        wishlists = new ArrayList<>();

        //initialize views
        wishlistLayout = this.getView().findViewById(R.id.wishlist_layout);
        wishlistCreationForm = this.getView().findViewById(R.id.wishlist_creation_form);
        addName = this.getView().findViewById(R.id.add_name);
        addDescription = this.getView().findViewById(R.id.add_description);
        newWishlistButton = this.getView().findViewById(R.id.new_wishlist_button);
        addWishlistButton = this.getView().findViewById(R.id.add_wishlist_button);

        Snackbar.make(wishlistLayout, "onViewCreated run", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        //set onclick listeners
        newWishlistButton.setOnClickListener(this);
        addWishlistButton.setOnClickListener(this);

        //If wishlist does not belong to current user, don't allow editing
        if(!(MainActivity.userProfile.getUid() == uID)){
            newWishlistButton.setVisibility(View.GONE);
        }

        //Connect to current wishlist in Firebase
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profile/User/" + uID + "/wishbook");

        //Will update view every time current user's friends list is updated in
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                wishlistLayout.removeAllViews();

                GenericTypeIndicator<ArrayList<Wishlist>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Wishlist>>(){};
                System.out.println("good");
                wishlists = dataSnapshot.getValue(genericTypeIndicator);
                System.out.println("still good");

                if(!(wishlists == null)) drawListItems();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    public void drawListItems()
    {
        System.out.println("in drawListItems");

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        for(int i = 0; i < wishlists.size(); i++) {

            Wishlist list = wishlists.get(i);

            fWishbookItem fragment = fWishbookItem.newInstance(list, uID, i);
            ft.add(wishlistLayout.getId(), fragment);


        }

        ft.commit();
    }

    public void addWishlist()
    {
        Wishlist newWishlist = new Wishlist();

        newWishlist.setListName(addName.getText().toString());
        newWishlist.setListDescription(addDescription.getText().toString());

        Date currentTime = Calendar.getInstance().getTime();
        newWishlist.setDateCreated(currentTime.toString());

        DatabaseReference myRef;

        if(wishlists == null) myRef = database.getReference("Profile/User/" + uID + "/wishbook/0");
        else myRef = database.getReference("Profile/User/" + uID + "/wishbook/" + Integer.toString(wishlists.size()));

        myRef.setValue(newWishlist);
        System.out.println(myRef.toString());

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

    }

    @Override
    public void onClick(View v) {

        FragmentManager fm = ((MainActivity) fWishbook.this
                .getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //When item is clicked, load profile fragment with friend information
        int i = v.getId();
        if (i == R.id.new_wishlist_button) {
            wishlistCreationForm.setVisibility(View.VISIBLE);
        }
        if (i == R.id.add_wishlist_button) {
            addWishlist();
            wishlistCreationForm.setVisibility(View.GONE);
        }
    }
}
