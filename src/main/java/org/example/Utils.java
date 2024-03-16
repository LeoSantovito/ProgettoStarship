/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author pierpaolo
 */
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
        Type objectTypeList = TypeToken.getParameterized(List.class, objectType).getType();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            T[] objectArray = gson.fromJson(br, (Type) Array.newInstance(objectType, 0).getClass());
            return List.of(objectArray);
        }
    }

    public static void printFromFile(String filename, String playerName) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Sostituisci eventuali segnaposto per il nome del giocatore con il nome effettivo
                line = line.replace("{playerName}", playerName);
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

}
