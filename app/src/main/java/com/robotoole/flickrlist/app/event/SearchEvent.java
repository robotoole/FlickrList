package com.robotoole.flickrlist.app.event;

/**
 * Created by robert on 2/21/15.
 */
public class SearchEvent {
    public enum SearchEventType {
        SHOW, HIDE, SEARCH
    }

    public SearchEventType type;
    public String query;
    public boolean forceUpdate;

    public SearchEvent(SearchEventType type) {
        this.type = type;
    }

    public SearchEvent(SearchEventType type, String query, boolean forceUpdate) {
        this.type = type;
        this.query = query;
        this.forceUpdate = forceUpdate;
    }
}
