package com.example.startuppage;

public class discussion_dblink {
    private String title, desc, username;

    public discussion_dblink (String title, String desc, String username){
        this.title = title;
        this.desc = desc;
        this.username = username;
    }

    public discussion_dblink() {
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public String getDesc() {
        return desc;
    }
    public String getUsername() {
        return username;
    }
}
