package com.example.amit.android_topprchallenge.Database;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by amit on 25-09-2016.
 */
public class EventsContract {

    public static final String CONTENT_AUTHORITY = "com.example.amit.android_topprchallenge";
    public static final Uri BASE_URI = Uri.parse("content://com.example.amit.android_topprchallenge");

   public static final String PATH_EVENTS = "events";
 //   public static final String IS_FAV = "is_favorite";
//    public static final String PATH_FAVORITE = "favorite";

    interface EventsColumns {
        /** Type: INTEGER PRIMARY KEY AUTOINCREMENT */
        String _ID ="_id";
        /** Type: TEXT */
        String COLUMN_ID = "id";
        /** Type: TEXT */
        String COLUMN_NAME = "name";
        /** Type: TEXT NOT NULL */
        String COLUMN_IMAGE = "image";
        /** Type: TEXT NOT NULL */
        String COLUMN_CATEGORY = "category";
        /** Type: TEXT NOT NULL */
        String COLUMN_DESCRIPTION = "description";
        /** Type: TEXT NOT NULL */
        String COLUMN_EXPERIENCE = "experience";

   //     String COLUMN_FAVORITE_RECORD_ID ="id";




    }



    public static class Events implements EventsColumns {

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;



        //CURSOR_DIR_BASE_TYPE for a number of records i.e. rows
        //CURSOR_ITEM_BASE_TYPE for a single record





        /** Matches: /events/ */
        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath(PATH_EVENTS).build();
        }

        /** Matches: /events/[_id]/ */
        public static Uri buildItemUri(long _id) {
            return BASE_URI.buildUpon().appendPath(PATH_EVENTS).appendPath(Long.toString(_id)).build();
        }

        /** Read event ID event detail URI. */
        public static long getItemId(Uri eventsUri) {
            return Long.parseLong(eventsUri.getPathSegments().get(1));
        }
    }

    /**
    public static class Favorites implements EventsColumns {

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;



        //CURSOR_DIR_BASE_TYPE for a number of records i.e. rows
        //CURSOR_ITEM_BASE_TYPE for a single record






        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath(PATH_FAVORITE).build();
        }


        public static Uri buildItemUri(long _id) {
            return BASE_URI.buildUpon().appendPath(PATH_FAVORITE).appendPath(Long.toString(_id)).build();
        }


        public static long getItemId(Uri favoritesUri) {
            return Long.parseLong(favoritesUri.getPathSegments().get(1));
        }
    }

*/
    }

