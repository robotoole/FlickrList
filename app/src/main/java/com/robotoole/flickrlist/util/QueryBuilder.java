package com.robotoole.flickrlist.util;

import java.util.List;

/**
 * Created by robert on 2/19/15.
 */
public class QueryBuilder {
    /**
     * Build a query to append to the flickr api url.
     *
     * @param tags List of tags to search for
     * @return
     */
    public static String buildFlickrTagsQuery(List<String> tags) {
        String query = "";
        String separator = (tags.size() > 1) ? "," : "";
        for (String tag : tags) {
            query += tag + separator;
        }
        return query;
    }

    /**
     * Build a query to append to the flickr api url.
     *
     * @param tags List of tags to search for
     * @return
     */
    public static String buildFlickrTagsQuery(String tags) {
        return tags;
    }
}
