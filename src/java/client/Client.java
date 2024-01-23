/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import static java.lang.System.console;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Scanner;
import org.apache.http.client.HttpClient;
import org.json.JSONObject;




import trip.Trip;

/**
 *
 * @author matt
 */
public class Client {
    public static void main(String  args[]) throws IOException {

        // main controller - user has to enter the number 6 to exit the program 
        // generates a userID for the running session 
        String userID = generateID();

        int userselected;
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        
        do{
          // menue function froces the user to enter a number and returns the response
           userselected = menueData();
           
           switch(userselected){
               // option 1 : generate use id, parses the json response and prints it the user 
               case 1:
                  clear();
                  String uuid = generateID();
                   System.out.println("UUID: " + uuid);
               
                   break;
                   
                case 2:
                     // option 2  : queries trip based on location given by the user, parses the json response and prints list of trips 
                   clear();
                    String response = queryTrips();
                        // if user enters location that is not in the database, error message will be thrown  
                        try{
                            
                            Trip[] listOfTrips = gson.fromJson(response, Trip[].class);
         
                        for (int i = 0; i < listOfTrips.length ; i++){

                            System.out.println(i + " location:" + listOfTrips[i].getLocation() +" date:" + listOfTrips[i].getDate()+ 
                                    " weather:" + listOfTrips[i].getWeather() + "°C " + " interest:" + listOfTrips[i].getInterest() );
                                     
                        }  
                        System.out.println("\b");
                        System.out.println("\b");
                        }  catch (Exception e) {
                        System.out.println("Trip not avalible for that location");
                        }
                   break;
                   
                   
                case 3:
                    // option 3  : adds trip based on location and date given by the user, parses the json response and prints server response
                    // generated userID for this session, is passed as userID when adding a new trip 
                   clear();
                   proposeTrip(userID);
                  
                   break;
                   
               case 4:
                   // option 4  : expresses interest based on userID and tripID, parses the json response and prints server response
                   clear();
                   expressInterest();
                   break;
                   
                case 5:
                    // option 5 : returns interest value based on userID and tripID, parses the json response and prints interest value to the user
                   clear();
                   viewInterest(userID);
                    
                   break;
                   
                case 6:
                    // exist the main program 
                    clear();
                    System.out.println("program exited");
                    
                   break;
                  
           }
        } while(userselected != 6);
        
        
        
    
    }
    // menue function to return users option
    static public int menueData(){
        
        Scanner sc = new Scanner(System.in);
        int number;
        
     
        do {
            
             System.out.println("Please select one of the 6 options");
            System.out.println("option 1 ---- generate id ");
            System.out.println("option 2 ---- query for new trips ");
            System.out.println("option 3 ---- propose new trips ");
            System.out.println("option 4 ---- express interest in trips ");
            System.out.println("option 5 ---- check for interest on my trip ");
            System.out.println("option 6 ---- exit ");

            while (!sc.hasNextInt() ) {
                System.out.println("please enter a number from 1 to 6");
                sc.next(); 
            }
            number = sc.nextInt();
        } while (number > 0 && number > 6);
        

       return number;
        
    }
    //
    //
    //
    // function to clear console screen 
    static public void clear() {
    for(int i = 0; i < 100; i++)
        {
            System.out.println("\b");
        }
    }
    // function to request/GET UUID from the orchestrator and return it as string; 
    static public String generateID() {
         String response = "";
        try {
           
            URL url = new URL("http://localhost:8080/myTripService/webresources/trip/UniqueID" );
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();


            BufferedReader bfReader = new BufferedReader(new InputStreamReader( con.getInputStream())); 
            
            
 
          String temp = "";
          while((temp = bfReader.readLine()) != null){
               response += temp;
          }
        } catch (Exception e) {
          System.out.println("error generating ID");
        } 
       
         JSONObject obj = new JSONObject(response);
         String ID = obj.getString("UUID").toString();

        return ID;
    }
    
    //function to return response that is a string and contains list of trips from the orchestrator based on location given by the user 
   static  public String queryTrips() throws MalformedURLException, IOException{
       System.out.println("location to test : kochi,tokyo,paris,london,sydney,istanbul");
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String response = "";
        Scanner sc = new Scanner(System.in);
  //   asks user for location 
        System.out.println("enter a location");
        String location = sc.next();
       
        

        try {
           
        URL url = new URL("http://localhost:8080/myTripService/webresources/trip/QueryNewTrips?location="+location.toLowerCase());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        
        
        BufferedReader bfReader = new BufferedReader(new InputStreamReader( con.getInputStream()));
          
          String temp = "";
          while((temp = bfReader.readLine()) != null){
               response += temp;
          }
          
          //System.out.println(response);
          
        } catch (Exception e) {
            System.out.println("error querying location");
        } 
        
        // retreives response from server : either error response or list of trips assocaited with a location 
        return response;
       
        
     
    }
   // function to query list of trips based on userID, returns response string 
   // used for the: view interest function 
   static  public String queryUserTrips(String userID ) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String response = "";
        
       
  
        try {
           
        URL url = new URL("http://localhost:8080/myTripService/webresources/trip/QueryUserTrips?userID="+userID);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        
        
        BufferedReader bfReader = new BufferedReader(new InputStreamReader( con.getInputStream()));
          
          String temp = "";
          while((temp = bfReader.readLine()) != null){
               response += temp;
          }
          
          //System.out.println(response);
          
        } catch (Exception e) {
            System.out.println("error querying location");
        } 
        
        
        return response;
       
   }
   
   // function to add a trip to the json file ( storage) by asking user for input : lcoation and date 
   // tripId is randomly generated 
   // userID that was generated for the session is passed through
   static public void proposeTrip(String userID ) throws MalformedURLException, IOException {
       
       
       String response = "";
       String tripID = generateID();
        
       Scanner sc = new Scanner(System.in);
        // asks  user for input that will be sent to orachestrator 
        System.out.println("enter a location");
        String location = sc.next();
        
        System.out.println("enter a date");
        String date = sc.next();
       
        
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("tripID", tripID);
        parameters.put("location", location.toLowerCase());
        parameters.put("date", date);
        
        String convertedParamsToString = "";
        
        StringBuilder mapAsString = new StringBuilder("");
        for (String key : parameters.keySet()) {
        mapAsString.append(key + "=" + parameters.get(key) + "&" );
        }
       convertedParamsToString = mapAsString.toString(); 
       convertedParamsToString  = convertedParamsToString.substring(0, convertedParamsToString.length() - 1);
        
       
       try{
           
        URL url = new URL("http://localhost:8080/myTripService/webresources/trip/ProposeNewTrips?" + convertedParamsToString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.connect();
        
        
        BufferedReader bfReader = new BufferedReader(new InputStreamReader( con.getInputStream()));
        
        
          
          String temp = "";
          while((temp = bfReader.readLine()) != null){
              response += temp;
              
          }
      
       } catch (Exception e){
           System.out.println("failed to add trip ");
       }
      // returns response from orchestrator and presents it to the user 
       JSONObject obj = new JSONObject(response);
       String finalResponse = obj.getString("response").toString();
       System.out.println("server response: " + finalResponse);
       System.out.println("\b");
       System.out.println("\b");
       
   }
   
    
    // function that updates interest level of a trip by sending userID and tripID values of the specfific trip, to the orchestrator 
   // it first invokes the queryTrips() function, mentioned earlier to prompt the user to search for a location, so it can retrieve a list of trips
   //  then it prompts the user to enter a number corresponding with a list of trips 
   // which ever number th user entered, will be used as an index to index that trip in the listOftrips and send the userID and tripID of that instance of the trip 
   // to the orchestrator for it to update and increment the interest level for that trip by 1 
   static public void expressInterest() throws IOException  {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        int number;
        Scanner sc = new Scanner(System.in);
        // invokes the queryTrips method, mentioned previously, to prompt user to enter a location 
        // and find list of trips assocaited in that location
        //  will return error message if location entered is not valid location in the JSON file 
       String queryResponse = queryTrips();
       
       try{
           
       Trip[] listOfTrips = gson.fromJson(queryResponse, Trip[].class);
        for (int i = 0; i < listOfTrips.length ; i++){

        System.out.println(i + " location:" + listOfTrips[i].getLocation() +" date:" + listOfTrips[i].getDate()+ 
                                    " weather:" + listOfTrips[i].getWeather() + "°C " + " interest:" + listOfTrips[i].getInterest() );
        }  
                                
       System.out.println("\b");
                                
       // asks user to select which trip trip they would like to express interest in, takes in an int 
        do {
             System.out.println("Please select number corresponding to the trip");
           
            while (!sc.hasNextInt() ) {
                System.out.println("please enter a number ");
                sc.next(); 
            }
            number = sc.nextInt();
        } while (number > 0 && number > listOfTrips.length);
       
       
       // uses the int, taken by the user, to find the trip ( in the list of trips) that they would like to express interest in.
        String userID = listOfTrips[number].getUserID();
        String tripID = listOfTrips[number].getTripID();
        
       // forms parameter to send the request to updated trip interest level 
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("tripID", tripID);
        
        String response = "";
        
        String convertedParamsToString = "";
        
        StringBuilder mapAsString = new StringBuilder("");
        for (String key : parameters.keySet()) {
        mapAsString.append(key + "=" + parameters.get(key) + "&" );
         }

       convertedParamsToString = mapAsString.toString(); 
       convertedParamsToString  = convertedParamsToString.substring(0, convertedParamsToString.length() - 1);
        // sends userId and tripId of the trip they would like to express interest in 
        try{
        
        URL url = new URL("http://localhost:8080/myTripService/webresources/trip/InterestInTrips?" + convertedParamsToString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.connect();
        
        
        BufferedReader bfReader = new BufferedReader(new InputStreamReader( con.getInputStream()));
        
       
         String temp = null;
          while((temp = bfReader.readLine()) != null){
              response += temp;
          }
          
        } catch(Exception e){
             System.out.println("failed to express interest");
        }
       // System.out.println(response);
        
          JSONObject obj = new JSONObject(response);
          String finalResponse = obj.getString("response").toString();
          System.out.println("server response: " + finalResponse);
       }  catch(Exception e){
           // error message if user enters a location that queryTrips() cannot find 
             System.out.println("location not available");
        }
        
   }
   
   // function that returns the interest value of a trip from the orchestrator, given specfifc userID and tripID 
   // function takes in userID value that was generated when the session started
   // request a query for list of trips associated with userID to the orchaestrator 
   // will return none/ error response from the orachestrator, if no trip has been added  prior 
   // if trips has been added prior, it will print list of trips asosicated with userID and promp the user to enter a number corresponding with specififc trip
   // which ever number user selects, the input will be used as an index to get the userID and tripID of a instance of trip from the list of trips.
   // the userId and tripID will be sent to orchestrator to return the interest value of the trip, assoicated with the user and tripID,
   // to present it, to the user
   
   static public void viewInterest(String userID) throws MalformedURLException, IOException {
        int number;
        Scanner sc = new Scanner(System.in);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
       
       
       // retrieves list of trips assoicated with the userID of the given/currently running session
       String resultFromQuery = queryUserTrips(userID);
       //System.out.println(resultFromQuery);
       
       try{
           
       Trip[] listOfTrips = gson.fromJson(resultFromQuery , Trip[].class);
        for (int i = 0; i < listOfTrips.length ; i++){
        
        //prints out all trips assoiciated with userID of the given session/currently running session
        System.out.println(i + " location:" + listOfTrips[i].getLocation() +" date:" + listOfTrips[i].getDate()+ 
                                    " weather:" + listOfTrips[i].getWeather() + "°C " );
        }  
                                
       System.out.println("\b");
       
       
       // asks user which one of their trips, they would like to check interest on
       do {
             System.out.println("Please select number corresponding to the trip");
           
            while (!sc.hasNextInt() ) {
                System.out.println("please enter a number ");
                sc.next(); 
            }
            number = sc.nextInt();
        } while (number > 0 && number > listOfTrips.length);

       
       // get userId and tripID from trip that the user wants to check interest level on
       String tripUserID = listOfTrips[number].getUserID();
       String tripTripID = listOfTrips[number].getTripID();
       
       
       // process parameter for http request 
       Map<String, String> parameters = new HashMap<>();
        parameters.put("userID", tripUserID );
        parameters.put("tripID", tripTripID);
        String response = "";
        
        String convertedParamsToString = "";
        
        StringBuilder mapAsString = new StringBuilder("");
      
        for (String key : parameters.keySet()) {
        mapAsString.append(key + "=" + parameters.get(key) + "&" );
         }
        convertedParamsToString = mapAsString.toString(); 
        convertedParamsToString  = convertedParamsToString.substring(0, convertedParamsToString.length() - 1);
        
        
        // calls orchestrator to return interestv value for a specfific trip 
        try{
        URL url = new URL("http://localhost:8080/myTripService/webresources/trip/CheckInterest?" + convertedParamsToString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        
        
        BufferedReader bfReader = new BufferedReader(new InputStreamReader( con.getInputStream()));
        
        
          
          String temp = null;
          while((temp = bfReader.readLine()) != null){
              response += temp;
          }
        } catch (Exception e){
            System.out.println("failed to view interest on trip");
        }
        
        
        
           //System.out.println(response);
           //converts JSON response to json object that can be parsed to retrieve value 
          JSONObject obj = new JSONObject(response);
          int interest = obj.getInt("interest");
          System.out.println("interest: " + interest);
          
        } catch (Exception e){
            // if no trips associated with userID this message will appear 
            System.out.println("You dont have any trips! Create a trip.");
        }  
        
   }
 

	

   
   

    
    
}
