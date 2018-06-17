package edb.eningabiye.dailysteps.networking;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Server  extends  AsyncTask<String, Void, String>{
    private ServerSocketChannel server;
    private Charset charset;
    Context context;
    public Server(Context ctxt) throws IOException {
        context= ctxt;
    }
    public static void writeResponse(SocketChannel sc, String response)
            throws IOException {
        ByteBuffer buff = ByteBuffer.allocate(response.getBytes().length);
        buff.put(response.getBytes());
        buff.flip();
        sc.write(buff);
    }
    public void launch() {

    }

    protected String doInBackground(String... strings) {
        charset = Charset.forName("UTF-8");
        ByteBuffer buffer=null;
        SocketChannel client =null;
        try {
            server = ServerSocketChannel.open();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N /*24*/) {
                server.bind(new InetSocketAddress(7777));
            }else{
                server.socket().bind(new InetSocketAddress(7777));
                Log.e("___________bound__"," bound on "+ server.socket().getLocalSocketAddress());

            }
            new Handler(Looper.getMainLooper()).post(()->{
                Toast.makeText(context,this.getClass().getName() + " bound on "+ server.socket().getLocalSocketAddress(), Toast.LENGTH_LONG).show();;
            });
            //while (true) {
            Log.e("Connection accepted fr ",  "go accept");

            client = server.accept();
            //Toast.makeText(context,"Connection accepted from " + client.socket().getInetAddress().getHostAddress(), Toast.LENGTH_LONG).show();;

            Log.e("Connection accepted fr ",  client.socket().getInetAddress().getHostAddress());
            serve(client);
            // }
        } catch (IOException ioe) {
            Log.e("____error_","I/O Error while communicating with client..."+ioe.getMessage());

        }  finally {
            silentlyClose(client);
        }

        return null;
    }

    protected void onPostExecute(String res) {
        Log.e("*******+-*/*******", "oki");
    }


    public void serve(SocketChannel sc) throws IOException{
        ByteBuffer buffer = ByteBuffer.allocate(1028);
        sc.read(buffer);
        buffer.flip();
        Log.e("read message", String.valueOf(Charset.defaultCharset().decode(buffer)));
        writeResponse(sc, "OK");
    }

    public static void silentlyClose(SocketChannel sc) {
        if (sc != null) {
            try {
                sc.close();
            } catch (IOException e) {
            }
        }
    }


}
