package m2dl.com.mobedik;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import m2dl.com.mobedik.m2dl.com.mobedik.domain.Anomaly;

import static android.location.LocationManager.GPS_PROVIDER;

public class AnomaliesActivity extends AppCompatActivity implements SensorEventListener {

    static int pictureId = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_LOCATION = 2;

    private LocationManager locationManager;
    private SensorManager mSensorManager;
    private ImageView imageViewPicture;
    private RadioGroup radioGroupCriticality;
    private Button buttonAlert;
    private RadioButton radioButtonComfort;
    private RadioButton radioButtonProblem;
    private RadioButton radioButtonDanger;

    private FirebaseDatabase database;
    private FirebaseStorage storage;

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anomalies);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
        Location lastLoc = locationManager.getLastKnownLocation(GPS_PROVIDER);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        radioGroupCriticality = (RadioGroup) findViewById(R.id.radioGroupCriticality);
        radioButtonComfort = (RadioButton) findViewById(R.id.radioButtonComfort);
        radioButtonProblem = (RadioButton) findViewById(R.id.radioButtonProblem);
        radioButtonDanger = (RadioButton) findViewById(R.id.radioButtonDanger);

        buttonAlert = (Button) findViewById(R.id.buttonAlert);
        buttonAlert.setEnabled(false);
        buttonAlert.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StorageReference storageRef = storage.getReference();
                StorageReference anomalyRef = storageRef.child(getPictureName());

                uploadPicture(anomalyRef);
                uploadData();
                pictureId++;
                finish();
            }
        });

        LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 2, mLocationListener);
        dispatchTakePictureIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewPicture.setImageBitmap(imageBitmap);
            buttonAlert.setEnabled(true);

            mSensorManager.getRotationMatrix(mRotationMatrix, null,
                    mAccelerometerReading, mMagnetometerReading);
            mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Don't receive any more updates from either sensor.
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void uploadPicture(StorageReference anomalyRef) {
        imageViewPicture.setDrawingCacheEnabled(true);
        imageViewPicture.buildDrawingCache();
        Bitmap bitmap = imageViewPicture.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = anomalyRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    private void uploadData() {
        Anomaly anomaly = new Anomaly();

        anomaly.setImageId(getPictureName());

        String criticality = null;
        if (radioButtonComfort.isChecked()) {
            criticality = "Comfort";
        } else if (radioButtonProblem.isChecked()) {
            criticality = "Problem";
        } else if (radioButtonComfort.isChecked()) {
            criticality = "Danger";
        }
        anomaly.setCriticality(criticality);

        HashMap<String, Float> orientation = new HashMap<String, Float>();
        orientation.put("x", mOrientationAngles[0]);
        orientation.put("y", mOrientationAngles[1]);
        orientation.put("z", mOrientationAngles[2]);
        anomaly.setOrientation(orientation);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location lastLoc = locationManager.getLastKnownLocation(GPS_PROVIDER);
        LatLng latLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        anomaly.setLat(latLng.latitude);
        anomaly.setLng(latLng.longitude);

        DatabaseReference anomalies = database.getReference("anomalies");
        anomalies.child(Integer.toString(pictureId)).setValue(anomaly);
    }

    public String getPictureName() {
        return "anomaly"+Integer.toString(pictureId);
    }
}
