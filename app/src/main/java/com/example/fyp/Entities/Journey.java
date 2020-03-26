package com.example.fyp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Journey implements Parcelable {
    private List<JourneyInformation> journeyInformationss = new ArrayList<JourneyInformation>();
    private String time;
    private String id;


    public Journey() {

    }


    public Journey(List<JourneyInformation> journeyInformationss) {
        this.journeyInformationss = journeyInformationss;
    }

    public Journey(String id) {

        this.id = id;
    }

    protected Journey(Parcel in) {
        journeyInformationss = in.createTypedArrayList(JourneyInformation.CREATOR);
        time = in.readString();
        id = in.readString();
    }

    public static final Creator<Journey> CREATOR = new Creator<Journey>() {
        @Override
        public Journey createFromParcel(Parcel in) {
            return new Journey(in);
        }

        @Override
        public Journey[] newArray(int size) {
            return new Journey[size];
        }
    };

    public List<JourneyInformation> getJourneyInformationss() {
        return journeyInformationss;
    }

    public void setJourneyInformationss(List<JourneyInformation> journeyInformationss) {
        this.journeyInformationss = journeyInformationss;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "journeyInformationss=" + journeyInformationss +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(journeyInformationss);
        dest.writeString(time);
        dest.writeString(id);
    }
}
