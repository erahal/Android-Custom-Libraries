package com.epollomes.galleryandupload;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

public class ImageLab {

    private static ImageLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ImageLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new ImageLab(context);
        }
        return sCrimeLab;
    }

    private ImageLab(Context context) {
        mContext = context.getApplicationContext();
    }

    public File getPhotoFile(ImageShot imageShot) {
        File externalFilesDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }
        return new File(externalFilesDir, imageShot.getPhotoFilename());
    }

}
