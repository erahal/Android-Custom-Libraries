package com.epollomes.galleryandupload;

/**
 * Created by erahal on 1/19/2016.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
