package com.example.harshal.inclass09;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshal on 3/26/2018.
 */

public class TripClass {

    String title;
    List<LatLng> points;

    @Override
    public String toString() {
        return "TripClass{" +
                "title='" + title + '\'' +
                ", points=" + points +
                '}';
    }

    public static class Points
    {
        double latitude, longitude;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        @Override
        public String toString() {
            return "Points{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }
}
