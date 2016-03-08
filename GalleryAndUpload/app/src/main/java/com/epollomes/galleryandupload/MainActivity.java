package com.epollomes.galleryandupload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.epollomes.galleryandupload.Utils.ImageUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements
        OnStartDragListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private final static int MAX_NUMBER_OF_IMAGE = 12;
    private boolean mTwoPane;
    ItemTouchHelper touchHelper;
    private static final int REQUEST_PHOTO = 2;
    int currentPhotoIndex = 1;
    FloatingActionButton fabCamera;
    RecyclerView recyclerView;
    List<ImageShot> currentListOfImagesShot;
    FloatingActionButton fabUpload;
    boolean isUploadClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        currentListOfImagesShot = new ArrayList<>();
//        currentListOfImagesShot.add(new ImageShot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        fabCamera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraAndTakePhoto();
            }
        });


        fabUpload = (FloatingActionButton) findViewById(R.id.fab_upload);
        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentListOfImagesShot.size() > 0) {
                    isUploadClicked = true;
                    uploadImage(currentListOfImagesShot.get(0).getFilePath(), 0);
                }
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                // .setAction("Action", null).show();
            }
        });
        fabUpload.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


        //        if (findViewById(R.id.item_detail_container) != null) {
        //            // The detail container view will be present only in the
        //            // large-screen layouts (res/values-w900dp).
        //            // If this view is present, then the
        //            // activity should be in two-pane mode.
        //            mTwoPane = true;
        //    }
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        //Remove this line in order to get a linear recyclerview
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(currentListOfImagesShot, this);
        recyclerView.setAdapter(adapter);


        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }


    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

        private final List<ImageShot> mValues;
        private final OnStartDragListener mDragStartListener;

        public SimpleItemRecyclerViewAdapter(List<ImageShot> items, OnStartDragListener dragStartListener) {
            mDragStartListener = dragStartListener;
            mValues = items;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);


            DisplayMetrics displayMetrics = new DisplayMetrics();
            MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, screenWidth / 3);
            view.setLayoutParams(params);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {


            holder.mItem = mValues.get(position);
            Bitmap bitmap = ImageUtil.getScaledBitmap(holder.mItem.getFilePath(), 500, 500);

            holder.mImageView.setImageBitmap(bitmap);
            final int removePosition = position;
            holder.handleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeImageShot(removePosition);

                }
            });

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        //                    Bundle arguments = new Bundle();
                        //                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        //                        ItemDetailFragment fragment = new ItemDetailFragment();
                        //                        fragment.setArguments(arguments);
                        //                        getSupportFragmentManager().beginTransaction()
                        //                                .replace(R.id.item_detail_container, fragment)
                        //                                .commit();
                    } else {
                        //                    Context context = v.getContext();
                        //                    Intent intent = new Intent(context, ItemDetailActivity.class);
                        //                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        //
                        //                        context.startActivity(intent);
                    }
                }
            });

            //  Code for the thing at the top most right in order to drag using it
            //            holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            //                @Override
            //                public boolean onTouch(View v, MotionEvent event) {
            //                    if (MotionEventCompat.getActionMasked(event) ==
            //                            MotionEvent.ACTION_DOWN) {
            //                        mDragStartListener.onStartDrag(holder);
            //                    }
            //                    return false;
            //                }
            //            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mValues, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mValues, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            mValues.remove(position);
            notifyItemRemoved(position);
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public ImageShot mItem;
            public final ImageView handleView;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.main_image);
                handleView = (ImageView) itemView.findViewById(R.id.handle);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + "'";
            }

            @Override
            public void onItemSelected() {
                itemView.setBackgroundColor(Color.LTGRAY);
            }

            @Override
            public void onItemClear() {
                itemView.setBackgroundColor(0);
            }
            // this is a comment to test the remote screen desktop and code from mac
        }
    }

    File mPhotoFile;
    ImageShot imageShot;

    private void openCameraAndTakePhoto() {

        imageShot = new ImageShot();
        mPhotoFile = ImageLab.get(this).getPhotoFile(imageShot);
        PackageManager packageManager = getPackageManager();

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = (mPhotoFile != null && captureImage.resolveActivity(packageManager) != null);
        fabCamera.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(captureImage, REQUEST_PHOTO);
        }

    }

    private void updatePhotoView() {

        if (recyclerView == null) {
            return;
        }

        if (mPhotoFile == null || !mPhotoFile.exists()) {
            return;
        } else {
            imageShot.setFilePath(mPhotoFile.getPath());
            currentListOfImagesShot.add(imageShot);
            recyclerView.getAdapter().notifyDataSetChanged();
            currentPhotoIndex += 1;

            if (currentPhotoIndex > 1) {
                fabUpload.setEnabled(true);
                fabUpload.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO) {
            updatePhotoView();
        }
    }

    public void removeImageShot(int index) {
        currentListOfImagesShot.remove(index);
        recyclerView.removeViewAt(index);
        recyclerView.getAdapter().notifyItemRemoved(index);
        recyclerView.getAdapter().notifyItemRangeChanged(index, currentListOfImagesShot.size());
        currentPhotoIndex -= 1;
        if (currentPhotoIndex == 1) {
            fabUpload.setEnabled(false);
            fabUpload.setVisibility(View.GONE);
            isUploadClicked = false;
        }
        if (isUploadClicked && currentListOfImagesShot.size() > 0) {
            isUploadClicked = true;
            uploadImage(currentListOfImagesShot.get(0).getFilePath(), 0);
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    ProgressDialog pDialog;

    public void uploadImage(String filePath, final int indexInMainList) {
        pDialog = ProgressDialog.show(this, "Uploading Images",
                "Please Wait", true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);

        RequestParams params = new RequestParams();
        // params.put("picture[image]", new File(filePath));

        try {
            params.put("fileToUpload", new File(filePath));
        } catch (FileNotFoundException e) {
            Toast.makeText(MainActivity.this, "Error reading file to upload", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        params.setForceMultipartEntityContentType(true);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://elierahal.com/test_upload/upload_image.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(MainActivity.this, "Uploaded successfully", Toast.LENGTH_LONG).show();
                try {
                    String tempString = new String(responseBody, "UTF-8");
                    Log.e("QQQ", "Success " + tempString);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();
                removeImageShot(indexInMainList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Failed to upload", Toast.LENGTH_LONG).show();
                try {
                    if (responseBody != null) {
                        String tempString = new String(responseBody, "UTF-8");
                        Log.e("QQQ", "Failure " + tempString);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                pDialog.setMessage((int) Math.floor((((float) bytesWritten / (float) totalSize)) * 100) + "%");

            }
        });


    }

}