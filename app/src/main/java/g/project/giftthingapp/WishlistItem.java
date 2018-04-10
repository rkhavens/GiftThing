package g.project.giftthingapp;

/**
 * Created by MarcP on 4/4/2018.
 */

public class WishlistItem {

    private String itemName;
    private String itemDescription;
    private String itemURL;
    private String imgURL;
    private double itemPrice;
    private int qtyDesired;
    private int qtyPurchased;
    private int rank;

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemURL() {
        return itemURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public int getQtyDesired() {
        return qtyDesired;
    }

    public int getQtyPurchased() {
        return qtyPurchased;
    }

    public int getRank() {
        return rank;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setQtyDesired(int qtyDesired) {
        this.qtyDesired = qtyDesired;
    }

    public void setQtyPurchased(int qtyPurchased) {
        this.qtyPurchased = qtyPurchased;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
