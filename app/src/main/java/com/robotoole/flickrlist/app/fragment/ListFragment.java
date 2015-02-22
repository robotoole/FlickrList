package com.robotoole.flickrlist.app.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.robotoole.flickrlist.Constants;
import com.robotoole.flickrlist.R;
import com.robotoole.flickrlist.app.adapter.PictureAdapter;
import com.robotoole.flickrlist.app.event.EventManager;
import com.robotoole.flickrlist.app.event.ProgressEvent;
import com.robotoole.flickrlist.app.event.RecyclerClickEvent;
import com.robotoole.flickrlist.app.event.SearchEvent;
import com.robotoole.flickrlist.app.event.ServiceEvent;
import com.robotoole.flickrlist.model.Picture;
import com.robotoole.flickrlist.util.IntentLauncher;
import com.robotoole.flickrlist.util.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Fragment that displays the list of Images returned from the Flickr api.
 */
public class ListFragment extends Fragment {
    /**
     * Shared elements passed across to the detail view
     * via the ActivityOptions.
     */
    private View mSharedTitle;
    private View mSharedAuthor;
    private View mSharedImageView;

    /**
     * Pictures list.
     */
    List<Picture> pictures = new ArrayList<Picture>();

    @InjectView(R.id.fragment_flickr_list)
    protected RecyclerView mListView;
    private PictureAdapter mAdapter;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate the layout and initialize the RecyclerView.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list,
                container, false);

        ButterKnife.inject(this, rootView);

        initializeListViewAdapter();

        return rootView;
    }

    /**
     * Register as an event listener and ensure we have some pictures to show.
     */
    @Override
    public void onResume() {
        super.onResume();

        EventManager.register(this);

        if (pictures == null || pictures.size() == 0) {
            getPicturesByService(QueryBuilder.buildFlickrTagsQuery(
                    getString(R.string.boston)), false);
        }
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
     * Listen for the response from the IntentService.
     *
     * @param event
     */
    public void onEventMainThread(ServiceEvent event) {
        switch (event.type) {
            case SUCCESS:
                pictures = event.pictures;
                notifyAdapter();
                break;
            case FAULT:
                Toast.makeText(getActivity(),
                        event.message,
                        Toast.LENGTH_LONG).show();
                break;
            default:
                //do nothing.
        }

        EventManager.post(new ProgressEvent(ProgressEvent.ProgressEventType.HIDE));
    }

    /**
     * Listen for the click event from the recycler view. Get the selected index
     * from the view tag and pass on the picture object and activity
     * transition options to the detail view.
     *
     * @param event
     */
    public void onEvent(RecyclerClickEvent event) {
        //get all the data from the event
        int selectedIndex = Integer.parseInt(event.viewClicked.getTag().toString());
        mSharedTitle = event.viewClicked.findViewById(R.id.fragment_list_item_title);
        mSharedImageView = event.viewClicked.findViewById(R.id.fragment_list_item_image);
        mSharedAuthor = event.viewClicked.findViewById(R.id.fragment_list_item_author);
        //create the transition
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                Pair.create(mSharedTitle, "title"),
                Pair.create(mSharedAuthor, "author"),
                Pair.create(mSharedImageView, "image"));

        //load the detail view
        IntentLauncher.launchDetail(pictures.get(selectedIndex),
                getActivity(),
                options);
    }

    /**
     * Listen for the SearchEvent and execute the search.
     *
     * @param event
     */
    public void onEvent(SearchEvent event) {
        switch (event.type) {
            case SEARCH:
                if (!TextUtils.isEmpty(event.query)) {
                    getPicturesByService(QueryBuilder.buildFlickrTagsQuery(
                            event.query), event.forceUpdate);
                }
                break;
            default:
                //do nothing;
        }
    }

    /**
     * Initialize a blank list view with no data.
     */
    private void initializeListViewAdapter() {
        mAdapter = PictureAdapter.newInstance(mListView);
        mListView.setAdapter(mAdapter);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(layoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
    }

    /**
     * Alert the adapter in the fragment that the pictures have been updated.
     */
    public void notifyAdapter() {
        mAdapter.updatePictures(pictures);
    }

    /**
     * Kick off an IntentService to retrieve the pictures.
     *
     * @param tags
     */
    public void getPicturesByService(String tags, boolean forceUpdate) {
        EventManager.post(new ProgressEvent(ProgressEvent.ProgressEventType.SHOW));
        IntentLauncher.launchService(Constants.FLICKR_API_URL + tags, getActivity(), forceUpdate);
        getActivity().setTitle(getString(R.string.app_name) + " - " + tags);
    }
}
