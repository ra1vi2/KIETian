package ra1vi2.asdc.kietian;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Student_main extends AppCompatActivity {

    private String roll_no = null;
    private String sem = null;
    private String sec = null;
    private ProgressDialog pDialog;
    private String password=null;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    private static String url_login = "http://10.42.0.1/get_class.php";
    private static String url_login2 = "http://10.42.0.1/get_att_sub.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PASS = "pass";
    private static final String TAG_Sub = "subject";
    private static final String TAG_facid = "fac_id";
    private static final String TAG_time = "time";
    private static final String TAG_Att = "sub_att";


    private GetclassTask mAuthTask = null;


    // products JSONArray
    JSONArray pass = null;
    JSONArray passs = null;

    SessionManager session;
    AlertDialogManager alert = new AlertDialogManager();

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
            //Log.d("bhai", "seesion check in done");



        /*if(!session.isLoggedIn())
        {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerss, new ClassesFragment())
                    .commit();
        }



        Intent intent = this.getIntent();


        if(intent.hasExtra("roll_no")) {
            Log.d("Intent NOt NUll","Not");
            roll_no = getIntent().getExtras().getString("roll_no");
            sem = getIntent().getExtras().getString("sem");
            sec = getIntent().getExtras().getString("sec");
            password = getIntent().getExtras().getString("pass");
        }


       else  {
            Log.d("Intent","Null");
            HashMap<String, String> user = session.getUserDetails();
            roll_no = user.get(SessionManager.KEY_ROLL);
            sem = user.get(SessionManager.KEY_SEM);
            sec = user.get(SessionManager.KEY_SEC);
            password = user.get(SessionManager.KEY_PASS);

        }


        if(roll_no == null)
        {
            Log.d("bhai","saved");
        }
        else if(roll_no.startsWith("f"))
        {
            session.logoutUser();
        }
        else{}



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
*/
                Intent intent = new Intent(getApplicationContext(), SendMessage.class);
                intent.putExtra("roll_no", roll_no);
                intent.putExtra("sem", sem);
                intent.putExtra("sec", sec);
                startActivity(intent);

            }
        });


        Button clas = (Button) findViewById(R.id.button2);

        clas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getclass();
            }
        });




       /* ImageButton prof = (ImageButton) findViewById(R.id.imageButton);
        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Profile_change_stdnt.class);
                intent.putExtra("roll_no", roll_no);
                intent.putExtra("sem", sem);
                intent.putExtra("sec", sec);
                intent.putExtra("pass", password);
                *//*Log.d("Sem", sem);
                Log.d("Sec", sec);
                *//*
                startActivity(intent);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_student_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.viewprofile:
                Intent intent = new Intent(getApplicationContext(), Profile_change_stdnt.class);
                intent.putExtra("roll_no", roll_no);
                intent.putExtra("sem", sem);
                intent.putExtra("sec", sec);
                intent.putExtra("pass", password);
                /*Log.d("Sem", sem);
                Log.d("Sec", sec);
                */
                startActivity(intent);
                return true;

            case R.id.findteacher:
                Intent intent2 = new Intent(getApplicationContext(),FindTeacher.class);
                        startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getclass() {
        if (mAuthTask != null) {
            return;
        } else {
            mAuthTask = new GetclassTask(sem, sec,roll_no);
            mAuthTask.execute((Void) null);
        }
    }


    public class GetclassTask extends AsyncTask<Void, Void, Boolean>

    {

        private final String mSem;
        private final String mSec;
        private final String roll;
        private String detail;
        private String att = "NA";
        private String sub;
        private String fac;
        private String time;

        GetclassTask(String sem, String sec,String roll_no) {
        mSem = sem;
        mSec = sec;
            roll = roll_no;

    }


        @Override
        protected void onPreExecute () {
        super.onPreExecute();
            pDialog = new ProgressDialog(Student_main.this);
            pDialog.setMessage("Getting Class Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
    }

        @Override
        protected Boolean doInBackground (Void...params){
            List<NameValuePair> paramss = new ArrayList<NameValuePair>();
            ArrayList<String> classdetails = new ArrayList<String>();
            paramss.add(new BasicNameValuePair("sem",mSem));
            paramss.add(new BasicNameValuePair("sec",mSec));
            List<NameValuePair> paramsss = new ArrayList<NameValuePair>();



            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_login, "GET", paramss);
           // Log.d("Total Data: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Getting Array of Products
                    pass = json.getJSONArray(TAG_PASS);
                    for (int i = 0; i < pass.length(); i++) {
                        JSONObject c = pass.getJSONObject(i);
                        sub = c.getString(TAG_Sub);
                         fac = c.getString(TAG_facid);
                         time = c.getString(TAG_time);
/*/*
                        Log.d("bhai Sub",sub);
                        Log.d("bhai Roll",roll);*/
                        paramsss.add(new BasicNameValuePair("sub", sub));
                        paramsss.add(new BasicNameValuePair("roll", roll));
                        JSONObject json2 = jParser.makeHttpRequest(url_login2, "GET", paramsss);
                        /*Log.d("call lg gyi",roll);
                        Log.d("Total Data: ", json2.toString());
*/
                        try {
                            int successs = json2.getInt(TAG_SUCCESS);
                            if (successs == 1) {
                              //  Log.d("sucesss", sub);
                                // Getting Array of Products
                                passs = json2.getJSONArray(TAG_PASS);
                                JSONObject d = passs.getJSONObject(0);

                              // if( d.getString(TAG_Att) != null){
                                 att  = d.getString(TAG_Att);
                                Log.d("att",att);
                               //}

                                detail = sub + "                   " + fac + "            " + time + "   "+att;
                                classdetails.add(i,detail);
                            }
                            else{
                                detail = sub + "                   " + fac + "            " + time + "   "+att;
                                classdetails.add(i,detail);
                               // Log.d("pta nni",sub);
                                }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("classdetails" , classdetails);
// set Fragmentclass Arguments
                    ClassesFragment fragobj = new ClassesFragment();
                    fragobj.setArguments(bundle);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.containerss, fragobj);
                    transaction.commit();
                }
                else
                {
                    Student_main.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(Student_main.this, "Couldn't fetch..", "Try Again", false);
                        }
                    });

                }
            }catch (JSONException e) {
                Student_main.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alert.showAlertDialog(Student_main.this, "Internet Not Working..", "Try Again", false);
                    }
                });
                e.printStackTrace();
            }


            return true;
    }

        @Override
        protected void onPostExecute ( final Boolean success){
            mAuthTask = null;

            pDialog.dismiss();
    }

        @Override
        protected void onCancelled () {
            mAuthTask = null;
    }

    }
}