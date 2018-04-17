package g.project.giftthingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class fWishlist extends Fragment implements View.OnClickListener{

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
    private CardView addItemCard;
    private LinearLayout itemCreationForm;
    protected TextView addLinkText;
    private Button addLinkButton;

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
        addItemCard = this.getView().findViewById(R.id.add_item_card);
        itemCreationForm = this.getView().findViewById(R.id.item_creation_form);
        addLinkText = this.getView().findViewById(R.id.add_link_text);
        addLinkButton = this.getView().findViewById(R.id.add_link_button);

        //set on click listeners
        addItemCard.setOnClickListener(this);
        addLinkButton.setOnClickListener(this);

        //Connect to current wishlist in Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profile/User/" + uID + "/wishbook/" + Integer.toString(index));

        //Will update view every time current user's friends list is updated in
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Where");
                currentWishlist = dataSnapshot.getValue(Wishlist.class);
                itemLayout.removeAllViews();
                System.out.println("do");
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
        System.out.println("you");
        for(int i = 0; i < currentWishlist.getItemList().size(); i++) {
            System.out.println("break");
            FragmentManager fm = ((MainActivity) fWishlist.this
                    .getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            WishlistItem item = currentWishlist.getItemList().get(i);

            fWishlistItem fragment = fWishlistItem.newInstance(item);
            ft.add(R.id.item_layout, fragment);

            ft.commit();
        }
    }

    public void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public void onClick(View v) {


        //When item is clicked, load profile fragment with friend information
        int i = v.getId();
        if (i == R.id.add_link_button) {
            if(addLinkText.getText().toString().replace(" ", "").equals("")){
                addLinkText.setError("Required.");
            }
            else if(!(addLinkText.getText().toString().contains("www.amazon.com"))){
                addLinkText.setError("Amazon links only.");
            }
            else new WebScraper().execute();
        }
    }

    private class WebScraper extends AsyncTask<Void, Void, Void> {

        private String title;
        private String name = "";
        private String description = "";
        private double price = 0;
        private String url = "https://www.amazon.com/Players-Handbook-Dungeons-Dragons-Wizards/dp/0786965606/ref=sr_1_2?ie=UTF8&qid=1523496117&sr=8-2&keywords=dnd";
        private String imgUrl = "";

        public String cleanString(String str){
            System.out.print(str);
            Document doc = Jsoup.parse(str);
            str = doc.text();
            System.out.print(str);
            return str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Android Basic JSoup Tutorial");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();*/
            url = addLinkText.getText().toString();
            addLinkText.setText("");
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                // Connect to the web site
                Document document = Jsoup.connect(url).get();

                // Get the html document title
                title = document.title();

                //Get product title from html
                Elements nameElementChildren = document.getElementsByTag("span");

                for (Element e : nameElementChildren) {
                    String key = "";
                    if (e.hasAttr("id"))
                        key = e.attr("id").toUpperCase();
                    System.out.println(key);
                    if (key.contains("PRODUCTTITLE")) {
                        name = e.html();
                        System.out.println("Name: "+name);
                        break;
                    }
                }

                //Get description from html for books/ebooks
                if(!(document.getElementById("bookDescription_feature_div") == null)) {
                    Element descElement = document.getElementById("bookDescription_feature_div");
                    Elements descElementChildren = descElement.getElementsByTag("noscript");
                    for (Element e : descElementChildren) {
                        description = e.html();
                    }
                }


                //Get description from html for other items
                if(!(document.getElementById("featurebullets_feature_div") == null)) {
                    Element descElement = document.getElementById("featurebullets_feature_div");
                    Elements descElementChildren = descElement.getElementsByTag("li");
                    for (Element e : descElementChildren) {
                        if(!(e.hasAttr("id"))) {
                            Document doc = Jsoup.parse(e.html());
                            description = description + "\n" + doc;
                        }
                    }
                }


                //Get price from html
                Element priceElement = document.getElementById("buybox");
                Elements priceElementChildren = priceElement.getElementsByTag("span");

                for (Element e : priceElementChildren) {
                    String key = "";
                    if(e.hasAttr("class"))
                        key = e.attr("class");
                    System.out.println(key);
                    if ("a-size-medium a-color-price offer-price a-text-normal".equals(key)) {
                        price = Double.parseDouble(e.html().replace("$",""));
                    }
                }


                Element imgContainerDiv = document.getElementById("landingImage");
                if(imgContainerDiv == null) imgContainerDiv = document.getElementById("imgBlkFront");
                if(imgContainerDiv == null) imgContainerDiv = document.getElementById("ebooksImgBlkFront");

                if(!(imgContainerDiv == null)) {
                    String urls = imgContainerDiv.attr("data-a-dynamic-image");
                    int index = urls.indexOf("\"", 3);
                    urls = urls.substring(2, index);
                    imgUrl = urls;
                    System.out.println(urls);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            WishlistItem item = new WishlistItem();
            item.setItemURL(url);
            item.setItemName(cleanString(name));
            item.setItemDescription(cleanString(description));
            item.setItemPrice(price);
            item.setQtyDesired(1);
            item.setQtyPurchased(0);
            item.setImgURL(imgUrl);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef;
            //if (currentWishlist.getItemList() == null) myRef = database.getReference("Profile/User/" + uID + "/wishbook/" + Integer.toString(index) + "/itemList/0");
            myRef = database.getReference("Profile/User/" + uID + "/wishbook/" + Integer.toString(index) + "/itemList/" + Integer.toString(currentWishlist.getItemList().size()));

            myRef.setValue(item);

            reloadFragment();
        }
    }



}
