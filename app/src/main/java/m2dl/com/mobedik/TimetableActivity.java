package m2dl.com.mobedik;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TimetableActivity extends AppCompatActivity {

    private String url = "http://www.master-developpement-logiciel.fr/vie-etudiante/emplois-du-temps/emploi-du-temps-m2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        Button button = (Button) findViewById(R.id.button_open_timetable);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}
