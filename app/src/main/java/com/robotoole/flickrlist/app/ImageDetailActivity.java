package com.robotoole.flickrlist.app;

import android.app.Activity;
import android.os.Bundle;

import com.robotoole.flickrlist.R;

/**
 * Default activity of the app. Initially loads two
 * fragments(List and Header).
 */
public class ImageDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // tried to make this animation smooth but failed miserably
        // when returning to the list view.
        getActionBar().hide();
        setContentView(R.layout.activity_detail);
    }
}