package edb.eningabiye.dailysteps.services;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;

import com.couchbase.lite.CouchbaseLiteException;

import edb.eningabiye.dailysteps.database.CouchData;
import edb.eningabiye.dailysteps.model.User;

public class ReceiverIntentService extends IntentService
        implements SensorEventListener,StepListener{

    private SensorManager mSensorManager;
    private StepDetector stepDetector;
    private Sensor mSensor;
    private CouchData db;


    public ReceiverIntentService() {
        super("ReceiverIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepDetector = new StepDetector();
        stepDetector.registerListener(this);
        try {
            db = new CouchData(this);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            stepDetector.updateAccel(sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void step(long timeNs) {
        //numSteps++;
        try {
            db.saveStep(1);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        //textView.setText(getString(R.string.today_count,numSteps));
    }
}
