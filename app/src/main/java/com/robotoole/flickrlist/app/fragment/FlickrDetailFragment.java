package com.robotoole.flickrlist.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robotoole.flickrlist.Constants;
import com.robotoole.flickrlist.R;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Fragment to load the image details.
 * Fragment may be overkill at this point but it's ready for master detail
 * layout if there is a requirement for it later.
 * Created by robert on 2/20/15.
 */
public class FlickrDetailFragment extends Fragment {

    @InjectView(R.id.fragment_list_item_image)
    public ImageView mImageView;
    @InjectView(R.id.fragment_list_item_title)
    public TextView mTitle;

    String title;
    String url;

    /**
     * Inflate the layout and populate the data.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_flickr_detail,
                container, false);
        ButterKnife.inject(this, rootView);

        getPictureInfo();

        loadInfo();

        return rootView;
    }

    /**
     * Clear butterknife.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        ButterKnife.reset(this);
    }

    /**
     * Grab the url and title from the bundle.
     */
    public void getPictureInfo() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle != null){
            title = bundle.getString(Constants.EXTRA_TITLE);
            url = bundle.getString(Constants.EXTRA_API_URL);
        }
    }

    /**
     * Set the title text and load the image.
     */
    public void loadInfo() {
        mTitle.setText(title);

        Picasso.with(getActivity())
                .load(url)
                .into(mImageView);
    }
}
