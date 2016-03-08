package com.epollomes.galleryandupload;

import java.util.UUID;

/**
 * Created by erahal on 3/4/2016.
 */
public class ImageShot {

    private UUID mId;
    private String mFilePath;

    public ImageShot() {
        this(UUID.randomUUID());
    }

    public ImageShot(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
