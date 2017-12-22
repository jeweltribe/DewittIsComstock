package com.example.varun.app4;


public class Movies {
    private int movieID;
    private String movieName;
    private String movieDate;
    private float movieRating;


    public Movies(int ID, String name, String date, float rating) {
        this.movieID = ID;
        this.movieName = name;
        this.movieDate = date;
        this.movieRating = rating;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int ID) {
        this.movieID = ID;
    }
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDate() {
        return movieDate;
    }

    public void setMovieDate(String movieDate) {
        this.movieDate = movieDate;
    }

    public float getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(int movieRating) {
        this.movieRating = movieRating;
    }
}
