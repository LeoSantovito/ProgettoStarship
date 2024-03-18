package org.example.Api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherApi implements Serializable {
       // Costruttore
       public WeatherApi() {

       }

       public void getWeatherData() throws IOException {
              String apiKey = "02f98a3e151d5752183dccb531023891";
              System.out.println("Inserisci la città di interesse: ");
              Scanner scanner = new Scanner(System.in);
              String city = scanner.nextLine();
              // String city = "Bari"; // Puoi specificare la città di interesse
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

              // Estrai e stampa le altre informazioni
              double temperature = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble() - 273.15;
              DecimalFormat df = new DecimalFormat("#.##");
              String formattedTemperature = df.format(temperature);
              System.out.println("Temperatura: " + formattedTemperature + " °C");

              int humidity = jsonResponse.getAsJsonObject("main").get("humidity").getAsInt();
              System.out.println("Umidità: " + humidity + "%");
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
                            return "nuvole sparse";
                     case "shower rain":
                            return "pioggia a rovesci";
                     case "rain":
                            return "pioggia";
                     case "thunderstorm":
                            return "temporale";
                     case "snow":
                            return "neve";
                     case "mist":
                            return "foschia";
                     case "moderate rain":
                            return "pioggia moderata";
                     default:
                            return description; // Se non c'è corrispondenza, restituisci la descrizione originale
              }
       }
}