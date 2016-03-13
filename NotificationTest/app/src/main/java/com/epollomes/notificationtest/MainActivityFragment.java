package com.epollomes.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    Button btnSimpleNotification;
    Button btnClickableNotification;
    Button btnNotificationColoredWithButton;
    Button btnExpandableNotifBigTextStyle;
    Button btnExpandableNotifBigPictureStyle;
    Button btnInboxStyleNotification;
    Button btnClearAllNotification;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        btnSimpleNotification = (Button) v.findViewById(R.id.simple_notifiction_button);
        btnSimpleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySimpleNotification();
            }
        });

        btnClickableNotification = (Button) v.findViewById(R.id.simple_notifiction_button2);
        btnClickableNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayClickableNotification();
            }
        });

        btnNotificationColoredWithButton = (Button) v.findViewById(R.id.simple_notifiction_button3);
        btnNotificationColoredWithButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotificationWithColorAndIcon();
            }
        });

        btnExpandableNotifBigTextStyle = (Button) v.findViewById(R.id.simple_notifiction_button4);
        btnExpandableNotifBigTextStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayExpandableNotifBigTextStyle();
            }
        });

        btnExpandableNotifBigPictureStyle = (Button) v.findViewById(R.id.simple_notifiction_button5);
        btnExpandableNotifBigPictureStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayExpandableNotifBigPictureStyle();
            }
        });

        btnInboxStyleNotification = (Button) v.findViewById(R.id.simple_notifiction_button6);
        btnInboxStyleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInboxStyleNOtification();
            }
        });

        btnClearAllNotification = (Button) v.findViewById(R.id.simple_notifiction_button7);
        btnClearAllNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllNotification();
            }
        });

        return v;
    }


    public void displaySimpleNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setSmallIcon(R.drawable.ic_sms_24dp).setContentTitle(getContext().getResources().getString(R.string
                .notification_title))
                .setContentText(getContext().getResources().getString(R.string.notification_content));
        Notification notification = builder.build();
        NotificationManagerCompat.from(getContext()).notify(1, notification);
    }

    public void displayClickableNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setSmallIcon(R.drawable.ic_sms_24dp).setContentTitle(getContext().getResources().getString(R.string
                .notification_title))
                .setContentText(getContext().getResources().getString(R.string.clickable));

        int requestCode = 252525;
        PendingIntent intent = PendingIntent.getActivity(getContext(), requestCode, new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
        builder.setContentIntent(intent);
        Notification notification = builder.build();
        NotificationManagerCompat.from(getContext()).notify(2, notification);
    }


    public void displayNotificationWithColorAndIcon() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setSmallIcon(R.drawable.ic_sms_24dp).setContentTitle(getContext().getResources().getString(R.string
                .notification_title))
                .setContentText(getContext().getResources().getString(R.string.colored_with_icon));

        // The color is for the small icon on the bottom of the notification
        int color = ContextCompat.getColor(getContext(), R.color.colorAccent);
        builder.setColor(color);

        //creating bitmap for large icon
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.logo512);
        builder.setLargeIcon(bitmap);

        Notification notification = builder.build();
        NotificationManagerCompat.from(getContext()).notify(3, notification);
    }

    private void displayExpandableNotifBigTextStyle() {

        // Create the style object with BigPictureStyle subclass.
        NotificationCompat.BigTextStyle notiStyle = new
                NotificationCompat.BigTextStyle();
        notiStyle.setBigContentTitle("Expandable Notification");
        notiStyle.setSummaryText("Single line text under seperator");
        notiStyle.bigText(getContext().getResources().getString(R.string.notification_content));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setSmallIcon(R.drawable.ic_sms_24dp).setContentTitle(getContext().getResources().getString(R.string
                .notification_title))
                .setContentText(getContext().getResources().getString(R.string.colored_with_icon))
                .setStyle(notiStyle);

        // The color is for the small icon on the bottom of the notification
        int color = ContextCompat.getColor(getContext(), R.color.colorAccent);
        builder.setColor(color);

        //creating bitmap for large icon
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.logo512);
        builder.setLargeIcon(bitmap);

        Notification notification = builder.build();
        NotificationManagerCompat.from(getContext()).notify(4, notification);
    }


    private void displayExpandableNotifBigPictureStyle() {

        // Create the style object with BigPictureStyle subclass.
        NotificationCompat.BigPictureStyle notiStyle = new
                NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle("Picture Style Notification");
        notiStyle.setSummaryText("Single line text before image");
        notiStyle.bigLargeIcon(BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.logo512));
        notiStyle.bigPicture(BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.logo512));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setSmallIcon(R.drawable.ic_sms_24dp).setContentTitle("Picture Style Notification")
                .setContentText(getContext().getResources().getString(R.string.colored_with_icon))
                .setStyle(notiStyle);

        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.logo512);
        // save bitmap to cache directory
        try {

            File cachePath = new File(getContext().getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(getContext().getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(getContext(), "com.epollomes.notificationtest.fileprovider", newFile);

        if (contentUri != null) {

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContext().getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
//            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
            int requestCode = 252525;
            PendingIntent intent = PendingIntent.getActivity(getContext(), requestCode, shareIntent, 0);
            builder.addAction(R.drawable.ic_share_24dp, "Share", intent);

        }


        //action button for the picture style
        // The color is for the small icon on the bottom of the notification
        int color = ContextCompat.getColor(getContext(), R.color.colorAccent);
        builder.setColor(color);

        //creating bitmap for large icon
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.logo512);
        builder.setLargeIcon(bitmap);
        Notification notification = builder.build();
        NotificationManagerCompat.from(getContext()).notify(5, notification);
    }

    private void displayInboxStyleNOtification() {

        // Create the style object with BigPictureStyle subclass.
        NotificationCompat.InboxStyle notiStyle = new
                NotificationCompat.InboxStyle();
        notiStyle.setBigContentTitle("Inbox Style Notification");
        notiStyle.setSummaryText("Single line text under seperator");
        notiStyle.addLine("This is line 1");
        notiStyle.addLine("This is line 2");
        notiStyle.addLine("This is line 3");
        notiStyle.addLine("This is line 4");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setSmallIcon(R.drawable.ic_sms_24dp).setContentTitle("Inbox Style Notification")
                .setContentText(getContext().getResources().getString(R.string.colored_with_icon))
                .setStyle(notiStyle);

        // The color is for the small icon on the bottom of the notification
        int color = ContextCompat.getColor(getContext(), R.color.colorAccent);
        builder.setColor(color);

        //creating bitmap for large icon
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.logo512);
        builder.setLargeIcon(bitmap);

        Notification notification = builder.build();
        NotificationManagerCompat.from(getContext()).notify(6, notification);
    }

    private void clearAllNotification() {
        //Clearing by ID
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context
                .NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        notificationManager.cancel(2);
        notificationManager.cancel(3);
        notificationManager.cancelAll();
    }
}
