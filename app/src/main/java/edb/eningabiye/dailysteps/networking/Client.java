package edb.eningabiye.dailysteps.networking;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Client extends AsyncTask<String, Void, String>{
    private SocketChannel socket;
    private Charset charset;
    private String result;

    private void silentlyClose(SocketChannel socketChannel) {
        if (socketChannel != null) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                // Ignore
            }
        }

    }

    private void readFully2(SocketChannel socketChannel, ByteBuffer bb) throws IOException {
        while (bb.hasRemaining() && socketChannel.read(bb) != -1) {
        }
    }

    public static boolean readFully(SocketChannel sc, ByteBuffer buffer) throws IOException {
        int nb = sc.read(buffer);
        Log.e("******"+nb, " read "+nb);
        buffer.flip();
        return nb != -1;
    }
    @Override
    public void onPreExecute(){
        Log.e("******cli*********", "cliznt start");
    }
    @Override
    protected String doInBackground(String... strings) {
        charset = Charset.forName("UTF-8");
        ByteBuffer buffer;
        Log.e("******try*******", "go try");

        try {
            socket = SocketChannel.open();
            String ridge = "192.168.43.97";
            String feel = "192.168.43.47";
            socket.connect(new InetSocketAddress(ridge,Integer.valueOf(strings[2])));
            socket.write(charset.encode(strings[0]));
            buffer = ByteBuffer.allocate(1024);
            Log.e("cli_connected*******", "done");
            socket.read(buffer);//todo exception raised here
            buffer.flip();
            result = String.valueOf(charset.decode(buffer));
        } catch (IOException e) {
            Log.e("**catch*******", "done "+e.getMessage());
        }

        if(result!= null) {
            Log.e("---retour serveur-----:", result);
        }
        return result;
    }
    @Override
    protected void onPostExecute(String res) {
        //Log.e("*******+-*/*******", res);
        result = res;
        silentlyClose(socket);
    }
}
