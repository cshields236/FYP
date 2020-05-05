package com.example.fyp.Entities;

import com.google.gson.JsonObject;

public class places {

    private String place_id;
    private String name;
    private String rating;
    private String latitude;
    private String longitude;
    private String vicinity;
    private String distance;

    public places(JsonObject placeJSONObject)
    {
        place_id = placeJSONObject.get("place_id").getAsString();
        name = placeJSONObject.get("name").getAsString();
        rating = placeJSONObject.get("rating").getAsString();
        latitude = placeJSONObject.getAsJsonObject("geometry").getAsJsonObject("location").get("lat").getAsString();
        longitude = placeJSONObject.getAsJsonObject("geometry").getAsJsonObject("location").get("lng").getAsString();
        vicinity = placeJSONObject.get("vicinity").getAsString();
//        distance = placeJSONObject.get("")
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }
    public String getVicinity()
    {
        return vicinity;
    }
    public String getLatitude(){
        return latitude;
    }
    public String getLongitude(){
        return longitude;
    }
}
