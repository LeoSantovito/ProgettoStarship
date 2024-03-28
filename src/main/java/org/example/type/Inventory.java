/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class Inventory implements Serializable {

    private List<AdvObject> list;

    public Inventory(){
        list = new ArrayList<>();
    }

    public List<AdvObject> getList() {
        return list;
    }

    public void setList(List<AdvObject> list) {
        this.list = list;
    }

    public void add(AdvObject o) {
        list.add(o);
    }

    public void remove(AdvObject o) {
        list.remove(o);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
