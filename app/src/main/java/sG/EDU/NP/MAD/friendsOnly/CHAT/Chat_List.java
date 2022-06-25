package sG.EDU.NP.MAD.friendsOnly.CHAT;

public class Chat_List {


    private String mobile, name, message, date, time;

    public Chat_List(String mobile, String name, String message, String date, String time) {
        this.mobile = mobile;
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
