package com.example.proj;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonInfo implements Parcelable {
    private String name, lastname, nickname, color, animal, movie;
    public PersonInfo() {
        name="";
        lastname="";
        nickname="";
        color="";
        animal="";
        movie="";
    }
    public PersonInfo(String n, String l, String nn, String c, String a, String m) {
        name=n;
        lastname=l;
        nickname=nn;
        color=c;
        animal=a;
        movie=m;
    }
    public PersonInfo(Parcel in) {
        String[] data = new String[6];
        in.readStringArray(data);
        name = data[0];
        lastname = data[1];
        nickname = data[2];
        color = data[3];
        animal = data[4];
        movie = data[5];
    }
    public void setName(String n) { name=n; }
    public void setLastname(String l) { lastname=l; }
    public void setNickname(String nn) { nickname=nn; }
    public void setColor(String c) { color=c; }
    public void setAnimal(String a) { animal=a; }
    public void setMovie(String m) { movie=m; }
    public String getName() { return name; }
    public String getLastname() { return lastname; }
    public String getNickname() { return nickname; }
    public String getColor() { return color; }
    public String getAnimal() { return animal; }
    public String getMovie() { return movie; }
    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                name, lastname, nickname, color, animal, movie
        });
    }
    public static final Parcelable.Creator<PersonInfo>CREATOR = new Parcelable.Creator<PersonInfo>() {
        @Override
        public PersonInfo createFromParcel(Parcel parcel) { return new PersonInfo(parcel); }

        @Override
        public PersonInfo[] newArray(int i) { return new PersonInfo[i]; }
    };
}

