package edb.eningabiye.dailysteps.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edb.eningabiye.dailysteps.R;
import edb.eningabiye.dailysteps.networking.Client;
import edb.eningabiye.dailysteps.services.ClientService;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    private ArrayList<WifiP2pDevice> data;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private WifiP2pInfo info;
    private Intent mainIntent;
    private Context context;
    public ShareAdapter(ArrayList<WifiP2pDevice> devices, WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, Context context, WifiP2pInfo wifiP2pInfo, Intent i) {
        this.data = devices;
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.context = context;
        this.info = wifiP2pInfo;
        this.mainIntent = i;
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
        if(device.status == WifiP2pDevice.AVAILABLE) {
            if (holder.connect.getVisibility() == View.GONE) {
                holder.connect.setVisibility(View.VISIBLE);
            }
        }else {
            holder.connect.setVisibility(View.GONE);
        }
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
        Button connect, send;
        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.d_name);
            status = v.findViewById(R.id.d_state);
            connect = v.findViewById(R.id.connect);
            send = v.findViewById(R.id.send);
            connect.setOnClickListener(view -> {
                connect.setVisibility(View.GONE);
                WifiP2pDevice device = data.get(getLayoutPosition());
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                config.groupOwnerIntent = 0;
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
                        Toast.makeText(context, "Connect connected.", Toast.LENGTH_SHORT).show();
                        Log.e("-----------","wifi connected");
                        Log.e("Adress: ", (null!=info ? info.groupOwnerAddress.getHostAddress():" ____"));

                        if (send.getVisibility() == View.GONE) {
                            send.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(context, "Connect failed. Retry.", Toast.LENGTH_SHORT).show();
                        Log.e("-----------","Connect failed. Retry.");

                    }
                });

            });
            send.setOnClickListener(view->{
                Intent intent = new Intent(context, ClientService.class);
                intent.setAction("SEND");
                //intent.putExtra("host", info.groupOwnerAddress.getHostAddress());
                intent.putExtra("message", mainIntent.getStringExtra("message"));
                intent.putExtra("steps", String.valueOf(mainIntent.getIntExtra("steps",0)));
                intent.putExtra("nom", mainIntent.getStringExtra("name"));
                context.startService(intent);
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
