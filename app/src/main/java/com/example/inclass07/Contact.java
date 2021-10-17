package com.example.inclass07;

import java.io.Serializable;

public class Contact implements Serializable {
    String id, Name, Email, Type, Number;

    public Contact(String str) {
        String[] data = str.split(",");
        id = data[0];
        Name = data[1];
        Email = data[2];
        Number = data[3];
        Type = data[4];
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getType() {
        return Type;
    }

    public String getNumber() {
        return Number;
    }

    public String getId() {
        return id;
    }
}
