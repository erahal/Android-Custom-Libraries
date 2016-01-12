package com.epollomes.recyclerviewwithsearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.epollomes.recyclerviewwithsearch.Content.ItemModel;
import com.epollomes.recyclerviewwithsearch.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

//http://jsonplaceholder.typicode.com/photos

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */


/**
 * Implementing the search view is under the following link
 * http://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview
 */


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    ImageLoader mImageLoader;
    View mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.recycler_view_title);
        setSupportActionBar(toolbar);

        mListOfItems = new ArrayList<>();
        mRecyclerView = findViewById(R.id.item_list);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);

        mImageLoader = AppController.getInstance().getImageLoader();
        fetchJSONDataAnFillInList();

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<ItemModel> filteredModelList = filter(completeCopyOfListOfItems, newText);
        mRecyclerViewContentAdapter.animateTo(filteredModelList);
        ((RecyclerView) mRecyclerView).scrollToPosition(0);
        return true;
    }

    //Below code is for setting upf the recycler view
    RecyclerViewContentAdapter mRecyclerViewContentAdapter;

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerViewContentAdapter = new RecyclerViewContentAdapter(mListOfItems);
        recyclerView.setAdapter(mRecyclerViewContentAdapter);
    }

    List<ItemModel> mListOfItems;
    List<ItemModel> completeCopyOfListOfItems;  //For filtering use this complete list not the above,
    //because the original list is referenced inside the adapter

    private void fetchJSONDataAnFillInList() {
        String tag_json_arry = "json_array_req";
        //String url = "http://jsonplaceholder.typicode.com/photos";
        String url = "https://raw.githubusercontent.com/erahal/Code-Snippets/master/photos.JSON";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                mListOfItems.add(new ItemModel(
                                        response.getJSONObject(i).getInt("albumId"),
                                        response.getJSONObject(i).getInt("id"),
                                        response.getJSONObject(i).getString("title"),
                                        response.getJSONObject(i).getString("url"),
                                        response.getJSONObject(i).getString("thumbnailUrl")
                                ));
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, "Error Paring JSON File", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                        cloneTheList();
                        mRecyclerViewContentAdapter.notifyDataSetChanged();
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
                fetchJSONDataAnFillInList();
                pDialog.hide();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    private void cloneTheList() {
        if (completeCopyOfListOfItems != null) {
            completeCopyOfListOfItems.clear();
        } else {
            completeCopyOfListOfItems = new ArrayList<>();
        }
        for (ItemModel pItemModel : mListOfItems) {
            completeCopyOfListOfItems.add(new ItemModel(
                    pItemModel.getAlbumID(),
                    pItemModel.getID(),
                    pItemModel.getTitle(),
                    pItemModel.getImageURL(),
                    pItemModel.getThumbnailURL()
            ));
        }
    }

    public class RecyclerViewContentAdapter extends RecyclerView.Adapter<RecyclerViewContentAdapter.ItemViewHolder> {

        List<ItemModel> listOfAdaptersItems;

        public RecyclerViewContentAdapter(List<ItemModel> items) {
            listOfAdaptersItems = items;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list_content, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, int position) {
            holder.mItemModel = listOfAdaptersItems.get(position);
            holder.bindGalleryItem();

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItemModel.getImageURL());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItemModel.getImageURL());
                        context.startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });

            YoYo.with(Techniques.ZoomIn)
                    .duration(400)
                    .playOn(holder.mView);
        }

        @Override
        public int getItemCount() {
            return listOfAdaptersItems.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            private View mView;
            private ItemModel mItemModel;
            private NetworkImageView imageViewThumbnails;
            private TextView textViewTitle;
            // private TextView textViewAlbumId;
            // private TextView textViewId;

            public ItemViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                imageViewThumbnails = (NetworkImageView) itemView.findViewById(R.id.image_view_thumbnails);
                textViewTitle = (TextView) itemView.findViewById(R.id.text_view_title);
            }

            public void bindGalleryItem() {
                textViewTitle.setText(mItemModel.getTitle());
                // textViewId.setText(mItemModel.getID());
                // textViewAlbumId.setText(mItemModel.getAlbumID());
                imageViewThumbnails.setContentDescription(mItemModel.getThumbnailURL());
                // If you are using NetworkImageView
                imageViewThumbnails.setImageUrl(mItemModel.getThumbnailURL(), mImageLoader);
            }

        }

        /***
         * Methods for the animation of the views
         */

        public void animateTo(List<ItemModel> models) {
            applyAndAnimateRemovals(models);
            applyAndAnimateAdditions(models);
            applyAndAnimateMovedItems(models);
        }

        private void applyAndAnimateRemovals(List<ItemModel> newModels) {
            for (int i = listOfAdaptersItems.size() - 1; i >= 0; i--) {
                final ItemModel model = listOfAdaptersItems.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAdditions(List<ItemModel> newModels) {
            for (int i = 0, count = newModels.size(); i < count; i++) {
                final ItemModel model = newModels.get(i);
                if (!listOfAdaptersItems.contains(model)) {
                    addItem(i, model);
                }
            }
        }

        private void applyAndAnimateMovedItems(List<ItemModel> newModels) {
            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
                final ItemModel model = newModels.get(toPosition);
                final int fromPosition = listOfAdaptersItems.indexOf(model);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }

        public ItemModel removeItem(int position) {
            final ItemModel model = listOfAdaptersItems.remove(position);
            notifyItemRemoved(position);
            return model;
        }

        public void addItem(int position, ItemModel model) {
            listOfAdaptersItems.add(position, model);
            notifyItemInserted(position);
        }

        public void moveItem(int fromPosition, int toPosition) {
            final ItemModel model = listOfAdaptersItems.remove(fromPosition);
            listOfAdaptersItems.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }

    }


    private List<ItemModel> filter(List<ItemModel> models, String query) {
        query = query.toLowerCase();
        final List<ItemModel> filteredModelList = new ArrayList<>();
        for (ItemModel model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("EEEE", "Destroyed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("EEEE", "Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("EEEE", "Resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("EEEE", "Stop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("EEEE", "Start");
    }
}
