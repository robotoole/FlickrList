package com.robotoole.flickrlist.util;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;

import com.robotoole.flickrlist.Constants;
import com.robotoole.flickrlist.app.ImageDetailActivity;
import com.robotoole.flickrlist.model.Picture;
import com.robotoole.flickrlist.model.service.DataService;

/**
 * Helper class to consistently launch intents from any class.
 * Created by robert on 2/19/15.
 */
public class IntentLauncher {

    /**
     * Launch the FlickrXMLService with a specified url and a flag
     * for whether or not use the cached data.
     *
     * @param url
     * @param context
     * @param forceUpdate
     */
    public static void launchService(String url, Context context, boolean forceUpdate) {
        Intent intent = new Intent(context, DataService.class);
        intent.putExtra(Constants.EXTRA_API_URL, url);
        intent.putExtra(Constants.EXTRA_FORCE_UPDATE, forceUpdate);
        context.startService(intent);
    }

    /**
     * Launch the detail activity. Pass along the picture properties and
     * transition options.
     *
     * @param picture
     * @param context
     * @param options
     */
    public static void launchDetail(Picture picture, Context context,
                                    ActivityOptions options) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(Constants.EXTRA_IMAGE_URL, picture.getLink());
        intent.putExtra(Constants.EXTRA_TITLE, picture.getTitle());
        intent.putExtra(Constants.EXTRA_AUTHOR, picture.getAuthor());
        intent.putExtra(Constants.EXTRA_AUTHOR_LINK, picture.getAuthor_id());

        context.startActivity(intent, options.toBundle());
    }
}
