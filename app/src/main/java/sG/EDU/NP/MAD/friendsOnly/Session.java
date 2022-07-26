package sG.EDU.NP.MAD.friendsOnly;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }


    public void setusername(String username) {
        prefs.edit().putString("username", username).commit();
    }

    public void setprofilePic(String profilePic) {
        prefs.edit().putString("profilePic", profilePic).commit();
    }

    public String getprofilePic() {
        String profilePic = prefs.getString("profilePic",null);
        return profilePic;
    }


    public String getusename() {
        String username = prefs.getString("username",null);
        return username;
    }

    public void clearData() {
        prefs.edit().putString("username", "").commit();
        prefs.edit().putString("profilePic", "").commit();
    }


}
