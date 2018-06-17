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

public class Client {
    private SocketChannel socket;
    Charset charset;
    String result;
    public Client(){

    }

    public String getUnboundedResponse(String ...params) throws IOException {
        new Connbect().execute(params[0],params[1],params[2]);
        return result;
    }

    private void silentlyClose(SocketChannel socketChannel) {
        if (socketChannel != null)
            try {
                socketChannel.close();
            } catch (IOException e) {
                // Ignore
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

    class Connbect extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings) {
            Log.e("***************", "start");
            charset = Charset.forName("UTF-8");
            Log.e("****ch**********", "charset");
            ArrayList<ByteBuffer> ListBuffer = new ArrayList<>();
            Log.e("*******bu*******", "listbuffer");
            ByteBuffer buffer=null;
            Log.e("******try*******", "go try");

            try {
                socket = SocketChannel.open();
                socket.connect(new InetSocketAddress(strings[1],Integer.valueOf(strings[2])));
                socket.write(charset.encode(strings[0]));
                buffer = ByteBuffer.allocate(1024);
                Log.e("*****0*connected*******", "done");
                socket.read(buffer);//todo exception raised here
                buffer.flip();
            } catch (IOException e) {
                Log.e("**catch*******", "done "+e.getMessage());
            }
            Log.e("*****0********", "end");
            result = null != buffer ? String.valueOf(Charset.defaultCharset().decode(buffer)):null;
            if(result!= null) {
                Log.e("SUPPPERRRRR------", result);
            }
            return result;
        }

        protected void onPostExecute(String res) {
            //Log.e("*******+-*/*******", res);
            result = res;
        }

    }
}
