package com.example.amit.android_topprchallenge;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.text.format.Time;
import android.util.Log;
import com.example.amit.android_topprchallenge.Database.EventsContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by amit on 25-09-2016.
 */
public class UpdaterService extends IntentService {

    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.example.amit.android_topprchallenge.intent.action.STATE_CHANGE";

    public static final String EXTRA_REFRESHING
            = "com.example.amit.android_topprchallenge.intent.extra.REFRESHING";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");
            return;
        }

        sendBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

        Uri dirUri = EventsContract.Events.buildDirUri();

        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(dirUri).build());

        try {
            JSONArray array = RemoteConfig.fetchJsonArray();
            if (array == null) {
                throw new JSONException("Invalid parsed item array" );
            }

            for (int i = 0; i < array.length(); i++) {
                Log.v(TAG, array.toString());
                ContentValues values = new ContentValues();
                JSONObject object = array.getJSONObject(i);
                values.put(EventsContract.Events.COLUMN_ID, object.getString("id" ));
                Log.v(TAG, object.getString("id"));
                values.put(EventsContract.Events.COLUMN_NAME, object.getString("name" ));
                values.put(EventsContract.Events.COLUMN_IMAGE, object.getString("image" ));
                values.put(EventsContract.Events.COLUMN_CATEGORY, object.getString("category"));
                values.put(EventsContract.Events.COLUMN_DESCRIPTION, object.getString("description" ));
                values.put(EventsContract.Events.COLUMN_EXPERIENCE, object.getString("experience" ));

                cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
            }

            getContentResolver().applyBatch(EventsContract.CONTENT_AUTHORITY, cpo);

        } catch (JSONException | RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Error updating content.", e);
        }

        sendBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }
}
