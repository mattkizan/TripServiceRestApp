/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import externalapi.ExternalAPIs;
import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author matt
 */
public class TripService {
    // this is the trip resource class, where the data is read from the json file 
    // this class contains the method for the core functionalites of the service 
    // this class is based on the Trip class (  model for this class)  
    // contains list of trips loaded from the json file 
    private Trip[] listOfTrips;
  
    
    // loads the list of trip to container 
    public TripService() {
        
        
         try {
                    // Set up GSON
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            
            // Read the JSON from file
            String jsonString = "";
            List <String> lines = Files.readAllLines(Paths.get("/Users/matt/Desktop/myTripService/src/java/trip/trips.json"));
            for (String line : lines) {
                jsonString += line;
            }
            
            
         
            this.listOfTrips = gson.fromJson(jsonString, Trip[].class);
            
            
      } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

            
    // returns all trips in the container 
    public Trip[] getAllTrips() throws IOException{

                
     return listOfTrips;
        
    }
    
    // this function adds a Trip object to the array list first,
    // then converts the array list into a json array string 
    // and then finally writes the new json array string to the json file 
    public String  addTrip(Trip t1){
        
        String error = "{\"response\":\"unable to create trip\"}";
        JSONObject jsonError = new JSONObject(error);
        
        String success = "{\"response\":\"trip created\"}";
        JSONObject jsonSuccess = new JSONObject(success);
        
        
      ArrayList<Trip> match = new ArrayList<Trip>();  
      String json = "";
      try{

        for( Trip i : this.listOfTrips ){
           
                match.add(i);        
        }
        match.add(t1);
        
        json = new Gson().toJson(match );
         
        FileWriter file = new FileWriter("/Users/matt/Desktop/myTripService/src/java/trip/trips.json");
        file.write(json);
        file.flush();
        file.close();
        return jsonSuccess.toString();
        
  
      } catch(IOException e){
          e.printStackTrace();
      }
        return jsonError.toString();
    }
    
    // this function iterates through listOfTrips to find the trip with specified location
    // returns the array list empty if no trip is found with the specific location 
    
    public String queryTripByLocation(String location) throws IOException{
        String error = "{\"response\":\"error: unable to query trip\"}";
        JSONObject jsonErrorResponse = new JSONObject(error);
        
        String json = "";
        double temp = 0.0; 
        ArrayList<Trip> match = new ArrayList<Trip>();
        try{
        // adds all trips with location same as the variable to a list 
        for( Trip i : this.listOfTrips ){
            
            if(i.getLocation().equals(location)){
                
                // calls the external api to get the weather for a trip 
                /// given the location as a parameter 
                temp = ExternalAPIs.getWeatherForecast(i.getLocation());
                // sets the weather value for each trip found 
                i.setWeather(temp);
                
                match.add(i);
            }
                
        }
        } catch(Exception e){
            return jsonErrorResponse.toString();
        }
        // checks to see if the list is empty : no trips found 
        if(match.isEmpty())
        {
            return jsonErrorResponse.toString();
            
        }  else{
            // if not , it returns list of trips as a json string
            json = new Gson().toJson(match );
            return json;
        }       
        
  
    }
    // finds all trips associated with a specific user ID 
    public String queryTripByUserID(String userID) throws IOException{
        String error = "{\"response\":\"error: unable to query trip\"}";
        JSONObject jsonErrorResponse = new JSONObject(error);
        
        String json = "";
       
        ArrayList<Trip> match = new ArrayList<Trip>();
        
        
        for( Trip i : this.listOfTrips ){
            
            if(i.getUserID().equals(userID)){
                
                match.add(i);
            }
                
        }
        // checks to see if the list is empty : no trips found 
        if(match.isEmpty())
        {
            return jsonErrorResponse.toString();
            
        }  else{
            // if not , it returns list of trips as a json string
            json = new Gson().toJson(match );
            return json;
        }
       
    }
    
    // this function checks interest for a trip given the userID and tripID
    public String checkInterest(String userID, String tripID){
        
        String error = "{\"response\":\"error: unable to find check interest for requested parameter \"}";
        JSONObject jsonErrorResponse = new JSONObject(error);
        JsonObject json = new JsonObject();
       
        try{
            
 
        for( Trip i : this.listOfTrips ){
              if(i.getUserID().equals(userID) && i.getTripID().equals(tripID)){
               int interest = i.getInterest();
              // if trip found, interest will be returned as json string to client
               json.addProperty("interest", interest);
               return json.toString();
            }
           
        }
        
        
        } catch(Exception e){
            e.printStackTrace();
        }
        
       // error if no trip is found 
        return jsonErrorResponse.toString();
        
    }
    
    // this function increments interest level by 1 for a trip given the userID and tripID 
    public String showInterest(String userID, String tripID) throws IOException{
          
        String error = "{\"response\":\"error: unable to express interest for requested parameter \"}";
        String success = "{\"response\":\"successfully submited interest\"}";
        JSONObject jsonErrorResponse = new JSONObject(error);
        JSONObject jsonSuccessResponse = new JSONObject(success);
        // puts all the trips into a list 
        ArrayList<Trip> listOfTrips2 = new ArrayList<Trip>();
        
        for( Trip i : this.listOfTrips ){
           
                listOfTrips2.add(i);
        }
        
        // iterates through array list until trip is found, then it incremenates the value by 1 and finally writes the updated list of trips to json file
        try {
        int index  = 0; 
        int interestValue = 0;
         for( Trip i : listOfTrips2 ){
              if(i.getUserID().equals(userID) && i.getTripID().equals(tripID)){
                  
                 
                  interestValue = i.getInterest();
                  i.setInterest(interestValue+1);
                  
                  String json = new Gson().toJson(listOfTrips2 );
                  FileWriter file = new FileWriter("/Users/matt/Desktop/myTripService/src/java/trip/trips.json");
                  file.write(json);
                  file.flush();
                  file.close(); 
                  
                  return jsonSuccessResponse.toString();
  
            }
         }
        } catch (IOException e){
             e.printStackTrace();
             
        }
     
    
                    
        return jsonErrorResponse.toString();
    }
    
    
    
    
    
    
  
        
    
}
