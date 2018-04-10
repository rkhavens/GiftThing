package g.project.giftthingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by MarcP on 4/9/2018.
 */

public class fWishbookItem extends Fragment implements View.OnClickListener {

    //Param Argument Names
    private static final String ARG_ITEM_NAME = "list-name";
    private static final String ARG_DESCRIPTION = "list-description";
    private static final String ARG_PRIVACY_LEVEL = "list-privacy-level";
    private static final String ARG_DATE_CREATED = "list-date-created";
    private static final String ARG_INDEX = "list-index";
    private static final String ARG_UID = "list-uid";


    //Params
    private String listName;
    private String listDescription;
    private String privacyLevel;
    private String dateCreated;
    private int index;
    private String uID;

    //Views
    private LinearLayout itemBox;
    private TextView listNameView;
    private TextView listDescriptionView;

    //Use this function to generate a new instance of fWishlistItem
    public static fWishbookItem newInstance(Wishlist list,String uID, int index) {
        fWishbookItem myFragment = new fWishbookItem();

        Bundle args = new Bundle();
        args.putString(ARG_ITEM_NAME, list.getListName());
        args.putString(ARG_DESCRIPTION, list.getListDescription());
        //args.putString(ARG_PRIVACY_LEVEL, list.getPrivacyLevel());
        args.putString(ARG_DATE_CREATED, list.getDateCreated());
        args.putInt(ARG_INDEX, index);
        args.putString(ARG_UID, uID);

        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //Set onClickListener for itemBox
        itemBox = this.getView().findViewById(R.id.layout_itemBox);
        itemBox.setOnClickListener(this);

        //Initialize display views
        listNameView = this.getView().findViewById(R.id.wishbookitem_content_name);
        listDescriptionView = this.getView().findViewById(R.id.wishbookitem_content_description);

        //Set displays with display information
        listNameView.setText(listName);
        listDescriptionView.setText(listDescription);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initialize Params
        listName = getArguments().getString(ARG_ITEM_NAME);
        listDescription = getArguments().getString(ARG_DESCRIPTION);
        //privacyLevel = getArguments().getString(ARG_PRIVACY_LEVEL);
        //dateCreated = getArguments().getString(ARG_DATE_CREATED);
        index = getArguments().getInt(ARG_INDEX);
        uID = getArguments().getString(ARG_UID);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishbook_item, container, false);
    }

    @Override
    public void onClick(View v) {

        FragmentManager fm = ((MainActivity) fWishbookItem.this
                .getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        int i = v.getId();
        if (i == R.id.layout_itemBox) {
            ft.replace(R.id.fragment_place, fWishlist.newInstance(uID, index));
            ft.commit();

        }
    }
}
