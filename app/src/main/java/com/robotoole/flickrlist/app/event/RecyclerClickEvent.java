package com.robotoole.flickrlist.app.event;

import android.view.View;

/**
 * Event used to alert any listeners that the recycler view item has
 * been clicked.
 * Created by robert on 2/20/15.
 */
public class RecyclerClickEvent {

    public View viewClicked;

    public RecyclerClickEvent(View viewClicked) {
        this.viewClicked = viewClicked;
    }

}
