package edb.eningabiye.dailysteps.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

import edb.eningabiye.dailysteps.ShareStepsActivity;
import edb.eningabiye.dailysteps.model.MyWifi;
import edb.eningabiye.dailysteps.networking.Server;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private ShareStepsActivity mActivity;
    public ArrayList<WifiP2pDevice> peers = new ArrayList<>();
    private WifiP2pInfo info;

    public WifiP2pInfo getInfo() {
        return info;
    }


    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,ShareStepsActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }


    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            Collection<WifiP2pDevice> refreshedPeers =  peerList.getDeviceList();
            if (!refreshedPeers.equals(peers)) {
                peers.clear();
                peers.addAll(refreshedPeers);
                mActivity.getListAdapter().notifyDataSetChanged();
                for (WifiP2pDevice device : peers) {
                    Log.e("________deviceName", device.deviceName+"");
                }
            }

            if (peers.size() == 0) {
                Log.e("Peers", "No devices found");
            }
        }
    };
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            WiFiDirectBroadcastReceiver.this.info = wifiP2pInfo;
            Log.e("________Wifi info______", wifiP2pInfo.toString());
            //new Server(mActivity).execute();
            MyWifi.ip_group = wifiP2pInfo.groupOwnerAddress.getHostAddress();
            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                new Server(mActivity).execute();
                Log.e("________g_Owner ___", wifiP2pInfo.groupOwnerAddress.getHostAddress());
            } else if (wifiP2pInfo.groupFormed) {
                Log.e("______Not g owner ___", wifiP2pInfo.groupOwnerAddress.getHostAddress());
            }

        }
    };
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
            } else {
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (mManager != null) {
                mManager.requestPeers(mChannel, peerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (mManager == null) {
                return;
            }
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                mManager.requestConnectionInfo(mChannel, connectionInfoListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
        }
    }
}