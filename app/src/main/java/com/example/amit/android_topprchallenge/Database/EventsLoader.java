package com.example.amit.android_topprchallenge.Database;

import android.content.Context;
import android.net.Uri;
import android.content.CursorLoader;

/**
 * Created by amit on 25-09-2016.
 */
public class EventsLoader extends CursorLoader {

    public static EventsLoader newAllArticlesInstance(Context context) {
        return new EventsLoader(context, EventsContract.Events.buildDirUri());
    }

    public static EventsLoader newInstanceForItemId(Context context, long itemId) {
        return new EventsLoader(context, EventsContract.Events.buildItemUri(itemId));
    }

    private EventsLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null,null);
    }

    public interface Query {
        String[] PROJECTION = {
                EventsContract.EventsColumns._ID,
                EventsContract.EventsColumns.COLUMN_ID,
                EventsContract.EventsColumns.COLUMN_NAME,
                EventsContract.EventsColumns.COLUMN_IMAGE,
                EventsContract.EventsColumns.COLUMN_CATEGORY,
                EventsContract.EventsColumns.COLUMN_DESCRIPTION,
                EventsContract.EventsColumns.COLUMN_EXPERIENCE,

        };



        int _ID = 0;
        int COLUMN_ID =1;
        int NAME = 2;
        int IMAGE_URL = 3;
        int CATEGORY = 4;
        int DESCRIPTION = 5;
        int EXPERIENCE = 6;


    }
}
