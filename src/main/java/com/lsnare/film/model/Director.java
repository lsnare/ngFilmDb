package com.lsnare.film.model;

/**
 * Created by lucian.snare on 9/13/2015.
 */
public class Director {
    public String name;
    public String nameId;

    public Director(String name){
        this.name = name;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        String directorString = this.getName() + "\n" + this.getNameId() +"\n\n";
        return directorString;
    }
}
