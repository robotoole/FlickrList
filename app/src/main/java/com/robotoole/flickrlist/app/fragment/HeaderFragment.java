package com.robotoole.flickrlist.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.robotoole.flickrlist.R;
import com.robotoole.flickrlist.app.event.EventManager;
import com.robotoole.flickrlist.app.event.SearchEvent;
import com.robotoole.flickrlist.app.event.SearchEvent.SearchEventType;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Search header used to search for "n" tags
 * separated by a comma.
 * Created by robert on 2/21/15.
 */
public class HeaderFragment extends Fragment {

    @InjectView(R.id.fragment_header_edittext)
    EditText mSearchText;

    /**
     * Inflate the layout and populate the data.
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
        View rootView = inflater.inflate(R.layout.fragment_header,
                container, false);
        ButterKnife.inject(this, rootView);
        setupListeners();
        return rootView;
    }

    /**
     * Close the search view when clicking on the X.
     *
     * @param view
     */
    @OnClick(R.id.fragment_header_button_close)
    public void onClick(View view) {
        mSearchText.setText("");
        EventManager.post(new SearchEvent(SearchEventType.HIDE));
    }

    /**
     * Set the onEditorAction listener for the edit text to
     * post a search event to search and hide the search.
     */
    public void setupListeners() {
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!TextUtils.isEmpty(v.getText().toString())) {
                    EventManager.post(new SearchEvent(SearchEventType.SEARCH,
                            v.getText().toString(), true));
                    EventManager.post(new SearchEvent(SearchEventType.HIDE));
                }
                return false;
            }
        });
    }
}
