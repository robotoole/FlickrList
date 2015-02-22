package com.robotoole.flickrlist.util;

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
    public static String buildFlickrTagsQuery(String[] tags) {
        String query = "";
        String separator = (tags.length > 1) ? "," : "";
        for (String tag : tags) {
            if (tag == tags[tags.length - 1]) {
                //ensure no trailing comma's
                separator = "";
            }

            query += tag.trim() + separator;
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
        if (tags.contains(",")) {
            return buildFlickrTagsQuery(tags.split(","));
        }
        return tags;
    }
}
