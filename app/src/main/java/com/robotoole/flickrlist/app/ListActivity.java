package com.robotoole.flickrlist.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.robotoole.flickrlist.Constants;
import com.robotoole.flickrlist.R;
import com.robotoole.flickrlist.util.IntentLauncher;

/**
 * Default activity of the app. Loads the list view
 * and handles the action overflow items.
 */
public class ListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    /**
     * Switch cities based on the action item selected.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String city = "";
        switch (item.getItemId()) {
            case R.id.action_boston:
                city = "Boston";
                break;
            case R.id.action_paris:
                city = "Paris";
                break;
            case R.id.action_nyc:
                city = "NYC";
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        IntentLauncher.launchService(Constants.FLICKR_API_URL + city, this, true);
        return !TextUtils.isEmpty(city);
    }
}
