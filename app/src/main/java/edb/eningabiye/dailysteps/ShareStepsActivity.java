package edb.eningabiye.dailysteps;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

import edb.eningabiye.dailysteps.adapters.ShareAdapter;
import edb.eningabiye.dailysteps.services.WiFiDirectBroadcastReceiver;

public class ShareStepsActivity extends AppCompatActivity {
    private ArrayList<WifiP2pDevice> list = new ArrayList<>();

    private ShareAdapter listAdapter;
    private RecyclerView recyclerView;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private IntentFilter mIntentFilter;
    private WiFiDirectBroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_steps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        receiver =new WiFiDirectBroadcastReceiver(wifiP2pManager,channel,this);



        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //Log.e("Discover", " succes");
            }
            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(ShareStepsActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                Log.e("Discover not", ""+reasonCode);
            }
        });
        Intent mainIntent = null;

        if(getIntent().getAction() != null && getIntent().getAction().equals("MAIN")){
            mainIntent =  getIntent();
        }
        recyclerView = findViewById(R.id.devices_show);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        listAdapter = new ShareAdapter(receiver.peers, wifiP2pManager,channel, this, receiver.getInfo(), mainIntent);
        recyclerView.setAdapter(listAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, mIntentFilter);
    }

    @Override
    protected void onStart() {
        registerReceiver(receiver, mIntentFilter);
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        if (wifiP2pManager != null && channel != null) {
            wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {

                @Override
                public void onFailure(int reasonCode) {
                }

                @Override
                public void onSuccess() {
                }
            });
        }
        super.onDestroy();
    }

    public ShareAdapter getListAdapter() {
        return listAdapter;
    }
}
