package org.example.Api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class WeatherApi implements Serializable {
    // Costruttore
    public WeatherApi() {

    }

    public void getWeatherData() throws IOException {
        String apiKey = "12fe4119d3075943be8f74ad336dd645";

        System.out.println("Dove vuoi andare?: \n");
        Scanner scanner = new Scanner(System.in);
        String city = scanner.nextLine(); // String city = "Bari"; // Puoi specificare la città di interesse
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

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

        // Estrai la descrizione del meteo e traduci in italiano
        String weatherDescription = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
        String italianWeatherDescription = translateWeatherDescription(weatherDescription);
        System.out.println("Descrizione del meteo: " + italianWeatherDescription);

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

    // Metodo per tradurre la descrizione del meteo in italiano
    private String translateWeatherDescription(String description) {
        switch (description) {
            case "clear sky":
                return "cielo sereno";
            case "few clouds":
                return "poche nuvole";
            case "scattered clouds":
                return "nuvole sparse";
            case "broken clouds":
                return "nuvole irregolari";
            case "overcast clouds":
                return "nuvoloso";
            case "shower rain":
                return "pioggia a rovesci";
            case "rain":
                return "pioggia";
            case "thunderstorm":
                return "temporale";
            case "snow":
                return "neve";
            case "mist":
                return "nebbia";
            case "smoke":
                return "fumo";
            case "haze":
                return "foschia";
            case "light rain":
            case "light intensity drizzle":
                return "pioggia leggera";
            case "fog":
                return "nebbia";
            case "sand":
                return "sabbia";
            case "dust":
                return "polvere";
            case "ash":
                return "cenere";
            case "squall":
                return "tormenta";
            case "tornado":
                return "tornado";
            case "volcanic ash":
                return "cenere vulcanica";
            case "moderate rain":
                return "pioggia moderata";
            default:
                return description; // Se non c'è corrispondenza, restituisci la descrizione originale
        }
    }

}
