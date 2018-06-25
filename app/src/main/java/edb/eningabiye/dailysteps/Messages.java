package edb.eningabiye.dailysteps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Result;

import java.util.ArrayList;

import edb.eningabiye.dailysteps.adapters.MessageAdapter;
import edb.eningabiye.dailysteps.database.CouchData;
import edb.eningabiye.dailysteps.model.Message;

public class Messages extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = findViewById(R.id.messages);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        CouchData db;
        ArrayList<Message> messages = new ArrayList<>();
        try {
            db = new CouchData(this);
            for (Result result : db.getMessages()) {
                Dictionary all = result.getDictionary(CouchData.DATABASE_MSG);
                Message message =new Message(all.getString("nom"), all.getString("date"),all.getString("message"), all.getString("steps"));
                messages.add(message);
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        MessageAdapter messageAdapter = new MessageAdapter(messages,this);
        recyclerView.setAdapter(messageAdapter);
    }

}
