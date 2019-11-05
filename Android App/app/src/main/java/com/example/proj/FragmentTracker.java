package com.example.proj;

public interface FragmentTracker {
    public void fragmentVisible(String s);
    public void goNext();
    public void goBack();
    public void saveNameAndLastName(String name, String lname);
    public void saveNickname(String nickname);
    public void saveColor(String color);
    public void saveAnimal(String animal);
    public void saveMovie(String movie);
    public void finished();
}
