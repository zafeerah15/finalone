package sG.EDU.NP.MAD.friendsOnly;


//To do task class
public class ToDo {
    public int id, status;
    private boolean isMedia;
    public String title, updateDate,mediaUrl;

    public ToDo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public Boolean getIsMedia() {
        return isMedia;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    public ToDo( Boolean isMedia, String mediaUrl) {

        this.isMedia = isMedia;
        this.mediaUrl = mediaUrl;
    }

}

