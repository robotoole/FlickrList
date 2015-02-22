package com.robotoole.flickrlist.model.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.robotoole.flickrlist.Constants;
import com.robotoole.flickrlist.R;
import com.robotoole.flickrlist.app.event.EventManager;
import com.robotoole.flickrlist.app.event.ServiceEvent;
import com.robotoole.flickrlist.model.Picture;
import com.robotoole.flickrlist.model.db.DatabaseHelper;
import com.robotoole.flickrlist.util.NetworkUtil;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * IntentService used to load data from either the web or locally via sqlite.
 * Created by robert on 2/19/15.
 */
public class DataService extends IntentService {
    static final String TAG = DataService.class.getSimpleName();

    List<Picture> pictures;
    DatabaseHelper dbHelper;

    public DataService() {
        super("DataService");
    }

    /**
     * Handle the intent with the parameters for the api url
     * as well as whether or not to use the local cache. If
     * we are not using the cache, we clear the database and
     * start fresh. If we do, we try to get the data from
     * there instead of calling the api. We notify the UI
     * of the service completion by leveraging EventBus's
     * ability to easily post messages back to our main UI
     * thread.
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        dbHelper = new DatabaseHelper(getApplicationContext());

        boolean forceUpdate = intent.getBooleanExtra(Constants.EXTRA_FORCE_UPDATE, false);
        String url = intent.getExtras().getString(Constants.EXTRA_API_URL);

        boolean isOnline = NetworkUtil.isOnline(this);

        //try to get pictures from the web, only if we aren't forcing
        //an update of the data.
        if (forceUpdate && isOnline) {
            //remove all pictures
            dbHelper.clearPictures();
        } else {
            //try to get pictures from the db.
            pictures = dbHelper.getAllPictures();
            //if we tried to force update but didn't have internet
            if (forceUpdate && !isOnline) {
                postFailEvent(getString(R.string.error_no_internet_force_update));
            }
        }

        if (pictures != null && pictures.size() > 0) {
            Log.d(TAG, "Found pictures, not getting data!");
            //we're done here.
            postSuccessEvent();
            return;
        } else {
            Log.d(TAG, "No pictures found, GETTING data!");
            pictures = new ArrayList();
        }

        //if we got this far, we have an empty db so we have
        //to go to the server.
        if (isOnline) {
            getPicturesFromWeb(url);
        } else {
            postFailEvent(getString(R.string.error_no_connection));
        }

        //notify the UI of the successful or failure service result.
        if (pictures.size() > 0) {
            postSuccessEvent();
        } else {
            postFailEvent(getString(R.string.error_no_pictures_found));
        }
    }

    /**
     * Get the images from the Flickr Api. Retrieve the XML document
     * from the api, and loop the "entry" nodes. Take each entry node
     * to serialize via the model object and add it to the
     * List<Picture>.
     *
     * @param apiUrl
     */
    public void getPicturesFromWeb(String apiUrl) {
        try {
            Log.d(TAG, "loading url: " + apiUrl);
            URL url = new URL(apiUrl);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            //get all the entries
            NodeList nodeList = doc.getElementsByTagName("entry");

            //parse them to picture objects
            for (int i = 0; i < nodeList.getLength(); i++) {
                pictures.add(new Picture().copyFrom(nodeList.item(i)));
            }

            if (pictures.size() > 0) {
                //save them to the local db
                dbHelper.saveCache(pictures);
            }
        } catch (Exception e) {
            Log.e(TAG, "XML Parsing Excpetion = " + e);
        }
    }

    /**
     * Post a success event back up to the UI layer.
     */
    public void postSuccessEvent() {
        EventManager.post(new ServiceEvent(ServiceEvent
                .ServiceEventType.SUCCESS, pictures));
    }

    /**
     * Post a fail message back up to the UI layer.
     *
     * @param message
     */
    public void postFailEvent(String message) {
        EventManager.post(new ServiceEvent(ServiceEvent
                .ServiceEventType.FAULT, message));
    }
}
