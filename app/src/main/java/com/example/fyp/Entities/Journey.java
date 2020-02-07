package com.example.fyp.Entities;

import java.util.ArrayList;
import java.util.List;

public class Journey {
    private List<JourneyInformation> journeyInformationss = new ArrayList<JourneyInformation>();
    private String time;

    public Journey() {

    }


    public Journey(List<JourneyInformation> journeyInformationss) {
        this.journeyInformationss = journeyInformationss;
    }

    public Journey(String time) {

        this.time = time;
    }

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
}
