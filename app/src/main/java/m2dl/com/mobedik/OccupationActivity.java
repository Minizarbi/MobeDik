package m2dl.com.mobedik;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import m2dl.com.mobedik.m2dl.com.mobedik.domain.Building;

/**
 * Created by Timothee on 10/03/2017.
 */

public class OccupationActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();
    GraphView graphRU1, graphRU2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupation);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refBuldings = database.getReference("buildings");

        DatabaseReference refRU1 = refBuldings.child("2");
        DatabaseReference refRU2 = refBuldings.child("1");

        graphRU1 = (GraphView) findViewById(R.id.graphRU1);
        graphRU2 = (GraphView) findViewById(R.id.graphRU2);

        ValueEventListener graphEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Building building = dataSnapshot.getValue(Building.class);

                TreeSet<DataPoint> dataPoints = new TreeSet<DataPoint>(new Comparator<DataPoint>() {
                    @Override
                    public int compare(DataPoint dataPoint, DataPoint t1) {
                        if (dataPoint.getX() < t1.getX()) {
                            return -1;
                        }
                        else if (dataPoint.getX() > t1.getX()) {
                            return 1;
                        }
                        else {
                            return 0;
                        }
                    }
                });
                HashMap<String, Integer> freq = building.getFreq();
                for (String heure : freq.keySet()) {
                        dataPoints.add(new DataPoint(Integer.parseInt(heure), freq.get(heure)));
                }

                GraphView graph;
                if (building.getName().equals("RU1"))
                    graph = graphRU1;
                else if (building.getName().equals("RU2"))
                    graph = graphRU2;
                else return;

                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));
                updateGraph(graph, series);
                initGraphViewPort(graph);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        refRU1.addValueEventListener(graphEventListener);
        refRU2.addValueEventListener(graphEventListener);
    }

    public void initGraphViewPort(GraphView graph) {
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
    }

    public void updateGraph(GraphView graph, LineGraphSeries<DataPoint> series) {

        graph.removeAllSeries();
        graph.addSeries(series);
    }
}