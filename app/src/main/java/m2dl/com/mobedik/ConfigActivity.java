package m2dl.com.mobedik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        TextView aboutView = (TextView) findViewById(R.id.text_about);

        String about = "";

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            if (user.isAnonymous()) {
                about += "Vous êtes anonyme.\n";
            }
            about += "Identifiant : " + user.getEmail() + "\n";
            about += "Uid : " + user.getUid() + "\n";
        }
        about += "Formation : M2DL\n";

        aboutView.setText(about);
    }
}
