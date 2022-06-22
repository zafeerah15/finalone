package sg.edu.np.mad.chatapp.messages;

public class MessagesList {

    private String name,phoneno,lastMessage, profilepicture, chatKey, userType, bio;

    private boolean granted;

    private int unseenMessages;

    public MessagesList(String name,String phoneno, String lastMessage, String profilepicture, int unseenMessages, String chatKey, Boolean granted, String userType, String bio) {
        this.name = name;
        this.phoneno = phoneno;
        this.lastMessage = lastMessage;
        this.profilepicture = profilepicture;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
        this.granted = granted;
        this.userType = userType;
        this.bio = bio;

    }
    public String getBio(){
        return bio;
    }

    public String getName() {
        return name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getProfilepicture(){
        return profilepicture;
    }

    public String getChatKey() {
        return chatKey;
    }

    public boolean isGranted() {
        return granted;
    }

    public String getUserType() {
        return userType;
    }
}
