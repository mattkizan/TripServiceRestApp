/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip;

import com.google.gson.Gson;
import externalapi.ExternalAPIs;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author matt
 */
@Path("trip")
public class TripOperations {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TripOperations
     */
    public TripOperations() {
    }

    /**
     * Retrieves representation of an instance of trip.TripOperations
     * @return an instance of java.lang.String
     */
    
    
    //NOTE: this is my orchestrator class, the methods assoicated with the rest services 
    // are in the TripService class 
    
    // tripservice class contains my resource ( repository of trips), this is
    // intialised so the methods in this class can have access to the list of trips 
    TripService tripService = new TripService();
    
    //this methods generates UUID by calling the external api method getRandom()
    // from the externalAPI class located in the externalapi class 
    @Path("UniqueID")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String GenerateUniqueID() throws IOException {
   
    
    
    String error = "{\"response\":\"error: unable to generate UUID \"}";
    JSONObject jsonErrorResponse = new JSONObject(error);
    
     try{
          //   retrieves 1 UUID as JSON string 
        return ExternalAPIs.getRandomID();
        
     } catch(Exception e){
         return error;
     }
     
    } 
    
    // this methods returns a list of trips to client based on the location parameter
    @Path("QueryNewTrips")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String QueryForNewTrips(@QueryParam("location")String location) throws IOException {
        

        String myResult = "";
          
              
        myResult =  tripService.queryTripByLocation(location);
           
        return myResult;
        
    
     
    }
    
    // this method adds a new trip to the storage(json file) 
    @Path("ProposeNewTrips")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String ProposeNewTrips(@QueryParam("userID")String userID,@QueryParam("tripID")String tripID, @QueryParam("location")String location,
            @QueryParam("date")String date)throws IOException{
        
      String error = "{\"response\":\"error: unable to add new trip\"}";
       JSONObject jsonErrorResponse = new JSONObject(error);
       // intialises an instance of the model class 
       try {
            Trip t1 = new Trip();
            t1.setUserID(userID);
            t1.setTripID(tripID);
            t1.setLocation(location);
            t1.setDate(date);
            t1.setWeather(0.0);
            t1.setInterest(1);

        // returns json string of : either sucess or error message 
        return tripService.addTrip(t1);
        
       } catch (Exception e){
           return error;
       }
    }
    // this method checks interest value on trip based on userID and tripID 
    @Path("CheckInterest")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String CheckForInterest(@QueryParam("userID")String userID, @QueryParam("tripID")String tripID ) throws IOException{
        
       
        String result = tripService.checkInterest(userID, tripID);
     
        return result;
       
    }
    
    // this method increments the interest value of a trip by 1 given the userID and tripID of a trip 
    @Path("InterestInTrips")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public String InterestInTrips(@QueryParam("userID")String userID, @QueryParam("tripID")String tripID) throws IOException {
        
            String result = tripService.showInterest(userID, tripID);
            
            return result;
        
        
    }
    // retrieves all trips associated with a specific usr ID 
    @Path("QueryUserTrips")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    
    public String QueryUserTrips(@QueryParam("userID")String userID) throws IOException {
        

        String myResult = "";
          
              
        myResult =  tripService.queryTripByUserID(userID);
           
        return myResult;
        
    
     
    }
    
 
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHtml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of TripOperations
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }
}
