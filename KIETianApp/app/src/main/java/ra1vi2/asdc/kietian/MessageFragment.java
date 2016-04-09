package ra1vi2.asdc.kietian;

/**
 * Created by jaihanuman on 10/3/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageFragment extends Fragment {

    private ArrayAdapter<String> mClassAdapter;
    private List<String> mDetail;
    List<String> orgmsg;
    String[] data = {
            "Your Inbox is Loading..."
    };
    ArrayList<String> msglist = new ArrayList<String>(Arrays.asList(data));


    public MessageFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Create some dummy data for the ListView.  Here's a sample weekly forecast



        Bundle bundle = getArguments();
        //ArrayList<String> dailyclass;
        if(bundle!=null)
        {
            msglist = bundle.getStringArrayList("msglist");
           // orgmsg = bundle.getStringArrayList("orgmsg");
            Log.d("Message List :", msglist.toString());
        }
        else
        {
            Log.d("Null HERE", "really");
        }
        mClassAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_inbox, // The name of the layout ID.
                        R.id.list_item_inbox_textview, // The ID of the textview to populate.
                        msglist);

        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView_inbox);
        listView.setAdapter(mClassAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               /* String msg = orgmsg.get(position);
                Intent intent = new Intent(getActivity(), ShowInboxMsg.class);
                        intent.putExtra("msg", msg);
                intent.putExtra("info",msglist.get(position));
                startActivity(intent);*/
                Toast.makeText(getActivity(), "You Clicked " + position + " item. Wait For Coming Functions",
                        Toast.LENGTH_LONG).show();
            }
        });

        return rootView;


    }
}
