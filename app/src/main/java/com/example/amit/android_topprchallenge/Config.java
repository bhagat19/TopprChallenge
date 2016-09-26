package com.example.amit.android_topprchallenge;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by amit on 25-09-2016.
 */
public class Config {
    public static final URL BASE_URL;

    static {
        URL url = null;
        try {
            url = new URL("https://hackerearth.0x10.info/api/toppr_events?type=json&query=list_events" );
        } catch (MalformedURLException ignored) {

        }

        BASE_URL = url;
    }
}
