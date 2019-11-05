package com.example.proj;

public class Movie {
    private String name;
    private int poster_id;

    public String getName() {return this.name;}
    public int getPoster_id() {return this.poster_id;}

    public Movie(String name, int poster_id)
    {
        this.name = name;
        this.poster_id = poster_id;
    }
}
