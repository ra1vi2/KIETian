package ra1vi2.asdc.kietian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SendMessage extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String roll_no;
    private String sem;
    private String sec;
    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();
    private static String url_login = "http://10.42.0.1/get_msg_inbox.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PASS = "pass";
    private static final String TAG_FROM = "from";
    private static final String TAG_TIME = "time";
    private static final String TAG_MSG = "message";

    private GetmsgTask mAuthTask = null;


    SessionManager session;
    // products JSONArray
    JSONArray pass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        roll_no = getIntent().getExtras().getString("roll_no");
        sem = getIntent().getExtras().getString("sem");
        sec = getIntent().getExtras().getString("sec");


        getmsg();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.compose_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(getApplicationContext(), ComposeMessage.class);
                intent.putExtra("roll_no", roll_no);
               /* intent.putExtra("sem", sem);
                intent.putExtra("sec", sec);*/
                startActivity(intent);
            }
        });
    }

    private void getmsg() {
        if (mAuthTask != null) {
            return;
        } else {
            mAuthTask = new GetmsgTask(roll_no);
            mAuthTask.execute((Void) null);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SendOneFragment(), "INBOX");
        adapter.addFragment(new SendTwoFragment(), "OUTBOX");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }

    public class GetmsgTask extends AsyncTask<Void, Void, Boolean>

    {
        private final String mRollno;
        private String msg;

        GetmsgTask(String roll_no) {
            mRollno = roll_no;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SendMessage.this);
            pDialog.setMessage("Loading Messages...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            List<NameValuePair> paramss = new ArrayList<NameValuePair>();
            ArrayList<String> msglist = new ArrayList<String>();
            ArrayList<String> orgmsg = new ArrayList<String>();
            paramss.add(new BasicNameValuePair("rollno",mRollno));

           // Log.d("making A",roll_no);
            JSONObject json = jParser.makeHttpRequest(url_login, "GET", paramss);
            Log.d("Total Data: ", json.toString());
            Log.d("toatal data parsed",roll_no);

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    pass = json.getJSONArray(TAG_PASS);
                    for (int i = 0; i < pass.length(); i++) {
                        JSONObject c = pass.getJSONObject(i);
                        String from = c.getString(TAG_FROM);
                        String mesg = c.getString(TAG_MSG);
                        String time = c.getString(TAG_TIME);
                      Log.d("Message",mesg);


                       msg =  from + "                               " + time;
                        msglist.add(i,msg);

                        orgmsg.add(i,mesg);

                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("msglist" ,msglist);
                    //bundle.putStringArrayList("orgmsg" ,orgmsg);
// set Fragmentclass Arguments
                    MessageFragment fragobj = new MessageFragment();
                    fragobj.setArguments(bundle);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_msg, fragobj);
                    transaction.commit();
                }
                else
                {
                    /*String message = (String)json.getString("message");
                    Toast.makeText(getApplicationContext(), message,
                            Toast.LENGTH_LONG).show();*/

                }
            }catch (JSONException e) {
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
