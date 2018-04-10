package g.project.giftthingapp;

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
import com.google.firebase.database.ValueEventListener;


public class fWishlist extends Fragment {

    //Param argument names
    private static final String ARG_UID = "user-id";
    private static final String ARG_INDEX = "wishlist-name";

    // Params
    private String uID;
    private int index;

    //Wishlist object for display information
    private Wishlist currentWishlist;

    //Views
    private LinearLayout itemLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fWishlist() {
    }

    public static fWishlist newInstance(String uID, int index) {
        fWishlist fragment = new fWishlist();
        Bundle args = new Bundle();
        args.putString(ARG_UID, uID);
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            uID = getArguments().getString(ARG_UID);
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //initialize Wishlist object
        currentWishlist = new Wishlist();

        //initialize views
        itemLayout = this.getView().findViewById(R.id.item_layout);

        //Connect to current wishlist in Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profile/User/" + uID + "/wishlistList/" + Integer.toString(index));

        //Will update view every time current user's friends list is updated in
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentWishlist = dataSnapshot.getValue(Wishlist.class);
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
        for(int i = 0; i < currentWishlist.getItemList().size(); i++) {
            FragmentManager fm = ((MainActivity) fWishlist.this
                    .getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            WishlistItem item = currentWishlist.getItemList().get(i);

            fWishlistItem fragment = fWishlistItem.newInstance(item);
            ft.add(R.id.item_layout, fragment);

            ft.commit();
        }
    }

    //grab display information from each friend in current user's friend list
    public void getItemInfo()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Get display information from wishlist items
        for(int i = 0; i < currentWishlist.getItemList().size(); i++) {

            //get reference to single item on list
            DatabaseReference myRef = database.getReference("Profile/User/" + uID + "/wishlistList/" + index + "/ItemList/" + Integer.toString(i));

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                WishlistItem item = new WishlistItem();

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    item = dataSnapshot.getValue(WishlistItem.class);
                    //drawListItem(item);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

}
