package com.example.fyp.Entities;

import java.util.ArrayList;
import java.util.List;

public class Journey {
    private List<JourneyInformation> journeyInformationss = new ArrayList<JourneyInformation>();


    public Journey() {

    }


    public Journey(List<JourneyInformation> journeyInformationss) {
        this.journeyInformationss = journeyInformationss;
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
}
