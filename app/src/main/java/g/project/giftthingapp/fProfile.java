package g.project.giftthingapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class fProfile extends Fragment implements View.OnClickListener {

    //Buttons
    FloatingActionButton fab_edit;
    FrameLayout frag_frame;

    //Text Views
    TextView profile_name;
    TextView profile_content_bio;
    TextView profile_content_yes;
    TextView profile_content_no;

    ImageView profile_bio_expand;
    ImageView profile_bio_collapse;
    ImageView profile_yes_expand;
    ImageView profile_yes_collapse;
    ImageView profile_no_expand;
    ImageView profile_no_collapse;

    CardView profile_bio_card;
    CardView profile_yes_card;
    CardView profile_no_card;
    CardView profile_wishbook_card;

    boolean edit_mode;

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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        edit_mode = false;

        //initialize views
        //TODO: makde fab_edit invisible to non profile users
        fab_edit = (FloatingActionButton) this.getView().findViewById(R.id.profile_edit_fab);
        frag_frame = (FrameLayout) this.getView().findViewById(R.id.wishlists_place);

        profile_name = this.getView().findViewById(R.id.profile_name);

        profile_bio_card = this.getView().findViewById(R.id.profile_bio);
        profile_yes_card = this.getView().findViewById(R.id.profile_yes);
        profile_no_card = this.getView().findViewById(R.id.profile_no);
        profile_wishbook_card = this.getView().findViewById(R.id.profile_wishlists);

        profile_content_bio = this.getView().findViewById(R.id.profile_content_bio);
        profile_content_yes = this.getView().findViewById(R.id.profile_content_yes);
        profile_content_no = this.getView().findViewById(R.id.profile_content_no);

        //expansion/collapse listeners
        profile_bio_collapse = this.getView().findViewById(R.id.profile_bio_ic_collapse);
        profile_bio_expand = this.getView().findViewById(R.id.profile_bio_ic_expand);
        profile_yes_collapse = this.getView().findViewById(R.id.profile_yes_ic_collapse);
        profile_yes_expand = this.getView().findViewById(R.id.profile_yes_ic_expand);
        profile_no_collapse = this.getView().findViewById(R.id.profile_no_ic_collapse);
        profile_no_expand = this.getView().findViewById(R.id.profile_no_ic_expand);

        //set on click listeners
        fab_edit.setOnClickListener(this);
        frag_frame.setOnClickListener(this);

        profile_bio_expand.setOnClickListener(this);
        profile_bio_collapse.setOnClickListener(this);
        profile_yes_collapse.setOnClickListener(this);
        profile_yes_expand.setOnClickListener(this);
        profile_no_expand.setOnClickListener(this);
        profile_no_collapse.setOnClickListener(this);

        profile_bio_card.setOnClickListener(this);
        profile_yes_card.setOnClickListener(this);
        profile_no_card.setOnClickListener(this);
        profile_wishbook_card.setOnClickListener(this);

        userProfile = new Profile();

        // prevent editing
        profile_content_bio.setEnabled(false);
        profile_content_yes.setEnabled(false);
        profile_content_no.setEnabled(false);

        //default testing
        profile_content_bio.setText(R.string.profile_bio);
        profile_content_yes.setText(R.string.profile_yes);
        profile_content_no.setText(R.string.profile_no);

        String uID = getArguments().getString("uID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profile/User/" + uID);
        //myRef.setValue(userProfile);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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


        //open wishlist view inside profile
        /*
        FrameLayout frag_frame = (FrameLayout) this.getView().findViewById(R.id.wishlists_place);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.wishlists_place, fWishbook.newInstance(userProfile.getUid()));
        ft.commit();
        */
    }
    @Override
    public void onClick(View v){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.profile_edit_fab:
                // TODO: Update profile edit button
                if (!edit_mode)
                {
                    edit_mode = true;
                    // expand all cards, hide things
                    profile_content_bio.setVisibility(View.VISIBLE);
                    profile_bio_expand.setVisibility(View.GONE);
                    profile_bio_collapse.setVisibility(View.GONE);

                    profile_content_yes.setVisibility(View.VISIBLE);
                    profile_yes_expand.setVisibility(View.GONE);
                    profile_yes_collapse.setVisibility(View.GONE);

                    profile_content_no.setVisibility(View.VISIBLE);
                    profile_no_expand.setVisibility(View.GONE);
                    profile_no_collapse.setVisibility(View.GONE);

                    Snackbar.make(v, "Edit Mode On", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    edit_mode = false;

                    // un-hide things
                    profile_content_bio.setVisibility(View.VISIBLE);
                    profile_bio_expand.setVisibility(View.GONE);
                    profile_bio_collapse.setVisibility(View.VISIBLE);

                    profile_content_yes.setVisibility(View.VISIBLE);
                    profile_yes_expand.setVisibility(View.GONE);
                    profile_yes_collapse.setVisibility(View.VISIBLE);

                    profile_content_no.setVisibility(View.VISIBLE);
                    profile_no_expand.setVisibility(View.GONE);
                    profile_no_collapse.setVisibility(View.VISIBLE);

                    Snackbar.make(v, "Edit Mode Off", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }

                // make all content fields editable/uneditable
                profile_content_bio.setEnabled(edit_mode);
                profile_content_yes.setEnabled(edit_mode);
                profile_content_no.setEnabled(edit_mode);
                break;
            case R.id.wishlists_place:
                ft.replace(R.id.fragment_place, fWishbook.newInstance(userProfile.getUid()));
                ft.commit();
                ((MainActivity) getActivity())
                        .setActionBarTitle(userProfile.getName() + "'s Wishlists");
                break;
            case R.id.profile_bio:
                if (profile_content_bio.getVisibility() == View.GONE) {
                    profile_content_bio.setVisibility(View.VISIBLE);
                    profile_bio_expand.setVisibility(View.GONE);
                    profile_bio_collapse.setVisibility(View.VISIBLE);
                }
                else{
                    profile_content_bio.setVisibility(View.GONE);
                    profile_bio_expand.setVisibility(View.VISIBLE);
                    profile_bio_collapse.setVisibility(View.GONE);
                }
                break;
            case R.id.profile_bio_ic_collapse:
                profile_content_bio.setVisibility(View.GONE);
                profile_bio_expand.setVisibility(View.VISIBLE);
                profile_bio_collapse.setVisibility(View.GONE);
                break;
            case R.id.profile_bio_ic_expand:
                profile_content_bio.setVisibility(View.VISIBLE);
                profile_bio_expand.setVisibility(View.GONE);
                profile_bio_collapse.setVisibility(View.VISIBLE);
                break;
            case R.id.profile_yes:
                if (profile_content_yes.getVisibility() == View.GONE) {
                    profile_content_yes.setVisibility(View.VISIBLE);
                    profile_yes_expand.setVisibility(View.GONE);
                    profile_yes_collapse.setVisibility(View.VISIBLE);
                }
                else{
                    profile_content_yes.setVisibility(View.GONE);
                    profile_yes_expand.setVisibility(View.VISIBLE);
                    profile_yes_collapse.setVisibility(View.GONE);
                }
                break;
            case R.id.profile_yes_ic_collapse:
               profile_content_yes.setVisibility(View.GONE);
               profile_yes_expand.setVisibility(View.VISIBLE);
               profile_yes_collapse.setVisibility(View.GONE);
                break;
            case R.id.profile_yes_ic_expand:
                profile_content_yes.setVisibility(View.VISIBLE);
                profile_yes_expand.setVisibility(View.GONE);
                profile_yes_collapse.setVisibility(View.VISIBLE);
                break;
            case R.id.profile_no:
                if (profile_content_no.getVisibility() == View.GONE) {
                    profile_content_no.setVisibility(View.VISIBLE);
                    profile_no_expand.setVisibility(View.GONE);
                    profile_no_collapse.setVisibility(View.VISIBLE);
                }
                else{
                    profile_content_no.setVisibility(View.GONE);
                    profile_no_expand.setVisibility(View.VISIBLE);
                    profile_no_collapse.setVisibility(View.GONE);
                }
                break;
            case R.id.profile_no_ic_collapse:
                profile_content_no.setVisibility(View.GONE);
                profile_no_expand.setVisibility(View.VISIBLE);
                profile_no_collapse.setVisibility(View.GONE);
                break;
            case R.id.profile_no_ic_expand:
                profile_content_no.setVisibility(View.VISIBLE);
                profile_no_expand.setVisibility(View.GONE);
                profile_no_collapse.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

}
