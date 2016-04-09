package ra1vi2.asdc.kietian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Profile_change_stdnt extends AppCompatActivity {

    private ProgressDialog pDialog;

    SessionManager session;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    private static String url_login = "http://10.42.0.1/update_pass.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    //private static final String TAG_PASS = "pass";
    AlertDialogManager alert = new AlertDialogManager();



    private ChangePassTask mAuthTask = null;


    // products JSONArray
    //JSONArray pass = null;
    private String roll_no = null;
    private String sem = null;
    private String sec = null;
    private String password = null;
    private String strchngpass = null;
    private String strchngpasscnfrm = null;
    TextView temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change_stdnt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
      final  Button chngebutton = (Button) findViewById(R.id.chngpassbutton);
      final  EditText editchngpass = (EditText) findViewById(R.id.etchngpass);
      final  EditText editchngpasscnfrm = (EditText) findViewById(R.id.etchngpasscnfrm);
         temp = (TextView) findViewById(R.id.textView4);
        final TextView newpasstext = (TextView) findViewById(R.id.textView2);
        final TextView cnewpasstext = (TextView) findViewById(R.id.textView3);

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editchngpass.isFocused())
                {
                    editchngpass.setVisibility(View.INVISIBLE);
                    editchngpasscnfrm.setVisibility(View.INVISIBLE);
                    newpasstext.setVisibility(View.INVISIBLE);
                    cnewpasstext.setVisibility(View.INVISIBLE);
                    chngebutton.setVisibility(View.INVISIBLE);

                }
                editchngpass.setVisibility(View.VISIBLE);
                editchngpasscnfrm.setVisibility(View.VISIBLE);
                newpasstext.setVisibility(View.VISIBLE);
                cnewpasstext.setVisibility(View.VISIBLE);
                chngebutton.setVisibility(View.VISIBLE);
            }
        });

        roll_no = getIntent().getExtras().getString("roll_no");
        sem = getIntent().getExtras().getString("sem");
        sec = getIntent().getExtras().getString("sec");
        password = getIntent().getExtras().getString("pass");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.prof_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SendMessage.class);
                intent.putExtra("roll_no", roll_no);
                intent.putExtra("sem", sem);
                intent.putExtra("sec", sec);
                startActivity(intent);
            }
        });
/*

        ImageButton logout = (ImageButton) findViewById(R.id.logout2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });
*/


       /* Log.d("Sem", sem);
        Log.d("Sec", sec);
*/
        TextView roll = (TextView) findViewById(R.id.roll_no_id);
        roll.setText(roll_no);

        TextView seme = (TextView) findViewById(R.id.profile_sem);
        seme.setText(sem);
        TextView sect = (TextView) findViewById(R.id.profile_sec);
        sect.setText(sec);

        chngebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strchngpass = editchngpass.getText().toString();
                strchngpasscnfrm = editchngpasscnfrm.getText().toString();


                 if(strchngpass==null || strchngpasscnfrm==null)
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Password",
                            Toast.LENGTH_SHORT).show();

                }
                if (!(strchngpass.equals(strchngpasscnfrm))) {
                    Toast.makeText(getApplicationContext(), "Entered Passwords Do not Match",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    chngpassword();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_profile_chng_stdnt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuchngpass:
                temp.setVisibility(View.VISIBLE);
                return true;
            case R.id.menulogout:
                session.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }


    private void chngpassword() {
        if (mAuthTask != null) {
            return;
        } else {
            mAuthTask = new ChangePassTask(roll_no, strchngpass);
            mAuthTask.execute((Void) null);
        }
    }

        public class ChangePassTask extends AsyncTask<Void, Void, Boolean>

        {

            private final String mRollno;
            private final String mPass;
            private String detail;

            ChangePassTask(String roll_no, String strchngpass) {
                mRollno = roll_no;
                mPass = strchngpass;

            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(Profile_change_stdnt.this);
                pDialog.setMessage("Updating Password...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }


            @Override
            protected Boolean doInBackground(Void... params) {
                List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                //ArrayList<String> classdetails = new ArrayList<String>();
                paramss.add(new BasicNameValuePair("roll_no", mRollno));
                paramss.add(new BasicNameValuePair("chngpass", mPass));

                // getting JSON string from URL

                JSONObject json = jParser.makeHttpRequest(url_login, "GET", paramss);
                Log.d("Total Data: ", json.toString());

                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        // Getting Array of Products
                       /* */
                        Profile_change_stdnt.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Your Password Has Been Changed Successfully...!!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                    else
                    {
                        Profile_change_stdnt.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alert.showAlertDialog(Profile_change_stdnt.this, "Couldn't Update..", "Try Again", false);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                mAuthTask = null;

                pDialog.dismiss();
            }

            @Override
            protected void onCancelled() {
                mAuthTask = null;
            }

        }
    }
