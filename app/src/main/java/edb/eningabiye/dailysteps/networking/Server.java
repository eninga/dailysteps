package edb.eningabiye.dailysteps.networking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import edb.eningabiye.dailysteps.services.NotifyService;

public class Server  extends  AsyncTask<String, Void, String>{
    private ServerSocketChannel server;
    private Charset charset;
    private Context context;

    public static void writeResponse(SocketChannel sc, String response)
            throws IOException {
        ByteBuffer buff = ByteBuffer.allocate(response.getBytes().length);
        buff.put(response.getBytes());
        buff.flip();
        sc.write(buff);
    }
    public Server(Context context) {
        super();
        this.context = context;
    }

    protected String doInBackground(String... strings) {
        charset = Charset.forName("UTF-8");
        ByteBuffer buffer;
        SocketChannel client =null;
        try {
            server = ServerSocketChannel.open();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N /*24*/) {
                server.bind(new InetSocketAddress(7777));
                Log.e("___________bound__"," bound on "+ server.socket().getLocalSocketAddress());
            }else{
                server.socket().bind(new InetSocketAddress(7777));
                Log.e("___________bound__"," bound on "+ server.socket().getLocalSocketAddress());

            }
            while (true) {
                Log.e("______________________",  "going to accept");
                client = server.accept();
                Log.e("____accepted________ ",  client.socket().getInetAddress().getHostAddress());
                buffer = ByteBuffer.allocate(512);
                client.read(buffer);
                buffer.flip();
                String msg = charset.decode(buffer).toString();
                Intent intent = new Intent(context, NotifyService.class);
                intent.setAction("NOTIFY");
                intent.putExtra("msg",msg);
                context.startService(intent);
                Log.e("__________Data______",  msg);
                client.write(charset.encode("OK sent!"));
            }
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
