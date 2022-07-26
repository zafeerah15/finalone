package sG.EDU.NP.MAD.friendsOnly.CHAT;

// setting the variables later required for the chat

public class Chat_List {


    private String mobile, name, message, date, time, mediaUrl;
    private boolean isMedia;

    public Chat_List(String mobile, String name, String message, String date, String time, Boolean isMedia, String mediaUrl) {
        this.mobile = mobile;
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
        this.isMedia = isMedia;
        this.mediaUrl = mediaUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsMedia() {
        return isMedia;
    }

    public String getMediaUrl() {
        return mediaUrl;
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
