package com.example.amit.android_topprchallenge.Database;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 25-09-2016.
 */
public class EventsProvider extends ContentProvider {

    private SQLiteOpenHelper mOpenHelper;

    private static final int EVENTS = 0;
    private static final int EVENTS__ID = 1;
    private static final int FAVORITE = 2;
    private static final int FAVORITE__ID = 3;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = EventsContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, EventsContract.PATH_EVENTS, EVENTS);
        matcher.addURI(authority, EventsContract.PATH_EVENTS + "/#", EVENTS__ID);
//        matcher.addURI(authority, EventsContract.PATH_FAVORITE, FAVORITE);
 //       matcher.addURI(authority, EventsContract.PATH_FAVORITE + "/*", FAVORITE__ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new EventsDatabase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS:
                return EventsContract.Events.CONTENT_TYPE;
            case EVENTS__ID:
                return EventsContract.Events.CONTENT_ITEM_TYPE;
            /*
            case FAVORITE:
                return EventsContract.Favorites.CONTENT_TYPE;
            case FAVORITE__ID:
                return EventsContract.Favorites.CONTENT_ITEM_TYPE;
                */
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final SelectionBuilder builder = buildSelection(uri);
        Cursor cursor = builder.where(selection, selectionArgs).query(db, projection, sortOrder);
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;


    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS: {
                final long _id = db.insertOrThrow(EventsContract.PATH_EVENTS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return EventsContract.Events.buildItemUri(_id);
            }
            /*
            case FAVORITE: {
                final long _id = db.insertOrThrow(EventsContract.PATH_FAVORITE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return EventsContract.Favorites.buildItemUri(_id);
            }
            */
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSelection(uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return builder.where(selection, selectionArgs).update(db, values);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSelection(uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return builder.where(selection, selectionArgs).delete(db);
    }

    private SelectionBuilder buildSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        return buildSelection(uri, match, builder);
    }

    private SelectionBuilder buildSelection(Uri uri, int match, SelectionBuilder builder) {
        final List<String> paths = uri.getPathSegments();
        switch (match) {
            case EVENTS: {
                return builder.table(EventsContract.PATH_EVENTS);
            }
            case EVENTS__ID: {
                final String _id = paths.get(1);
                return builder.table(EventsContract.PATH_EVENTS).where(EventsContract.Events._ID + "=?", _id);
            }
            /*
            case FAVORITE: {
                final String _id = paths.get(1);
                return builder.table(EventsContract.PATH_FAVORITE);
            }
            case FAVORITE__ID: {
                final String _id = paths.get(1);
                return builder.table(EventsContract.PATH_FAVORITE).where(EventsContract.Favorites._ID + "=?", _id);
            }
            */
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /**
     * Apply the given set of {@link ContentProviderOperation}, executing inside
     * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

}
