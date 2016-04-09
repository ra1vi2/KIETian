package ra1vi2.asdc.kietian;

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

/**
 * Created by jaihanuman on 4/3/16.
 */
public class ClassesFragment extends Fragment {

    private ArrayAdapter<String> mClassAdapter;
    private List<String> mDetail;

    public ClassesFragment()
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
       /* String[] data = {
                "Lec 1 09 AM - Sub 1 - Fac 1 - 75%",
                "Lec 2 10 AM - Sub 2 - Fac 2 - 75%",
                "Lec 3 11 AM - Sub 3 - Fac 3 - 75%",
                "Lec 4 12 AM - Sub 4 - Fac 4 - 75%",
                "Lec 5 01 PM - Sub 5 - Fac 5 - 75%",
                "Lec 6 02 PM - Sub 6 - Fac 6 - 75%",
                "Lec 7 03 PM - Sub 7 - Fac 7-  75%"
        };*/
        String[] data = {
                "TAP Above Button To Get A Look At",
                "Your Todays Classes."
        };
        List<String> dailyclass = new ArrayList<String>(Arrays.asList(data));

        Bundle bundle = getArguments();
        //ArrayList<String> dailyclass;
        if(bundle!=null)
        {
            dailyclass = bundle.getStringArrayList("classdetails");
            Log.d("DailyClass :" ,dailyclass.toString());
        }
        else
        {
            Log.d("Null HERE", "really");
        }
        mClassAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_class, // The name of the layout ID.
                        R.id.list_item_class_textview, // The ID of the textview to populate.
                        dailyclass);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_class);
        listView.setAdapter(mClassAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //String forecast = mForecastAdapter.getItem(position);
              /*  Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);*/
                Toast.makeText(getActivity(), "You Clicked "+position+" item. Wait For Coming Functions",
                        Toast.LENGTH_LONG).show();
            }
        });

        return rootView;


    }
}