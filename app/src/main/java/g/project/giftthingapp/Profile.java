package g.project.giftthingapp;

import java.util.ArrayList;

/**
 * Created by MarcP on 4/2/2018.
 */

public class Profile {

    private String uid;
    private String name;
    private String address;
    private String birthday;
    private String biography;
    private String pantsSize;
    private String shirtSize;
    private String favColor;
    private ArrayList<String> hobbies;
    private ArrayList<String> likes;
    private ArrayList<String> dislikes;
    private ArrayList<String> friendsList;
    private ArrayList<Wishlist> wishbook;



    public Profile() {
        hobbies = new ArrayList<>();
        likes = new ArrayList<>();
        dislikes = new ArrayList<>();
        friendsList  = new ArrayList<>();
        wishbook = new ArrayList<>();
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setPantsSize(String pantsSize) {
        this.pantsSize = pantsSize;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public void setFavColor(String favColor) {
        this.favColor = favColor;
    }

    public void setHobbies(ArrayList<String> hobbies) {
        this.hobbies = hobbies;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public void setDislikes(ArrayList<String> dislikes) {
        this.dislikes = dislikes;
    }

    public void setFriendsList(ArrayList<String> friendsList) {
        this.friendsList = friendsList;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBiography() {
        return biography;
    }

    public String getPantsSize() {
        return pantsSize;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public String getFavColor() {
        return favColor;
    }

    public ArrayList<String> getFriendsList() {
        return friendsList;
    }

    public ArrayList<Wishlist> getWishbook() {
        return wishbook;
    }

    public ArrayList<String> getHobbies() {
        return hobbies;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public ArrayList<String> getDislikes() {
        return dislikes;
    }

    public String getHobbiesAsString() {

        String temp = "";
        for(int i = 0; i<hobbies.size(); i++)
            temp = temp + hobbies.get(i) + "\n";

        return temp;
    }

    public String getLikesAsString() {

        String temp = "";
        for(int i = 0; i<likes.size(); i++)
            temp = temp + likes.get(i) + "\n";

        return temp;
    }

    public String getDislikesAsString() {

        String temp = "";
        for(int i = 0; i<dislikes.size(); i++)
            temp = temp + dislikes.get(i) + "\n";

        return temp;
    }
    public int getNumberOfFriends()
    {
        return friendsList.size();
    }

    public void addHobby(String hobby){
        hobbies.add(hobby);
    }

    public void addLikes(String like){
        likes.add(like);
    }

    public void addDislike(String dislike){
        dislikes.add(dislike);
    }

    public void addFriend(String fID){friendsList.add(fID);}

    public void addWishlist(Wishlist wishlist){
        wishbook.add(wishlist);}
}
