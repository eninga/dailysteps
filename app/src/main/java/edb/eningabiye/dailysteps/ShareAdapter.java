package edb.eningabiye.dailysteps;

import android.content.Context;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import edb.eningabiye.dailysteps.networking.Client;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    private ArrayList<WifiP2pDevice> data;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    WifiP2pInfo info;
    private Context context;
    ShareAdapter(ArrayList<WifiP2pDevice> devices, WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, Context context, WifiP2pInfo wifiP2pInfo) {
        this.data = devices;
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.context = context;
        this.info = wifiP2pInfo;
    }

    @NonNull
    @Override
    public ShareAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_device, parent, false);
        return new ShareAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareAdapter.ViewHolder holder, int position) {
        WifiP2pDevice device = data.get(position);
        holder.name.setText(device.deviceName);
        holder.status.setText(getDeviceStatus(device.status));
    }

    @Override
    public int getItemCount() {
        if(data == null) {
            return 0;
        }else{
            return data.size();
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, status;
        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.d_name);
            status = v.findViewById(R.id.d_state);
            v.setOnClickListener(view -> {

                    WifiP2pDevice device = data.get(getLayoutPosition());
                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = device.deviceAddress;
                    config.wps.setup = WpsInfo.PBC;
                    /*try {
                        Client client = new Client();
                        //rig fab 192.168.43.97, fe80::826a:b0ff:fee7:b465
                        String s =client.getUnboundedResponse("Bonjour !","192.168.43.97","7777");
                        Toast.makeText(context, "'''''''''''  "+s+"   "+"192.168.43.97", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {

                            /*new Handler(Looper.getMainLooper()).post(()->{

                            });*/
                            Log.e("-----------","wifi connected");
                            Toast.makeText(context, "Wifi connected",Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Adress: "+ (null!=info ? info.groupOwnerAddress.getHostAddress():" ____"),Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onFailure(int reason) {
                            Toast.makeText(context, "Connect failed. Retry.", Toast.LENGTH_SHORT).show();
                            Log.e("-----------","Connect failed. Retry.");

                        }
                    });

            });
        }
    }
    private static String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Disponible";
            case WifiP2pDevice.INVITED:
                return "Invité";
            case WifiP2pDevice.CONNECTED:
                return "Connecté";
            case WifiP2pDevice.FAILED:
                return "Echec";
            case WifiP2pDevice.UNAVAILABLE:
                return "Indisponible";
            default:
                return "Inconnu";

        }
    }
}
