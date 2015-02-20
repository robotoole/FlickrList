package com.robotoole.flickrlist.app.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.robotoole.flickrlist.Constants;
import com.robotoole.flickrlist.R;
import com.robotoole.flickrlist.app.adapter.PictureAdapter;
import com.robotoole.flickrlist.app.event.EventManager;
import com.robotoole.flickrlist.app.event.RecyclerClickEvent;
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
public class FlickrListFragment extends Fragment {
    private View mSharedTitle;
    private View mSharedImageView;
    /**
     * Pictures list.
     */
    List<Picture> pictures = new ArrayList<Picture>();
    int selectedIndex = 0;

    @InjectView(R.id.fragment_flickr_list)
    protected RecyclerView mListView;
    private PictureAdapter mAdapter;

    /**
     * Pass in the api url that was generated from the header fragment...or somewhere else
     *
     * @param url the new parameter to call.
     * @return A new instance of fragment FlickrListFragment.
     */
    public static FlickrListFragment newInstance(String url) {
        FlickrListFragment fragment = new FlickrListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_API_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public FlickrListFragment() {
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
     * Inflate the layout and initialize the List.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_flickr_list,
                container, false);
        ButterKnife.inject(this, rootView);
        initializeListViewAdapter();
        //need to set shared element after click!

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
            getPicturesByService(QueryBuilder.buildFlickrTagsQuery("boston"));
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
        mSharedTitle = event.viewClicked.findViewById(R.id.fragment_list_item_title);
        mSharedImageView = event.viewClicked.findViewById(R.id.fragment_list_item_image);
        selectedIndex = Integer.parseInt(event.viewClicked.getTag().toString());

        //create the transition
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                Pair.create((View) mSharedTitle, "title"),
                Pair.create((View) mSharedImageView, "image"));

        //load the detail view
        IntentLauncher.launchDetail(pictures.get(selectedIndex), getActivity(), options);
    }

    /**
     * Alert the adapter in the fragment that the pictures have been updated.
     */
    public void notifyAdapter() {
        mAdapter.updatePictures(pictures);
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
     * Kick off an IntentService to retrieve the pictures.
     *
     * @param tags
     */
    public void getPicturesByService(String tags) {
        IntentLauncher.launchService(Constants.FLICKR_API_URL + tags, getActivity(), false);
    }
}
