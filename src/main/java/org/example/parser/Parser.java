/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.parser;

import org.example.Utils;
import org.example.type.AdvObject;
import org.example.type.Command;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Parser {

    private final Set<String> stopwords;

    public Parser(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    /* Metodo per trovare l'indice di un oggetto all'interno di una lista */
    private <T> int findIndex(List<T> items, String token, Predicate<T> matcher) {
        return IntStream.range(0, items.size())
                .filter(i -> {
                    T item = items.get(i);
                    return matcher.test(item) && (item instanceof Command ?
                            ((Command) item).getName().equals(token) || ((Command) item).getAlias().contains(token) :
                            ((AdvObject) item).getName().equals(token) || ((AdvObject) item).getAlias().contains(token));
                })
                .findFirst()
                .orElse(-1);
    }

    /* Metodo per controllare se il token è un comando, usando il metodo findIndex */
    private int checkForCommand(String token, List<Command> commands) {
        return findIndex(commands, token, command -> true);
    }

    /* Metodo per controllare se il token è un oggetto, usando il metodo findIndex */
    private int checkForObject(String token, List<AdvObject> objects) {
        return findIndex(objects, token, object -> true);
    }

    /* Metodo che restituisce ParserOutput in base al comando e agli oggetti trovati */
    public ParserOutput parse(String command, List<Command> commands, List<AdvObject> objects, List<AdvObject> inventory) {
        List<String> tokens = Utils.parseString(command, stopwords);
        if (!tokens.isEmpty()) {
            int ic = checkForCommand(tokens.get(0), commands);
            if (ic > -1) {
                if (tokens.size() > 1) {
                    int io = checkForObject(tokens.get(1), objects);
                    int ioinv = -1;
                    if (io < 0 && tokens.size() > 2) {
                        io = checkForObject(tokens.get(2), objects);
                    }
                    if (io < 0) {
                        ioinv = checkForObject(tokens.get(1), inventory);
                        if (ioinv < 0 && tokens.size() > 2) {
                            ioinv = checkForObject(tokens.get(2), inventory);
                        }
                    }
                    if (io > -1 && ioinv > -1) {
                        return new ParserOutput(commands.get(ic), objects.get(io), inventory.get(ioinv));
                    } else if (io > -1) {
                        return new ParserOutput(commands.get(ic), objects.get(io), null);
                    } else if (ioinv > -1) {
                        return new ParserOutput(commands.get(ic), null, inventory.get(ioinv));
                    } else {
                        return new ParserOutput(commands.get(ic), null, null);
                    }
                } else {
                    return new ParserOutput(commands.get(ic), null);
                }
            } else {
                return new ParserOutput(null, null);
            }
        } else {
            return null;
        }
    }

}
