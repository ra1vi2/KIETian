package ra1vi2.asdc.kietian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by jaihanuman on 15/3/16.
 */
public class SessionManager {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "KIETianPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_ROLL = "roll_no";
    public static final String KEY_SEM = "sem";

    public static final String KEY_SEC = "sec";
    public static final String KEY_PASS = "pass";






    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String roll_no,String sem,String sec,String pass){
        // Storing login value as TRUE
        Log.d("bhai","logging in");
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_ROLL, roll_no);
        editor.putString(KEY_SEM, sem);
        editor.putString(KEY_SEC, sec);
        editor.putString(KEY_PASS, pass);

        // commit changes
        editor.commit();
    }

    public void checkLogin() {

        // Check login status
        if (!this.isLoggedIn()) {
            Log.d("bhai","Not logged in");
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
        else
        {
            Log.d("bhai","logged in");
        }
    }

        public HashMap<String, String> getUserDetails(){
            HashMap<String, String> user = new HashMap<String, String>();
            // user name
            user.put(KEY_ROLL, pref.getString(KEY_ROLL, null));
            user.put(KEY_SEM, pref.getString(KEY_SEM, null));
            user.put(KEY_SEC, pref.getString(KEY_SEC, null));
            user.put(KEY_PASS, pref.getString(KEY_PASS, null));

            // return user
            return user;
        }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
