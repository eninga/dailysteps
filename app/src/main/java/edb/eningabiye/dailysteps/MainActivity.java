package edb.eningabiye.dailysteps;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import edb.eningabiye.dailysteps.adapters.ListAdapter;
import edb.eningabiye.dailysteps.database.CouchData;
import edb.eningabiye.dailysteps.model.Step;
import edb.eningabiye.dailysteps.model.User;
import edb.eningabiye.dailysteps.services.StepDetector;
import edb.eningabiye.dailysteps.services.StepListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener,StepListener {
    private ArrayList<Step> list = new ArrayList<>();
    private SensorManager mSensorManager;
    private StepDetector stepDetector;
    private Sensor mSensor;
    private int numSteps=0;
    private TextView textView;
    private CouchData db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.pas);
        try {
            db = new CouchData(this);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepDetector = new StepDetector();
        stepDetector.registerListener(this);

        RecyclerView recyclerView = findViewById(R.id.datashow);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
        String formattedDate = df.format(c);
        try {
            for (Result result : db.getSteps()) {
                Dictionary all = result.getDictionary(CouchData.DATABASE_NAME);
                //numSteps = db.getSteps().next().getDictionary(CouchData.DATABASE_NAME).getInt("steps");
                Step step = new Step(all.getString("date"),all.getInt("steps"),list.size()>0?list.get(list.size()-1).getSteps():0,new User(all.getString("user"), Double.valueOf(Math.pow(5,new Random().nextInt(6)+1)).intValue()+""));
                list.add(step);
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        if(list.size() > 0) {
            Step current = list.get(list.size() - 1);
            if (current.getDate().equals(formattedDate)) {
                numSteps = current.getSteps();
            }
        }
        Collections.reverse(list);
        ListAdapter adapter = new ListAdapter(list,this);
        recyclerView.setAdapter(adapter);

        try {
            db.getStepsi();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.message_shared) {
            startActivity(new Intent(this, Messages.class));
        }
        else if (id == R.id.total) {
            startStatActivity("total");
        } else if (id == R.id.moyenne) {
            startStatActivity("moyenne");

        } else if (id == R.id.min) {
            startStatActivity("min");

        } else if (id == R.id.max) {
            startStatActivity("max");

        } else if (id == R.id.user) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setAction("CHANGE_USER");
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void startStatActivity(String type){
        Intent intent = new Intent(this, StatActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        textView.setText(getString(R.string.today_count,numSteps));
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        Toast.makeText(this,"Arrêt du compteur des pas", Toast.LENGTH_SHORT).show();
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
        numSteps++;
        try {
            db.saveStep(1);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        runOnUiThread(()-> {
                textView.setText(getString(R.string.today_count,numSteps));
        });
    }
}
