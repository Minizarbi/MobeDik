package m2dl.com.mobedik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.occupation:
                startActivity(new Intent(this, OccupationActivity.class));
                return true;
            case R.id.geoloc:
                startActivity(new Intent(this, GeolocActivity.class));
                return true;
            case R.id.timetable:
                startActivity(new Intent(this, TimetableActivity.class));
                return true;
            case R.id.anomalies:
                startActivity(new Intent(this, AnomaliesActivity.class));
                return true;
            case R.id.codeinfos:
                startActivity(new Intent(this, CodeinfosActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
