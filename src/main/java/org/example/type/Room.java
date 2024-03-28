/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.type;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Room implements Serializable {

    private final int id;

    private String name;

    private String description;

    private String look;

    private String intro;

    private String inaccessibleMessage;

    private boolean visible = true;

    private boolean visited = false;

    private boolean accessible = true;
    private int southId;
    private int northId;
    private int eastId;
    private int westId;

    private Room south = null;

    private Room north = null;

    private Room east = null;

    private Room west = null;
    
    private final List<AdvObject> objects=new ArrayList<>();

    public Room(int id) {
        this.id = id;
    }

    public Room(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean getAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public String getInaccessibleMessage() {
        return inaccessibleMessage;
    }

    public void setInaccessibleMessage(String inaccessibleMessage) {
        this.inaccessibleMessage = inaccessibleMessage;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Room getSouth() {
        return south;
    }

    public void setSouth(Room south) {
        this.south = south;
    }

    public Room getNorth() {
        return north;
    }

    public void setNorth(Room north) {
        this.north = north;
    }

    public Room getEast() {
        return east;
    }

    public void setEast(Room east) {
        this.east = east;
    }

    public Room getWest() {
        return west;
    }

    public void setWest(Room west) {
        this.west = west;
    }

    public List<AdvObject> getObjects() {
        return objects;
    }

    public List<AdvObject> getAllObjects() {
        List<AdvObject> allObjects = new ArrayList<>();
        allObjects.addAll(objects);
        for (AdvObject object : objects) {
            if (object.isContainer()) {
                allObjects.addAll(object.getObjectsList());
            }
        }
        return allObjects;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

    public int getSouthId() {
        return southId;
    }

    public void setSouthId(int southId) {
        this.southId = southId;
    }

    public int getNorthId() {
        return northId;
    }

    public void setNorthId(int northId) {
        this.northId = northId;
    }

    public int getEastId() {
        return eastId;
    }

    public void setEastId(int eastId) {
        this.eastId = eastId;
    }

    public int getWestId() {
        return westId;
    }

    public void setWestId(int westId) {
        this.westId = westId;
    }
    public static Room findRoomById(List<Room> rooms, int roomId) {
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                return room;
            }
        }
        return null; // Se la stanza con l'ID specificato non viene trovata
    }
}
