package sG.EDU.NP.MAD.friendsOnly;

public class Radio {
    public String radioName, radioLink;
    public boolean isOffline;


    public String getRadioName() {
        return radioName;
    }

    public void setRadioName(String radioName) {
        this.radioName = radioName;
    }

    public String getRadioLink() {
        return radioLink;
    }

    public void setRadioLink(String radioLink) {
        this.radioLink = radioLink;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }
}

