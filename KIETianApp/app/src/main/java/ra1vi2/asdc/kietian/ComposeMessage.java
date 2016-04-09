package ra1vi2.asdc.kietian;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComposeMessage extends AppCompatActivity {

    private String roll_no;
    private String to_roll_no="todefault";
    //private String msgabout;
    private String msg_content="msgdefault";
    private ProgressDialog pDialog;

    private SendmsgTask mAuthTask = null;

    private EditText ettoroll;
    private EditText etmsgabout;
    private EditText  etcontent;

    AlertDialogManager alertDialogManager =  new AlertDialogManager();
    SessionManager session;

    JSONParser jParser = new JSONParser();
    private static String url_login = "http://10.42.0.1/send_msg.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        ettoroll = (EditText) findViewById(R.id.torollno1);
      //  etmsgabout = (EditText) findViewById(R.id.aboutmsg);
        etcontent = (EditText) findViewById(R.id.msg_content);


        roll_no = getIntent().getExtras().getString("roll_no");
        to_roll_no  = ettoroll.getText().toString();
        msg_content = etcontent.getText().toString();
        //msgabout = etmsgabout.getText().toString();

        Log.d("bhai","enjoy");
        Log.d("bhai ye",to_roll_no);
        Log.d("bhai ye",msg_content);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.compose_done_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Log.d("bhai roll_no",roll_no);
                Log.d("bhai to_roll_no",to_roll_no);
                Log.d("bhai msg_content",msg_content);
                sendmessage();
            }
        });
    }


    private void sendmessage() {
        if (mAuthTask != null) {
            return;
        } else {
            mAuthTask = new SendmsgTask(roll_no, msg_content, to_roll_no);
            mAuthTask.execute((Void) null);
        }
    }

    public class SendmsgTask extends AsyncTask<Void, Void, Boolean>

    {
        private final String mRollno;
        private String mMsg;
        private String mToroll;

        SendmsgTask(String roll_no, String msg_content, String to_roll_no) {
            mRollno = roll_no;
            mMsg = msg_content;
            mToroll = to_roll_no;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ComposeMessage.this);
            pDialog.setMessage("Sending Messages...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            List<NameValuePair> paramss = new ArrayList<NameValuePair>();
           // ArrayList<String> msglist = new ArrayList<String>();
            paramss.add(new BasicNameValuePair("rollno", mRollno));
            paramss.add(new BasicNameValuePair("msg_content", mMsg));
            paramss.add(new BasicNameValuePair("torollno", mToroll));


            Log.d("bhai yha", mRollno);
            JSONObject json = jParser.makeHttpRequest(url_login, "GET", paramss);

            Log.d("Total Data: ", json.toString());
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                        ComposeMessage.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alertDialogManager.showAlertDialog(ComposeMessage.this, "Message ..", "Try Again", false);
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Message Sent",
                                Toast.LENGTH_LONG).show();
                    }


            } catch (JSONException e) {
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