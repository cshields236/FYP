package com.example.fyp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class JourneyInformation implements Parcelable {
    private String name;
    private String time;
    private double leftEye;
    private double rightEye;



    public JourneyInformation() {
    }


    public JourneyInformation(String name, String time, double leftEye, double rightEye) {
        this.name = name;
        this.time = time;
        this.leftEye = leftEye;
        this.rightEye = rightEye;
    }

    public JourneyInformation( double leftEye, double rightEye) {

        this.leftEye = leftEye;
        this.rightEye = rightEye;
    }

    protected JourneyInformation(Parcel in) {
        name = in.readString();
        time = in.readString();
        leftEye = in.readDouble();
        rightEye = in.readDouble();
    }

    public static final Creator<JourneyInformation> CREATOR = new Creator<JourneyInformation>() {
        @Override
        public JourneyInformation createFromParcel(Parcel in) {
            return new JourneyInformation(in);
        }

        @Override
        public JourneyInformation[] newArray(int size) {
            return new JourneyInformation[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLeftEye() {
        return leftEye;
    }


    public void setLeftEye(double leftEye) {
        this.leftEye = leftEye;
    }

    public double getRightEye() {
        return rightEye;
    }

    public void setRightEye(double rightEye) {
        this.rightEye = rightEye;
    }

    @Override
    public String toString() {
        return "JourneyInfo{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", leftEye=" + leftEye +
                ", rightEye=" + rightEye +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(time);
        dest.writeDouble(leftEye);
        dest.writeDouble(rightEye);
    }
}
