/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package externalapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author matt
 */
public class ExternalAPIs {
    // this class contains 2 external apis 
    // this function requests weather of location 
        static public double getWeatherForecast(String location) throws IOException{
        
        // format the query using given location
        String response = "";
        final String API_KEY = "fcd783c273ae4a934d6574d954f62c41";
        String LOCATION = location;
        String URL = "http://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&appid=" + API_KEY + "&units=metric";
        
        // request from server
        try{
        URL url = new URL(URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        
        // read response
        BufferedReader bfReader = new BufferedReader(new InputStreamReader( con.getInputStream()));
       
       
        String temp = "";
        while((temp = bfReader.readLine()) != null){
               response += temp;
          }
        
        
       
       
        } catch (IOException e){
            e.printStackTrace();
        }
       // parse json response to extract temperature in the location 
         JSONObject obj = new JSONObject(response);
         JSONObject obj2 = obj.getJSONObject("main");
        
     
        double temperature = (double)obj2.get("temp");
     // return temperature value 
         return temperature;
     }
        
        
        
     // this function gets 1 random UUID from external API and returns the value 
     static public String getRandomID() throws IOException{
         
      
        final String  request = "{\n" +
        "    \"jsonrpc\": \"2.0\",\n" +
        "    \"method\": \"generateUUIDs\",\n" +
        "    \"params\": {\n" +
        "        \"apiKey\": \"aff70036-002f-46d8-80a4-4e780a371964\",\n" +
        "        \"n\": 1\n" +
        "    },\n" +
        "    \"id\": 15998\n" +
        "}";
        
      String response = "";
      
         
      try{ 
        // send request to external api 
        URL url = new URL("https://api.random.org/json-rpc/4/invoke");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("content-Type","application/json");
        con.setDoOutput(true);
        
        OutputStream os = con.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os,"UTF-8");
        osw.write(request);
        osw.flush();
        osw.close();
        os.close();
        
        con.connect();
        
        
        
       // read response
        BufferedReader bfReader = new BufferedReader(new InputStreamReader( con.getInputStream()));
       
        String temp ;
        while((temp = bfReader.readLine()) != null){
              response = response + temp;
          }
        
        
        
     } catch(Exception e){
        e.printStackTrace();
       } 
      
    // parse the response to get the data/ UUID value from a nested json obejct
    JSONObject jsonObject = new JSONObject(response);
    JSONObject myResponse = jsonObject.getJSONObject("result");
    JSONObject mySecondResponse = myResponse.getJSONObject("random");
    JSONArray dataArray = (JSONArray) mySecondResponse.get("data");

    ArrayList<String> list = new ArrayList<String>();

    for(int i=0; i<dataArray.length(); i++){
        list.add(dataArray.getString(i).toString());
    }
       
        JSONObject dataID = new JSONObject();
        dataID.put("UUID",list.get(0));
        
        // return UUID 
        return dataID.toString();
        
    }
        
        
        
   }   
        
    

