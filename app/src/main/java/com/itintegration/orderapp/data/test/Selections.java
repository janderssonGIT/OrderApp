package com.itintegration.orderapp.data.test;

import java.util.HashMap;
import java.util.Map;

public class Selections {


    private long id;
    private String unit;
    private String comment;
    private int amount;
    private boolean addedToOrder;

    public Selections(long id, String unit, String comment, int amount, boolean addedToOrder) {
        this.unit = unit;
        this.comment = comment;
        this.amount = amount;
        this.addedToOrder = addedToOrder;
        this.id = id;
    }

    public Selections() {

    }

    public Map<Integer, Selections> createEmptyHashMap(int size) {
        Map<Integer, Selections> map = new HashMap<>();
        for(int i = 0; i < size; i++) {
            Selections selection = new Selections(0,"", "", 0, false);
            map.put(i, selection);
        }
        return map;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isAddedToOrder() {
        return addedToOrder;
    }

    public void setAddedToOrder(boolean addedToOrder) {
        this.addedToOrder = addedToOrder;
    }
}
