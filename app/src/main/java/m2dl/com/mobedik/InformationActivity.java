package m2dl.com.mobedik;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity implements SensorEventListener {

    private TextView luminosityTextView;
    private TextView soundVolumeTextView;

    private AudioManager audio;

    private SensorManager mSensorManager;
    private Sensor mLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        luminosityTextView = (TextView) findViewById(R.id.luminosityTextView);
        soundVolumeTextView = (TextView) findViewById(R.id.soundVolumeTextView);

        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        updateSoundVolumeTextView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                updateSoundVolumeTextView();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                updateSoundVolumeTextView();
                return true;
            default:
                return false;
        }
    }

    public void updateSoundVolumeTextView() {
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        soundVolumeTextView.setText(Integer.toString(currentVolume));
    }

    @Override
    protected void onResume() {
        mSensorManager.registerListener(this, mLight,
                SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        float luminosityValue = event.values[0];
        String luminosityText = Float.toString(luminosityValue);
        luminosityTextView.setText(luminosityText);
    }
}
