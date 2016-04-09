package ra1vi2.asdc.kietian;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    //private static final int REQUEST_READ_CONTACTS = 0;
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    AlertDialogManager alert = new AlertDialogManager();

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
  /*  private static final String[] DUMMY_CREDENTIALS = new String[]{
            "1202910125:123456", "1202910124:ra1vi2"
    };
  */  private static String url_login = "http://10.42.0.1/get_login.php";
    private static String url_login2 = "http://10.42.0.1/fac_login.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PASS = "pass";
    private static final String TAG_SEM = "semester";
    private static final String TAG_SEC = "section";


    SessionManager session;

    // products JSONArray
    JSONArray pass = null;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private RadioGroup radiogroup;
    private int logintype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.roll_no);
        populateAutoComplete();

        session = new SessionManager(getApplicationContext());

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {


                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        radiogroup = (RadioGroup)findViewById(R.id.loginradio);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("bhai",""+checkedId);

                switch(checkedId)
                {
                    case 2131492984:
                    {
                        mEmailView.setHint("Roll No");
                        mEmailView.setInputType(InputType.TYPE_CLASS_NUMBER);

                        break;

                    }

                    case 2131492985:
                    {
                        mEmailView.setHint("Fac ID");
                        mEmailView.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;


                    }
                }
            }
        });
    }

    private void populateAutoComplete() {
         {

        }

         }

    /**
     * Callback received when a permissions request has been completed.
     */



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if(radiogroup.getCheckedRadioButtonId()==-1)
        {
           alert.showAlertDialog(LoginActivity.this,"Choose Login Type","Error",false);
            focusView = radiogroup;
            cancel=true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //logintype = radiogroup.getCheckedRadioButtonId();
          int  logintype2 = radiogroup.getCheckedRadioButtonId();
            Log.d("bhai", " radiowali" + logintype2);
            if(logintype2==2131492984)
            {
                logintype=1;
            }
            else
            {
                logintype=4;
            }

            showProgress(true);
            mAuthTask = new UserLoginTask(email, password,logintype);
            mAuthTask.execute((Void) null);
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if(email.startsWith("1")) {
            return (email.length() == 10);
        }

        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String password;
        private String sem;
        private String sec;
        private int mLogintype;
        private String status;


      /*  JSONParser jsonParser = new JSONParser();
*/
        UserLoginTask(String email, String password,int logintype) {
            mEmail = email;
            mPassword = password;
            mLogintype = logintype;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in..Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            // Simulate network access.
            ////////////////////////////////////////////////////////


            if(logintype==1) {
                List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                paramss.add(new BasicNameValuePair("roll_no", mEmail));
                // getting JSON string from URL

                JSONObject json = null;
                Log.d("bhai", String.valueOf(mLogintype));
                try {

                    json = jParser.makeHttpRequest(url_login, "GET", paramss);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "URL not Found",
                            Toast.LENGTH_SHORT).show();
                }

                // Check your log cat for JSON reponse
//            Log.d("Password: ", json.toString());

                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        // Getting Array of Products
                        pass = json.getJSONArray(TAG_PASS);
                        JSONObject c = pass.getJSONObject(0);


                        password = c.getString(TAG_PASS);
                        sem = c.getString(TAG_SEM);
                        sec = c.getString(TAG_SEC);
                        Log.d("Pass", password);
                        Log.d("Sem", sem);
                        Log.d("Sec", sec);
                        if (password.equals(mPassword)) {
                            return true;
                        }

                    } else {

                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alert.showAlertDialog(LoginActivity.this, "Could Not Login..", "Try Again", false);
                            }
                        });

                    }
                    return false;

                } catch (JSONException e) {

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(LoginActivity.this, "Internet Not Available..", "Try Again", false);
                        }
                    });
                    e.printStackTrace();
                }
            }
            else if (logintype==4)
            {
                List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                String memail2 = "'"+mEmail+"'";
                paramss.add(new BasicNameValuePair("fac_id", memail2));
                // getting JSON string from URL
                Log.d("bhai4",mEmail);

                JSONObject json = null;
                Log.d("bhai4", String.valueOf(logintype));
                try {

                    json = jParser.makeHttpRequest(url_login2, "GET", paramss);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "URL not Found",
                            Toast.LENGTH_SHORT).show();
                }

                // Check your log cat for JSON reponse
            Log.d("Password: ", json.toString());

                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        // Getting Array of Products
                        pass = json.getJSONArray(TAG_PASS);
                        JSONObject c = pass.getJSONObject(0);


                        password = c.getString(TAG_PASS);
                        status  = c.getString("status");

                        if (password.equals(mPassword)) {
                            return true;
                        }

                    } else {

                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alert.showAlertDialog(LoginActivity.this, "Could Not Login..", "Try Again", false);
                            }
                        });

                        return false;
                    }


                } catch (JSONException e) {

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(LoginActivity.this, "Internet Not Available..", "Try Again", false);
                        }
                    });
                    e.printStackTrace();
                }
            }

            return false;

            //////////////////////////////////////////////
              /*  for (String credential : DUMMY_CREDENTIALS) {
                    String[] pieces = credential.split(":");
                    if (pieces[0].equals(mEmail)) {

                        // Account exists, return true if the password matches.
                        return (pieces[1].equals(mPassword));
                    }

                }*/


            // TODO: register the new account here.

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            pDialog.dismiss();

            if (success) {
                if(logintype==1) {
                    session.createLoginSession(mEmail, sem, sec, password);
                    Intent intent = new Intent(getApplicationContext(), Student_main.class);
                    intent.putExtra("roll_no", mEmail);
                    intent.putExtra("sem", sem);
                    intent.putExtra("sec", sec);
                    intent.putExtra("pass", password);

                    startActivity(intent);
                }
                else{
                    sem="0";
                    sec="0";
                    session.createLoginSession(mEmail, sem, sec, password);
                    Intent intent = new Intent(getApplicationContext(), Faculty_main.class);
                    intent.putExtra("fac_id", mEmail);
                    intent.putExtra("pass", password);
                    intent.putExtra("status",status);

                    startActivity(intent);

                }
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

