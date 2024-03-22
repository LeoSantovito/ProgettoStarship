package org.example.api;
import java.io.*;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class LocationApi {
    public static String getPublicIP() {
        try {
            URL url = new URL("https://api.ipify.org");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String ip = reader.readLine();
            reader.close();
            return ip;
        } catch (IOException e) {
            // Gestione dell'eccezione: stampa un messaggio di errore o logga l'errore
            e.printStackTrace();
            // Restituisce un valore di fallback, ad esempio "unknown"
            return "unknown";
        }
    }

    public static String getGeolocation() {
        try {
            // Effettua una richiesta GET al servizio di geolocalizzazione basato sull'IP
            String ipAddress = getPublicIP(); // Inserisci qui il tuo indirizzo IP se lo conosci, altrimenti lascialo vuoto
            URL url = new URL("http://ip-api.com/json/" + ipAddress);
            Scanner scanner = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.next());
            }
            scanner.close();

            // Analizza la risposta JSON
            JSONObject jsonObject = new JSONObject(response.toString());
            return jsonObject.getString("city");

        } catch (IOException e) {
            e.printStackTrace();
            return "unknown";
        }
    }
}
