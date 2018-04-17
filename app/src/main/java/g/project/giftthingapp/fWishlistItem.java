package g.project.giftthingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.CardView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;

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
    private CardView itemBox;
    private TextView itemNameView;
    private TextView itemDescriptionView;
    private ImageView itemImgView;

    private ImageView removeItem;

    //Use this function to generate a new instance of fWishlistItem
    public static fWishlistItem newInstance(WishlistItem item) {
        fWishlistItem myFragment = new fWishlistItem();

        Bundle args = new Bundle();
        args.putString(ARG_ITEM_NAME, item.getItemName());
        args.putString(ARG_DESCRIPTION, item.getItemDescription());
        args.putString(ARG_URL, item.getItemURL());
        args.putString(ARG_IMG_URL, item.getImgURL());
        args.putDouble(ARG_PRICE, item.getItemPrice());
        args.putInt(ARG_QTY_DESIRED, item.getQtyDesired());
        args.putInt(ARG_QTY_PURCHASED, item.getQtyPurchased());
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Set onClickListener for itemBox
        itemBox = this.getView().findViewById(R.id.layout_itemBox);
        itemBox.setOnClickListener(this);

        //Initialize display views
        itemNameView = this.getView().findViewById(R.id.item_name);
        itemDescriptionView = this.getView().findViewById(R.id.item_summary);
        itemImgView = this.getView().findViewById(R.id.item_img);


        //Set displays with display information
        itemNameView.setText(itemName);
        itemDescriptionView.setText(itemDescription);
        if(!imgURL.equals("")) new imgScraper().execute();

        removeItem = this.getView().findViewById(R.id.item_remove);
        removeItem.setOnClickListener(this);

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
        imgURL = getArguments().getString(ARG_IMG_URL);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist_item, container, false);
    }

    @Override
    public void onClick(View v) {

        FragmentManager fm = ((MainActivity) fWishlistItem.this
                .getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //When item is clicked, follow item url
        int i = v.getId();
        if (i == R.id.layout_itemBox) {
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(itemURL));
            startActivity(intent);

        }
    }

    private class imgScraper extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //open img src
                InputStream input = new java.net.URL(imgURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            itemImgView.setImageBitmap(bitmap);
        }
    }
}