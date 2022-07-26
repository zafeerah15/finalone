package sG.EDU.NP.MAD.friendsOnly.story;

public class StoryList {

    private String userId, username, prof_url, story_url, caption;
    private Long date;

    public StoryList(String userId, String username, String prof_url, String story_url, String caption ,Long date) {
        this.userId = userId;
        this.username = username;
        this.prof_url = prof_url;
        this.caption = caption;
        this.story_url = story_url;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getProf_url() {
        return prof_url;
    }

    public String getStory_url() {
        return story_url;
    }

    public String getCaption() {
        return caption;
    }

    public Long getDate() {
        return date;
    }
}
