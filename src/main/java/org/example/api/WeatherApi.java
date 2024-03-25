package org.example.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class WeatherApi implements Serializable {
    private String city;
    // Costruttore
    public WeatherApi() {


    }

    public boolean getWeatherData(String city) throws IOException {
        String apiKey = "12fe4119d3075943be8f74ad336dd645";


        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&lang=it";

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200 || city == null || city.isEmpty()) {
            System.out.println("Hai bevuto? Non esiste questo posto! *Si spegne*");
            return false;
        }
        String encodedCity;
        encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

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

        // Estrai la descrizione del meteo
        String weatherDescription = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

        // Estrai e stampa la temperatura convertita in Celsius
        double temperature = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble() - 273.15;
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedTemperature = df.format(temperature);

        int humidity = jsonResponse.getAsJsonObject("main").get("humidity").getAsInt();

        //coordinate
        double cordsLat = jsonResponse.getAsJsonObject("coord").get("lat").getAsDouble();
        double cordsLon = jsonResponse.getAsJsonObject("coord").get("lon").getAsDouble();

        //Stampa il messaggio con le informazioni
        System.out.println("<< Creazione tunnel quantico per " + cordsLat + "°N " + cordsLon + "°E, pianeta Terra, via Lattea, Universo 47B...");
        System.out.println("Collegamento completato, informazioni attuali di " + countryName + ":");
        System.out.println("Condizioni meteo: " + weatherDescription + ", Temperatura: " + formattedTemperature + "°C, Umidità: " + humidity + "%");

        return true;
    }
}
