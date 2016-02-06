package com.groundzero.alkemy.utils;

import com.groundzero.alkemy.objects.AObject;
import com.parse.ParseGeoPoint;

/**
 * Created by al3x901 on 11/12/2015.
 */
public class ParseUtils {
    public static ParseGeoPoint createParseGeoPoint(AObject lastKnownLocation) {
        return new ParseGeoPoint((double)lastKnownLocation.get(ALocation.LATITUDE), (double) lastKnownLocation.get(ALocation.LONGITUDE));
    }
}
