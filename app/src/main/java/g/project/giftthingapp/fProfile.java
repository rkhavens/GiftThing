package g.project.giftthingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fProfile extends Fragment implements View.OnClickListener {

    FloatingActionButton fab_edit;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        fab_edit = (FloatingActionButton) this.getView().findViewById(R.id.profile_edit_fab);
        fab_edit.setOnClickListener(this);

        FrameLayout frag_frame = (FrameLayout) this.getView().findViewById(R.id.wishlists_place);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.wishlists_place, new fWishlistList());
        ft.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_profile, container, false);
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
