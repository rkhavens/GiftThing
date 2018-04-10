package g.project.giftthingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by MarcP on 4/9/2018.
 */

public class fWishlistItem extends Fragment implements View.OnClickListener {


    //Param Argument Names
    private static final String ARG_ITEM_NAME = "item-name";
    private static final String ARG_DESCRIPTION = "item-description";
    private static final String ARG_URL = "item-url";
    private static final String ARG_IMG_URL = "item-img-url";
    private static final String ARG_PRICE = "item-price";
    private static final String ARG_QTY_DESIRED = "qty-desired";
    private static final String ARG_QTY_PURCHASED = "qty-purchased";

    //Params
    private String itemName;
    private String itemDescription;
    private String itemURL;
    private String imgURL;
    private double itemPrice;
    private int qtyDesired;
    private int qtyPurchased;

    //Views
    private LinearLayout itemBox;
    private TextView itemNameView;
    private TextView itemDescriptionView;

    //Use this function to generate a new instance of fWishlistItem
    public static fWishlistItem newInstance(WishlistItem item) {
        fWishlistItem myFragment = new fWishlistItem();

        Bundle args = new Bundle();
        args.putString(ARG_ITEM_NAME, item.getItemName());
        args.putString(ARG_DESCRIPTION, item.getItemDescription());
        args.putString(ARG_URL, item.getItemURL());
        //args.putString(ARG_IMG_URL, item.getImgURL());
        args.putDouble(ARG_PRICE, item.getItemPrice());
        args.putInt(ARG_QTY_DESIRED, item.getQtyDesired());
        args.putInt(ARG_QTY_PURCHASED, item.getQtyPurchased());
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //Set onClickListener for itemBox
        itemBox = this.getView().findViewById(R.id.layout_itemBox);
        itemBox.setOnClickListener(this);

        //Initialize display views
        itemNameView = this.getView().findViewById(R.id.item_name);
        itemDescriptionView = this.getView().findViewById(R.id.item_summary);

        //Set displays with display information
        itemNameView.setText(itemName);
        itemDescriptionView.setText(itemDescription);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initialize Params
        itemName = getArguments().getString(ARG_ITEM_NAME);
        itemDescription = getArguments().getString(ARG_DESCRIPTION);
        itemURL = getArguments().getString(ARG_URL);
        itemPrice = getArguments().getDouble(ARG_PRICE);
        qtyDesired = getArguments().getInt(ARG_QTY_DESIRED);
        qtyPurchased = getArguments().getInt(ARG_QTY_PURCHASED);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist_item, container, false);
    }

    @Override
    public void onClick(View v) {
        /*
        FragmentManager fm = ((MainActivity) fWishlistItem.this
                .getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //When item is clicked, load profile fragment with friend information
        int i = v.getId();
        if (i == R.id.layout_itemBox) {
            ft.replace(R.id.fragment_place, fProfile.newInstance(getArguments().getString("uID")));
            ft.commit();

        }*/
    }
}
