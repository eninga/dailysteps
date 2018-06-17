package edb.eningabiye.dailysteps;

import android.content.Context;
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

import edb.eningabiye.dailysteps.services.WiFiDirectBroadcastReceiver;

public class ShareStepsActivity extends AppCompatActivity {
    private ArrayList<WifiP2pDevice> list = new ArrayList<>();


    private ShareAdapter listAdapter;
    private RecyclerView recyclerView;

    public static final long JOUR = 86400000;
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
                Toast.makeText(ShareStepsActivity.this, "Discover succes", Toast.LENGTH_LONG).show();
                //Log.e("Discover", " succes");
            }
            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(ShareStepsActivity.this, "reasonCode"+reasonCode, Toast.LENGTH_LONG).show();
                Log.e("Discover not", ""+reasonCode);
            }
        });
        /*WifiP2pDevice device;
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                //success logic
            }

            @Override
            public void onFailure(int reason) {sensor
                //failure logic
            }
        });*/
        recyclerView = findViewById(R.id.devices_show);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        listAdapter = new ShareAdapter(receiver.peers, wifiP2pManager,channel, this, receiver.getInfo());
        recyclerView.setAdapter(listAdapter);

    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    public ShareAdapter getListAdapter() {
        return listAdapter;
    }
}
