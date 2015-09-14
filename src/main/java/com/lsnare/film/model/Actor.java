package com.lsnare.film.model;

/**
 * Created by lucian.snare on 9/13/2015.
 */
public class Actor {
    public String actorId;
    public String actorName;
    public String character;
    public Boolean main;
    public String urlCharacter;
    public String urlPhoto;
    public String urlProfile;

    public Actor(String actorName){
        this.actorName = actorName;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public String getUrlCharacter() {
        return urlCharacter;
    }

    public void setUrlCharacter(String urlCharacter) {
        this.urlCharacter = urlCharacter;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }

    public String toString(){
        String actorString = "* " + this.getActorName() + "\n  " + this.getActorId() + "\n";
        return actorString;
    }
}