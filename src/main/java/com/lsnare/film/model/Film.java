package com.lsnare.film.model;
import java.util.List;


public class Film {

    public class Actor {
        public String actorId;
        public String actorName;
        public String character;
        public Boolean main;
        public String urlCharacter;
        public String urlPhoto;
        public String urlProfile;

        public Actor(){

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

    public class Director {
        public String name;
        public String nameId;

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

    public List<Actor> actors;
    public List<String> countries;
    public List<Director> directors;
    public List<String> filmingLocations;
    public List<String> genres;
    public String idIMDB;
    public List<String> languages;
    public String metascore;
    public String originalTitle;
    public String plot;
    public String rated;
    public String rating;
    public String releaseDate;
    public List<String> runtime;
    public String simplePlot;
    public String title;
    public String type;
    public String urlIMDB;
    public String urlPoster;
    public String votes;
    public List<Director> writers;
    public String year;

    public Film(String idIMDB, String title, String plot, String year){
        this.idIMDB = idIMDB;
        this.title = title;
        this.plot = plot;
        this.year = year;
    }

    public Film(){
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public List<String> getFilmingLocations() {
        return filmingLocations;
    }

    public void setFilmingLocations(List<String> filmingLocations) {
        this.filmingLocations = filmingLocations;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getIdIMDB() {
        return idIMDB;
    }

    public void setIdIMDB(String idIMDB) {
        this.idIMDB = idIMDB;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<String> getRuntime() {
        return runtime;
    }

    public void setRuntime(List<String> runtime) {
        this.runtime = runtime;
    }

    public String getSimplePlot() {
        return simplePlot;
    }

    public void setSimplePlot(String simplePlot) {
        this.simplePlot = simplePlot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrlIMDB() {
        return urlIMDB;
    }

    public void setUrlIMDB(String urlIMDB) {
        this.urlIMDB = urlIMDB;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public List<Director> getWriters() {
        return writers;
    }

    public void setWriters(List<Director> writers) {
        this.writers = writers;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String toString(){
        String filmString = this.getTitle() + "\n" + this.getPlot() + "\n\n" + "Cast: \n";
        for (Actor a : this.getActors()){
            filmString += a.toString();
        }
        filmString+="Directors: \n";
        for(Director d : this.getDirectors()){
            filmString += d.toString();
        }
        return filmString;
    }

}