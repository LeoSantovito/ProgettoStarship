package org.example.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Scanner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class WeatherApi implements Serializable {
    private String city;
    // Costruttore
    public WeatherApi() {


    }

    public void getWeatherData(String city) throws IOException {
        String apiKey = "12fe4119d3075943be8f74ad336dd645";


        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&lang=it";

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200 || city == null || city.isEmpty()) {
            System.out.println("Hai bevuto? Non esiste questa città");
            return;
        }
        String encodedCity;
        try {
            encodedCity = URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Errore durante la codifica del nome della città.");
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Analizza la risposta JSON
        JsonElement jsonElement = JsonParser.parseString(response.toString());
        JsonObject jsonResponse = jsonElement.getAsJsonObject();

        // Estrai e stampa le informazioni tradotte
        String countryName = jsonResponse.get("name").getAsString();
        System.out.println("Nome del paese: " + countryName);

        // Estrai la descrizione del meteo
        String weatherDescription = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
        System.out.println("Descrizione del meteo: " + weatherDescription);

        // Estrai e stampa la temperatura convertita in Celsius
        double temperature = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble() - 273.15;
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedTemperature = df.format(temperature);
        System.out.println("Temperatura: " + formattedTemperature + " °C");

        int humidity = jsonResponse.getAsJsonObject("main").get("humidity").getAsInt();
        System.out.println("Umidità: " + humidity + "%");

        //coordinate
        double cordsLat = jsonResponse.getAsJsonObject("coord").get("lat").getAsDouble();
        double cordsLon = jsonResponse.getAsJsonObject("coord").get("lon").getAsDouble();
        System.out.println("Cordinate latitudinale: " + cordsLat);
        System.out.println("Cordinate longitudinale: " + cordsLon);

    }





}
