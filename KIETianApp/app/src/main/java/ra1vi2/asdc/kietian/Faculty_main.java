package ra1vi2.asdc.kietian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Faculty_main extends AppCompatActivity {

    private String status;
    private String fac_id;
    private GetSec mAuthTasksec;
    private TakeClass mAuthTask;
    private static String url_sec = "http://10.42.0.1/get_sec.php";
    private static String url_take = "http://10.42.0.1/take_class.php";
    private String lectime;
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PASS = "pass";
    private static final String TAG_SEM = "semester";
    private static final String TAG_SEC = "section";

    private String sem = null;
    private String sec = null;
    JSONArray pass = null;
    AlertDialogManager alert = new AlertDialogManager();

    List<String> spinnerArray = new ArrayList<>();

    JSONParser jParser = new JSONParser();
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SessionManager session = new SessionManager(getApplicationContext());
        session.checkLogin();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Intent intent = this.getIntent();

        if (intent.hasExtra("fac_id")) {
            status = getIntent().getExtras().getString("status");
            fac_id = getIntent().getExtras().getString("fac_id");
        }

        TextView tvfac_id = (TextView) findViewById(R.id.fac_id);
        TextView tvstatus = (TextView) findViewById(R.id.status);
        spinner = (Spinner) findViewById(R.id.spinner);
        final Button button = (Button) findViewById(R.id.btntkclass);


        tvfac_id.setText(fac_id);
        tvstatus.setText(status);

        final Switch chngswitch = (Switch) findViewById(R.id.switch1);

        chngswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    spinner.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    getsec();
                } else if (!isChecked) {
                    spinner.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);

                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String semsec = parent.getItemAtPosition(position).toString();
                String semsecarr[] = semsec.split("-");
                sem = semsecarr[0].trim();
                sec = semsecarr[1].trim();

                Log.d("bhai sem", sem + "" + sec);
                button.setEnabled(true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                button.setEnabled(false);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tkclass();
            }
        });

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("HH:MM");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        String localTime = date.format(currentLocalTime);
        Log.d("bhai time is", localTime);
        String time[] = localTime.split(":");
        String hour = time[0];
        String minute = time[1];
        Log.d("bhai hour", hour + minute);


        switch (hour) {
            case "9": {
                if (Integer.parseInt(minute) < 40) {
                    lectime = "09:00:00";
                } else {
                    lectime = "09:40:00";
                }
                break;
            }

            case "10": {
                if (Integer.parseInt(minute) < 30) {
                    lectime = "09:40:00";
                } else {
                    lectime = "10:30:00";
                }
                break;
            }

            case "11": {
                if (Integer.parseInt(minute) < 20) {
                    lectime = "10:30:00";
                } else {
                    lectime = "11:20:00";
                }
                break;

            }

            case "12": {
                if (Integer.parseInt(minute) < 10) {
                    lectime = "11:20:00";
                } else {
                    lectime = "NA";
                }
                break;
            }

            case "13": {

                if (Integer.parseInt(minute) < 59) {
                    lectime = "01:20:00";
                } else {
                    lectime = "02:10:00";
                }
                break;
            }

            case "14": {

                if (Integer.parseInt(minute) < 50) {
                    lectime = "02:10:00";
                } else {
                    lectime = "03:00:00";
                }
                break;
            }

            case "15": {

                if (Integer.parseInt(minute) < 40) {
                    lectime = "03:00:00";
                } else {
                    lectime = "03:40:00";
                }
                break;
            }

            case "16": {

                if (Integer.parseInt(minute) < 10) {
                    lectime = "03:40:00";
                } else {
                    lectime = "NA";
                }
                break;
            }
        }
        lectime = "10:30:00";


    }

    private void getsec() {
        if (mAuthTasksec != null) {
            return;
        } else {
            mAuthTasksec = new GetSec(fac_id, lectime);
            mAuthTasksec.execute((Void) null);
        }
    }

    private void tkclass() {
        if (mAuthTask != null) {
            return;
        } else {
            mAuthTask = new TakeClass(fac_id, sem, sec);
            mAuthTask.execute((Void) null);
        }
    }


    public class GetSec extends AsyncTask<Void, Void, Boolean>

    {

        private String mFac_id;
        private String mLectime;

        GetSec(String fac_id, String lectime) {
            mFac_id = fac_id.trim();
            mLectime = lectime.trim();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Faculty_main.this);
            pDialog.setMessage("Loading Sec Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            List<NameValuePair> paramss = new ArrayList<NameValuePair>();
            paramss.add(new BasicNameValuePair("fac_id", mFac_id));
            paramss.add(new BasicNameValuePair("lectime", mLectime));

            JSONObject json = jParser.makeHttpRequest(url_sec, "GET", paramss);

            Log.d("facid", mFac_id);
            Log.d("time", lectime);
            Log.d("Total Data: ", json.toString());
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    pass = json.getJSONArray(TAG_PASS);
                    for (int i = 0; i < pass.length(); i++) {
                        JSONObject c = pass.getJSONObject(i);
                        String sem = c.getString(TAG_SEM);
                        String sec = c.getString(TAG_SEC);

                        if (sem != null) {
                            String semsec = sem + " - " + sec;
                            spinnerArray.add(semsec);


                        } else {
                            spinnerArray.add("No More Free Classes");
                        }
                    }

                    Faculty_main.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Faculty_main.this, android.R.layout.simple_spinner_item, spinnerArray);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinner.setAdapter(adapter);
                        }
                    });

                } else if (success == 2) {
                    pass = json.getJSONArray(TAG_PASS);
                    JSONObject c = pass.getJSONObject(0);
                    String msg = c.getString("message");
                    spinnerArray.add(msg);

                    Faculty_main.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Faculty_main.this, android.R.layout.simple_spinner_item, spinnerArray);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinner.setAdapter(adapter);

                            Button button = (Button) findViewById(R.id.btntkclass);
                            button.setEnabled(false);
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
            mAuthTasksec = null;

            pDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mAuthTasksec = null;
        }


    }

    public class TakeClass extends AsyncTask<Void, Void, Boolean>

    {

        private String mSem;
        private String mSec;
        private String mFac_id;

        public TakeClass(String fac_id, String sem, String sec) {
            mFac_id = fac_id.trim();
            mSem = sem.trim();
            mSec = sec.trim();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Faculty_main.this);
            pDialog.setMessage("Updating Class Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String semsec = sem+"-"+sec;
            List<NameValuePair> paramss = new ArrayList<NameValuePair>();
            paramss.add(new BasicNameValuePair("fac_id", mFac_id));
            paramss.add(new BasicNameValuePair("sem", mSem));
            paramss.add(new BasicNameValuePair("sec", mSec));
            paramss.add(new BasicNameValuePair("semsec", semsec));


            JSONObject json = jParser.makeHttpRequest(url_take, "GET", paramss);

            Log.d("facid", mFac_id);

            Log.d("Total Data: ", json.toString());
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    Faculty_main.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Class Taken Successfully...!!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else
                {
                    Faculty_main.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(Faculty_main.this, "Could Not Register..", "Try Again", false);

                        }
                    });
                    return false;

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

                spinnerArray.clear();

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
