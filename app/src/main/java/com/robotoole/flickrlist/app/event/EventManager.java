package com.robotoole.flickrlist.app.event;

import android.util.Log;

import de.greenrobot.event.EventBus;

/**
 * EventManager helps to cleanly subscribe/un-subscribe to the default EventBus as well as gives us
 * a place to create individual busses.
 *
 * @author robert
 */
public class EventManager {
    public static final int EVENTBUS_PRIORITY_ONE = 1;
    private static final String TAG = EventManager.class.getSimpleName();

    /**
     * Private constructor for utility class.
     */
    private EventManager() {
    }

    /**
     * Get the current default EventBus for EventManager.
     *
     * @return The current Bus.
     */
    public static EventBus getBus() {
        return EventBus.getDefault();
    }

    /**
     * Helper to keep the views cleaner.
     *
     * @param subscriber event object.
     */
    public static void register(Object subscriber) {
        Log.v(TAG, "Registering listener: " + subscriber);

        if (!getBus().isRegistered(subscriber)) {
            getBus().register(subscriber);
        }
    }

    /**
     * Helper to keep the views cleaner. This method allows us to register Events with higher
     * priorities
     *
     * @param subscriber event object.
     * @param priority   priority of the event.
     */
    public static void register(Object subscriber, int priority) {
        if (!getBus().isRegistered(subscriber)) {
            getBus().register(subscriber, priority);
        }
    }

    /**
     * Helper to register for sticky events.
     *
     * @param subscriber event object.
     */
    public static void registerSticky(Object subscriber) {
        if (!getBus().isRegistered(subscriber)) {
            getBus().registerSticky(subscriber);
        }
    }

    /**
     * Unregister a subscriber.
     *
     * @param subscriber event object.
     */
    public static void unregister(Object subscriber) {
        Log.v(TAG, "Unregistering listener: " + subscriber);
        getBus().unregister(subscriber);
    }

    /**
     * Post an event to the default bus.
     *
     * @param event event object.
     */
    public static void post(Object event) {
        Log.v(TAG, String.format("Event %s posted", event.getClass()
                .getSimpleName()));
        getBus().post(event);
    }

    /**
     * Helper to post sticky events.
     *
     * @param event event object.
     */
    public static void postSticky(Object event) {
        getBus().postSticky(event);
    }

    /**
     * Helper to get sticky events.
     *
     * @param <T>       The class of the event type.
     * @param eventType The type of the event.
     * @return The sticky event of class eventType
     */
    public static <T> T getStickyEvent(Class<T> eventType) {
        return getBus().getStickyEvent(eventType);
    }

    /**
     * Helper to remove sticky events.
     *
     * @param <T>       The class of the event type.
     * @param eventType The type of the event.
     * @return The sticky event of class eventType
     */
    public static <T> T removeStickyEvent(Class<T> eventType) {
        return getBus().removeStickyEvent(eventType);
    }

    /**
     * Helper to keep the views cleaner. One line is better than 2! This method allows us to cancel
     * lower priority events downstream. Check out {@link #register(Object, int)}
     *
     * @param sub event object.
     */
    public static void cancelEventDelivery(Object sub) {
        if (!getBus().isRegistered(sub)) {
            getBus().cancelEventDelivery(sub);
        }
    }
}
