package com.epollomes.recyclerviewwithsearch.Content;

public class ItemModel {

    int mAlbumID;
    int mID;
    String mTitle;
    String mImageURL;
    String mThumbnailURL;

    public ItemModel(int albumID, int ID, String title, String imageURL, String thumbnailURL) {
        mAlbumID = albumID;
        mID = ID;
        mTitle = title;
        mImageURL = imageURL;
        mThumbnailURL = thumbnailURL;
    }

    public ItemModel() {
        mAlbumID = 1;
        mID = 1;
        mTitle = "";
        mImageURL = "";
        mThumbnailURL = "";
    }

    public int getAlbumID() {
        return mAlbumID;
    }

    public void setAlbumID(int albumID) {
        mAlbumID = albumID;
    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        mThumbnailURL = thumbnailURL;
    }

}
