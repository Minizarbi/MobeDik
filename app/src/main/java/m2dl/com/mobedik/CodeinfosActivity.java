package m2dl.com.mobedik;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import m2dl.com.mobedik.m2dl.com.mobedik.domain.Building;

public class CodeinfosActivity extends AppCompatActivity {

    private TextView textView;
    private String scanContent;
    private String infos;
    private ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codeinfos);

        textView = (TextView) findViewById(R.id.textview_codeinfos);

        Button button = (Button) findViewById(R.id.button_scan);
        final CodeinfosActivity activity = this;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new IntentIntegrator(activity).initiateScan();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            scanContent = scanningResult.getContents();
            textView.setText(scanContent + "...");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("informations").child(scanContent);

            if (listener != null) {
                myRef.removeEventListener(listener);
            }

            listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    infos = dataSnapshot.getValue(String.class);
                    textView.setText(infos);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Do nothing
                }
            };

            myRef.addValueEventListener(listener);
        }
    }
}