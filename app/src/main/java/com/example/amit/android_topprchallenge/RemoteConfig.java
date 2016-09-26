package com.example.amit.android_topprchallenge;

import android.nfc.Tag;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by amit on 25-09-2016.
 */
public class RemoteConfig {


    private static final String TAG = "RemoteConfig";

    private RemoteConfig() {
    }

    public static JSONArray fetchJsonArray() {
        String itemsJson = null;
        try {
            itemsJson = fetchPlainText(Config.BASE_URL);
            Log.v(TAG, itemsJson);
        } catch (IOException e) {
            Log.e(TAG, "Error fetching items JSON", e);
            return null;
        }

        // Parse JSON
        try {

            JSONTokener tokener = new JSONTokener(itemsJson);
            Object val = tokener.nextValue();
            Log.v(TAG, val.toString());
            if (!(val instanceof JSONArray)) {
                throw new JSONException("Expected JSONArray");
            }
            return (JSONArray) val;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing items JSON", e);
        }

        return null;
    }

    static String fetchPlainText(URL url) throws IOException {
        String jsonData = "";
        JSONObject jObject = null;
        JSONArray jArray = null;


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();

            jsonData = response.body().string();
            jObject = new JSONObject(jsonData);
            jArray = jObject.getJSONArray("websites");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v(TAG, jArray.toString());
        return jArray.toString();
    }
}
