package edb.eningabiye.dailysteps;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import edb.eningabiye.dailysteps.adapters.ListAdapter;
import edb.eningabiye.dailysteps.adapters.StatAdapter;
import edb.eningabiye.dailysteps.database.CouchData;
import edb.eningabiye.dailysteps.model.Step;
import edb.eningabiye.dailysteps.model.User;
import edb.eningabiye.dailysteps.services.StepDetector;

public class StatActivity extends AppCompatActivity {
    private ArrayList<Step> list = new ArrayList<>();
    private CouchData db;
    Toolbar toolbar;
    private ResultSet resultSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        RecyclerView recyclerView = findViewById(R.id.datashow_stat);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
        String formattedDate = df.format(c);
        try {
            db = new CouchData(this);
            String title = setTitle(intent);
            if(getSupportActionBar()!=null){
                getSupportActionBar().setTitle(title);
            }
            for (Result result : resultSet) {
                Log.e("______0_____",result.getInt("$0")+"");
                Log.e("______1_____",result.getInt("$1")+"");
                Log.e("_______2____",result.getInt("$2")+"");
                Log.e("________3___",result.getInt("$3")+"");
                //numSteps = db.getSteps().next().getDictionary(CouchData.DATABASE_NAME).getInt("steps");
                Step step = new Step(result.getString("date"),result.getInt("$1"),list.size()>0?list.get(list.size()-1).getSteps():0,result.getString("month"), result.getString("year"));
                list.add(step);
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        Collections.reverse(list);
        StatAdapter adapter = new StatAdapter(list,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    public String setTitle(Intent i) throws CouchbaseLiteException {
        if(i != null){
            switch (i.getStringExtra("type")){
                case "total":
                    resultSet = db.getTotalSteps();
                    return "Total mensuel";
                case "min":
                    resultSet = db.getMinSteps();
                    return "Minimum mensuel";
                case "max":
                    resultSet = db.getMaxSteps();
                    return "Maximum mensuel";
                case "moyenne":
                    resultSet = db.getMoySteps();
                    return "Moyenne mensuelle";
            }
        }
        return null;
    }

}
