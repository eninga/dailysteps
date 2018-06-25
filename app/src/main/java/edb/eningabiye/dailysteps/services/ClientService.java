package edb.eningabiye.dailysteps.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ClientService extends IntentService {

    public ClientService() {
        super("ClientService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if ("SEND".equals(action)) {
                String result = null;
                Charset charset = Charset.forName("UTF-8");
                ByteBuffer buffer;
                SharedPreferences sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE);
                String username = sharedPref.getString("username", null);
                String message = intent.getStringExtra("message")+"##"+username+"##"+intent.getStringExtra("steps");
                try {
                    SocketChannel socket = SocketChannel.open();
                    String ridge = "192.168.43.97",
                            feel = "192.168.43.47",
                            group = "192.168.49.1";

                    socket.connect(new InetSocketAddress(group,7777));
                    socket.write(charset.encode(message));
                    buffer = ByteBuffer.allocate(1024);
                    socket.read(buffer);//todo exception raised here
                    buffer.flip();
                    result = String.valueOf(charset.decode(buffer));
                    final  String r = result;
                    new Handler(Looper.getMainLooper()).post(()->{
                        Toast.makeText(getBaseContext(),r, Toast.LENGTH_SHORT).show();
                    });
                } catch (IOException e) {
                    Log.e("**catch*******", "done "+e.getMessage());
                }

                if(result!= null) {
                    Log.e("---retour serveur-----:", result);
                    Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
