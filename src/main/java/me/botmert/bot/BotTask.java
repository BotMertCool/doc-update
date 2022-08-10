package me.botmert.bot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.entities.Activity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class BotTask implements Runnable {
    public void run() {
        try {
            Random random = new Random();
            String sURL;
            if (random.nextBoolean()) {
                sURL = "https://sheets.googleapis.com/v4/spreadsheets/1vW-nFbnR02F9BEnNPe5NBejHRGPt0QEGOYXLSePsC1k/values/Released?key=AIzaSyBoJi11OX24hzkg80RFs78GiRBsX-Oo4Ys";
            } else {
                sURL = "https://sheets.googleapis.com/v4/spreadsheets/1vW-nFbnR02F9BEnNPe5NBejHRGPt0QEGOYXLSePsC1k/values/Unreleased?key=AIzaSyBoJi11OX24hzkg80RFs78GiRBsX-Oo4Ys";
            }
    
            // Connect to the URL using java's native library
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();
    
            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
            JsonArray values = rootobj.get("values").getAsJsonArray(); //just grab the zipcode
            
            JsonArray song = (JsonArray) values.get(random.nextInt(values.size()));
            
            if (song.size() >= 8) {
                DiscordBot.getInstance().getClient().getPresence().setActivity(Activity.listening(song.get(1).getAsString().split("\n")[0]));
                System.out.println("Activity Changed");
            } else {
                run();
            }
    
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    
    }
}
