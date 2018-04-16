package g.project.giftthingapp;

import java.util.ArrayList;

/**
 * Created by MarcP on 4/4/2018.
 */

public class Wishlist {

    private String listName;
    private String listDescription;
    private String privacyLevel;
    private String dateCreated;
    private ArrayList<WishlistItem> itemList;

    public Wishlist()
    {
        itemList = new ArrayList<>();
    }

    public String getListName() {
        return listName;
    }

    public String getListDescription() {
        return listDescription;
    }

    public String getPrivacyLevel() {
        return privacyLevel;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public ArrayList<WishlistItem> getItemList() {
        return itemList;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setListDescription(String listDescription) {
        this.listDescription = listDescription;
    }

    public void setPrivacyLevel(String privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setItemList(ArrayList<WishlistItem> itemList) {
        this.itemList = itemList;
    }

    public void addWishlistItem(WishlistItem item)
    {
        this.itemList.add(item);
    }
}
