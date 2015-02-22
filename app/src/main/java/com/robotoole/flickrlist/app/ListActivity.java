package com.robotoole.flickrlist.app;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.robotoole.flickrlist.Constants;
import com.robotoole.flickrlist.R;
import com.robotoole.flickrlist.app.event.EventManager;
import com.robotoole.flickrlist.app.event.ProgressEvent;
import com.robotoole.flickrlist.app.event.SearchEvent;
import com.robotoole.flickrlist.app.event.SearchEvent.SearchEventType;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Default activity of the app. Loads the header, list, and search
 * button and handles the action overflow items. Also handles
 * animating in and out the views based on state.
 * Added indeterminate progress bar to show when we are getting data.
 */
public class ListActivity extends Activity {

    /**
     * Progress bar reference so we can show/hide
     */
    private ProgressBar mProgressBar;

    /**
     * FAB search button that we show/hide
     */
    @InjectView(R.id.activity_fab_search)
    ImageButton mFabButton;

    /**
     * View states of the activity.
     */
    public enum ViewStates {
        SEARCH, NO_SEARCH
    }

    ViewStates state;

    /**
     * Set the content view and initialize butterknife.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.inject(this);
    }

    /**
     * Show the 3 city options in the action overflow menu.
     *
     * @param menu
     * @return
     */
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
                city = getString(R.string.boston);
                break;
            case R.id.action_paris:
                city = getString(R.string.paris);
                break;
            case R.id.action_nyc:
                city = getString(R.string.nyc);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        manageProgressVisibility(true);
        EventManager.post(new SearchEvent(SearchEventType.HIDE));
        EventManager.post(new SearchEvent(SearchEventType.SEARCH, city, true));
        return !TextUtils.isEmpty(city);
    }

    /**
     * Register as an event listener.
     */
    @Override
    public void onResume() {
        super.onResume();

        EventManager.register(this);
    }

    /**
     * Unregister as an event listener.
     */
    @Override
    public void onPause() {
        super.onPause();

        EventManager.unregister(this);
    }

    /**
     * Listen for a progress event to show or hide the progress bar.
     *
     * @param event ProgressEvent
     */
    public void onEvent(ProgressEvent event) {
        switch (event.type) {
            case SHOW:
                manageProgressVisibility(true);
                break;
            case HIDE:
                manageProgressVisibility(false);
                break;
            default:
                manageProgressVisibility(false);
        }
    }

    /**
     * Listen for {#link SearchEvent} to show or hide
     * the header fragment, adjust the recycler view,
     * and show/hide teh fab button.
     *
     * @param event
     */
    public void onEvent(SearchEvent event) {
        switch (event.type) {
            case SHOW:
                manageSearchBarVisibility(true);
                manageFabVisibility(false);
                state = ViewStates.SEARCH;
                break;
            case HIDE:
                manageSearchBarVisibility(false);
                manageFabVisibility(true);
                state = ViewStates.NO_SEARCH;
                break;
            default:
                //do nothing.
                //SEARCH cases are handled in the ListFragment
        }
    }

    /**
     * Handle the click of the search fab.
     *
     * @param view
     */
    @OnClick(R.id.activity_fab_search)
    public void onClick(View view) {
        Log.d("test", "clicked");
        EventManager.post(new SearchEvent(SearchEventType.SHOW));
    }

    /**
     * Helper method to show/hide the progress.
     *
     * @param isVisible
     */
    public void manageProgressVisibility(boolean isVisible) {
        mProgressBar.setVisibility((isVisible)
                ? View.VISIBLE : View.GONE);
    }

    /**
     * Show or hide the search bar as well as move the
     * recycler view into the appropriate position.
     *
     * @param isVisible
     */
    private void manageSearchBarVisibility(boolean isVisible) {
        if (state != ViewStates.SEARCH && !isVisible) {
            //don't run the hide animation
            return;
        }

        View headerTarget = findViewById(R.id.activity_fragment_header);
        View listTarget = findViewById(R.id.activity_fragment_flickr_list);

        AnimatorSet listAnimator = getAnimator(listTarget);
        AnimatorSet headerAnimator = getAnimator(headerTarget);

        if (isVisible) {
            headerAnimator.playTogether(
                    ObjectAnimator.ofFloat(headerTarget, "translationY", -100.0f, 0.0f),
                    ObjectAnimator.ofFloat(headerTarget, "alpha", 0.0f, 1.0f)
            );
            listAnimator.play(ObjectAnimator.ofFloat(listTarget, "translationY", 0.0f, 160.0f));
        } else {
            headerAnimator.playTogether(
                    ObjectAnimator.ofFloat(headerTarget, "translationY", 0.0f, -100.0f),
                    ObjectAnimator.ofFloat(headerTarget, "alpha", 1.0f, 0.0f)
            );
            listAnimator.play(ObjectAnimator.ofFloat(listTarget, "translationY", 160.0f, 0.0f));
        }

        headerAnimator.start();
        listAnimator.start();
    }

    /**
     * Show or hide the search fab button.
     *
     * @param isVisible
     */
    public void manageFabVisibility(boolean isVisible) {
        AnimatorSet animator = getAnimator(mFabButton);

        if (isVisible) {
            animator.playTogether(
                    ObjectAnimator.ofFloat(mFabButton, "translationY", 100.0f, 0.0f),
                    ObjectAnimator.ofFloat(mFabButton, "alpha", 0.0f, 1.0f)
            );
        } else {
            animator.playTogether(
                    ObjectAnimator.ofFloat(mFabButton, "translationY", 0.0f, 100.0f),
                    ObjectAnimator.ofFloat(mFabButton, "alpha", 1.0f, 0.0f)
            );
        }

        animator.start();
    }

    /**
     * Animator factory method.
     *
     * @param target
     * @return
     */
    public AnimatorSet getAnimator(View target) {
        AnimatorSet animator = new AnimatorSet();
        animator.setTarget(target);
        animator.setDuration(Constants.ANIMATION_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }

    //-------- Progress overrides --------

    /**
     * Call init before we return the frame so we ensure the
     * progress bar is added to the view hierarchy in all
     * setContentView scenarios.
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        getLayoutInflater().inflate(layoutResID, init(), true);
    }

    @Override
    public void setContentView(View view) {
        init().addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        init().addView(view, params);
    }

    /**
     * Force our progress view into the layout and return the content frame
     * to be used for holding the real view.
     *
     * @return
     */
    private ViewGroup init() {
        super.setContentView(R.layout.progress);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_bar);
        return (ViewGroup) findViewById(R.id.activity_frame);
    }
}
