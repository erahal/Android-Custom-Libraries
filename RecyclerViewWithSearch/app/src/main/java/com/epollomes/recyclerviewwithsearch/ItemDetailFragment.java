package com.epollomes.recyclerviewwithsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.epollomes.recyclerviewwithsearch.Content.ItemModel;
import com.epollomes.recyclerviewwithsearch.app.AppController;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ItemModel mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = new ItemModel();
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem.setImageURL(getArguments().getString(ARG_ITEM_ID));
        }
    }

    private NetworkImageView mImageViewDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            mImageViewDetails = ((NetworkImageView) rootView.findViewById(R.id.item_detail));
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            // If you are using NetworkImageView
            mImageViewDetails.setImageUrl(mItem.getImageURL(), imageLoader);
        }

        return rootView;
    }
}
