package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.type.AdvObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* Classe di utilità che contiene metodi pubblici per la gestione di file e oggetti. */

public class Utils {

    public static Set<String> loadFileListInSet(File file) throws IOException {
        Set<String> set = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.ready()) {
            set.add(reader.readLine().trim().toLowerCase());
        }
        reader.close();
        return set;
    }

    public static List<String> parseString(String string, Set<String> stopwords) {
        List<String> tokens = new ArrayList<>();
        String[] split = string.toLowerCase().split("\\s+");
        for (String t : split) {
            if (!stopwords.contains(t)) {
                tokens.add(t);
            }
        }
        return tokens;
    }

    /* Metodo per serializzare un oggetto. */
    public static byte[] serializeObject(Object obj) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }

    /* Metodo per deserializzare un oggetto. */
    public static Object deserializeObject(byte[] data) throws Exception {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        }
    }

    public static <T> List<T> loadObjectsFromFile(String filePath, Class<T> objectType) throws IOException {
        Gson gson = new Gson();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            T[] objectArray = gson.fromJson(br, (Type) Array.newInstance(objectType, 0).getClass());
            return List.of(objectArray);
        }
    }

    public static void printFromFilePlaceholder(String filename, String placeholder) {
        try (Scanner scanner = new Scanner(new File(filename), StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Sostituisci eventuali segnaposto con la stringa passata
                line = line.replace("{placeholder}", placeholder);
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file " + filename);
        }
    }

    public static void printFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename), StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file " + filename);
        }
    }

    public static void waitForEnter() {
        System.out.println("Premi invio per continuare...");
        try {
            System.in.read();
            System.in.skip(System.in.available()); // Pulisce il buffer di input
        } catch (IOException e) {
            System.err.println("Errore durante la lettura dell'input: " + e.getMessage());
        }
    }

    public static AdvObject findObjectById(List<AdvObject> objectsList, int id) {
        for (AdvObject object : objectsList) {
            if (object.getId() == id) {
                return object;
            }
        }
        return null;
    }

    /* Stampa il tempo di gioco. */
    public static String printGameTime(int totalSeconds){
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        String printHours = hours == 1 ? "ora" : "ore";
        String printMinutes = minutes == 1 ? "minuto" : "minuti";
        String printSeconds = seconds == 1 ? "secondo" : "secondi";

        if(hours == 0 && minutes == 0){
            return seconds + " " + printSeconds;
        } else if(hours == 0){
            return minutes + " " + printMinutes + " e " + seconds + " " + printSeconds;
        } else {
            return hours + " " + printHours + ", " + minutes + " " + printMinutes + " e " + seconds + " " + printSeconds;
        }
    }

}
