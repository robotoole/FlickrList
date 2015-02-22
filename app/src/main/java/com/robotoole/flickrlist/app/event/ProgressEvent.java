package com.robotoole.flickrlist.app.event;

/**
 * Created by robert on 2/21/15.
 */
public class ProgressEvent {
    public enum ProgressEventType {
        SHOW, HIDE
    }

    public ProgressEventType type;

    public ProgressEvent(ProgressEventType type) {
        this.type = type;
    }
}
