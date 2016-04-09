package ra1vi2.asdc.kietian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowInboxMsg extends AppCompatActivity {

    TextView from;
    TextView about;
    TextView orgmsg;
    TextView time;
    String info;
    String msg;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_inbox_msg);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        info = getIntent().getExtras().getString("info");
        msg = getIntent().getExtras().getString("orgmsg");

        String[] pieces = info.split(" ");


        from = (TextView)findViewById(R.id.msg_from_show);
        from.setText(pieces[0]);

        time = (TextView)findViewById(R.id.msg_time_show);
        time.setText(pieces[1]);
        /*
        about = (TextView)findViewById(R.id.msg_about_show);
        about.setText();
        */
        orgmsg = (TextView)findViewById(R.id.org_msg_show);
        orgmsg.setText(msg);

    }
}
