package ra1vi2.asdc.kietian;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindTeacher extends AppCompatActivity {

    TextView ffsem;
    TextView ffsec;
    TextView ffroom;
    TextView ffsem16;
    TextView ffsem17;
    TextView ffsem18;
    TextView ffsem19;
    TextView ffsem20;
    TextView ffsem21;


    EditText ffed;
    Button btnff;

    private ProgressDialog pDialog;

    private String fac_id;

    private FindFaculty mAuthTask = null;

    JSONArray pass = null;

    AlertDialogManager alert = new AlertDialogManager();

    JSONParser jParser = new JSONParser();
    private static String url_login = "http://10.42.0.1/find_fac.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PASS = "pass";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_teacher);

        ffed = (EditText) findViewById(R.id.ffed);

        ffsem16 = (TextView) findViewById(R.id.textView16);
        ffsem17 = (TextView) findViewById(R.id.textView17);
        ffsem18 = (TextView) findViewById(R.id.textView18);
        ffsem19 = (TextView) findViewById(R.id.ffsem);
        ffsem20 = (TextView) findViewById(R.id.sssec);
        ffsem21 = (TextView) findViewById(R.id.ffroom);

        btnff = (Button) findViewById(R.id.btnfind);

        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fac_id = ffed.getText().toString();
                findtchr();
            }
        });


    }

    private void findtchr() {
        if (mAuthTask != null) {
            return;
        } else {
            mAuthTask = new FindFaculty(fac_id);
            mAuthTask.execute((Void) null);
        }
    }

    public class FindFaculty extends AsyncTask<Void, Void, Boolean>

    {
        private final String mFac_id;
        private String sem="-";
        private String sec="-";
        private  String currsec;
        private String roomno="Staff Room";

        FindFaculty(String fac_id) {
            mFac_id = fac_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FindTeacher.this);
            pDialog.setMessage("Getting Class Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> paramss = new ArrayList<NameValuePair>();
            paramss.add(new BasicNameValuePair("fac_id", mFac_id));

            JSONObject json = jParser.makeHttpRequest(url_login, "GET", paramss);
            // Log.d("Total Data: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Getting Array of Products
                    pass = json.getJSONArray(TAG_PASS);
                    JSONObject c = pass.getJSONObject(0);
                    currsec = c.getString("currsec");
                    String[] as = currsec.split("-");


                    if(currsec=="null")
                    {
                        sem="-";
                        sec="-";
                        roomno="Staff Room";
                    }

                    else {
                        sem = as[0];
                        sec = as[1];
                        roomno = "NA";
                    }


                    FindTeacher.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ffsem16.setVisibility(View.VISIBLE);
                            ffsem17.setVisibility(View.VISIBLE);
                            ffsem18.setVisibility(View.VISIBLE);

                            ffsem19.setVisibility(View.VISIBLE);
                            ffsem20.setVisibility(View.VISIBLE);
                            ffsem21.setVisibility(View.VISIBLE);

                            ffsem19.setText(sem);
                            ffsem20.setText(sec);
                            ffsem21.setText(roomno);

                        }
                    });


                    return true;
                } else {
                    FindTeacher.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(FindTeacher.this, "Could Not Find..", "Try Again", false);
                        }
                    });
                    return false;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            pDialog.dismiss();

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            FindTeacher.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ffed.setText("");
                          }
            });

        }
    }
}
