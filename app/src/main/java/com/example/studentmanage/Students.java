package com.example.studentmanage;

import java.io.Serializable;

public class Students implements Serializable {
    private String id;
    private String Name;
    private String classS;
    private String birth;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getClassS() {
        return classS;
    }

    public void setClassS(String classS) {
        this.classS = classS;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return Name + " : " + birth;
    }
}
