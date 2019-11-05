package com.example.proj;

public class ThingsToDo {
    private long id;
    private String title;
    private String time;
    private Integer request_code;
    public ThingsToDo(){
    }
    public ThingsToDo(String title,String time,Integer request_code){
        this.title=title;
        this.time=time;
        this.request_code=request_code;
    }

    public long getId(){ return id; }

    public void setId(long id) { this.id=id; }

    public String getTitle() { return title; }

    public void setTitle(String name) { this.title = name; }

    public String getTime() { return time; }

    public void setTime(String lastname) { this.time = lastname; }

    public Integer getRequest_code() { return request_code; }

    public void setRequest_code(Integer request_code) { this.request_code = request_code; }

}

