package com.robotoole.flickrlist.app.event;

import com.robotoole.flickrlist.model.Picture;

import java.util.List;

/**
 * Event used to notify the UI of a success or failure of
 * retrieving data.
 * Created by robert on 2/19/15.
 */
public class ServiceEvent {
    public enum ServiceEventType {
        SUCCESS, FAULT;
    }

    public ServiceEventType type;
    public String message;
    public List<Picture> pictures;

    /**
     * Fault constructor.
     *
     * @param type
     * @param message
     */
    public ServiceEvent(ServiceEventType type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Success constructor.
     *
     * @param type
     * @param pictures
     */
    public ServiceEvent(ServiceEventType type, List<Picture> pictures) {
        this.type = type;
        this.pictures = pictures;
    }
}
