package g.project.giftthingapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class fProfile extends Fragment implements View.OnClickListener {

    //Buttons
    FloatingActionButton fab_edit;

    //Text Views
    TextView profile_name;
    TextView profile_content_bio;
    TextView profile_content_yes;
    TextView profile_content_no;

    //Profile for holding display information
    Profile userProfile;

    public static fProfile newInstance(String uID) {
        fProfile myFragment = new fProfile();

        Bundle args = new Bundle();
        args.putString("uID", uID);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        fab_edit = (FloatingActionButton) this.getView().findViewById(R.id.profile_edit_fab);
        fab_edit.setOnClickListener(this);

        profile_name = this.getView().findViewById(R.id.profile_name);
        profile_content_bio = this.getView().findViewById(R.id.profile_content_bio);
        profile_content_yes = this.getView().findViewById(R.id.profile_content_yes);
        profile_content_no = this.getView().findViewById(R.id.profile_content_no);

        userProfile = new Profile();

        String uID = getArguments().getString("uID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profile/User/" + uID);
        //myRef.setValue(userProfile);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(Profile.class);
                initializeViews();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    public void initializeViews(){
        profile_content_bio.setText(userProfile.getBiography());
        profile_content_yes.setText(userProfile.getLikesAsString());
        profile_content_no.setText(userProfile.getDislikesAsString());
        profile_name.setText(userProfile.getName());

        FrameLayout frag_frame = (FrameLayout) this.getView().findViewById(R.id.wishlists_place);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.wishlists_place, fWishbook.newInstance(userProfile.getUid()));
        ft.commit();

    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.profile_edit_fab:
                Snackbar.make(v, "EDIT ALL THE THINGS!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            default:
                break;
        }
    }

}
