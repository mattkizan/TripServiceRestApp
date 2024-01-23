/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip;


import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author matt
 */
// this is the model class for the trip object and for the TripService class 
@XmlRootElement
public class Trip {
    // values for a trip object 
    private String userID;
    private String tripID;
    private String location;
    private String date;
    private double weather;
    private int interest;
    
    
    public Trip(){
     
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getWeather() {
        return weather;
    }

    public void setWeather(double weather) {
        this.weather = weather;
    }
    
      public int getInterest() {
        return interest;
    }

    public void setInterest (int interest) {
        this.interest = interest;
    }

    
    
    
}
