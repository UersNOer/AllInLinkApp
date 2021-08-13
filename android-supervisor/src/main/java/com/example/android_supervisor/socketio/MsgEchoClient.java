package com.example.android_supervisor.socketio;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.OauthHostManager;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;
import okhttp3.HttpUrl;

/**
 * @author wujie
 */
public class MsgEchoClient {
    private Socket socket;

    private Context context;
    private final String baseUrl;

    private List<Listener> listeners = new ArrayList<>();

    public MsgEchoClient(Context context, String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private Socket createSocket(String uri) throws URISyntaxException {
        IO.Options options = new IO.Options();
        HttpUrl.Builder newBuilder = new HttpUrl.Builder();
        if (!Environments.isDeBug()) {
            //深圳特
            String hostAddress = OauthHostManager.getInstance(context).getMsgConfig();
            if (!TextUtils.isEmpty(hostAddress) && hostAddress.contains("tykj")){
//                options.path ="/szcg/socketApi";
                options.path ="/socket.io";
            }
            else if (!TextUtils.isEmpty(hostAddress)&& (hostAddress.contains("szzhcg") || hostAddress.contains("smartum"))){
                HttpUrl baseUrl = HttpUrl.parse(hostAddress);

//                String schem = uri.substring(0,uri.indexOf("//"));
//                LogUtils.e("--->>>>>>>>>>>>>>>>>>>>>>>>"+ schem);
//                String host =uri.substring(0, uri.replace(schem,"").indexOf("/"));
//                uri = stringBuffer.append(schem).append(host).toString();

                newBuilder.scheme(baseUrl.scheme());
                newBuilder.host(baseUrl.host());
                newBuilder.port(baseUrl.port());

                String temp = uri.replace(newBuilder.toString(), "");
                options.path = "/"+temp +"/socketApi";
                uri = newBuilder.toString();
                LogUtils.e("--->>>>>>>>>>>>>>>>>>>>>>>>"+temp);
            }else {
                HttpUrl baseUrl = HttpUrl.parse(hostAddress);

                newBuilder.scheme(baseUrl.scheme());
                newBuilder.host(baseUrl.host());
                newBuilder.port(baseUrl.port());

                String temp = uri.replace(newBuilder.toString(), "");
//                options.path = "/"+temp +"/socketApi";
                options.path ="/socket.io";
                uri = newBuilder.toString();
                LogUtils.e("--->>>>>>>>>>>>>>>>>>>>>>>>"+temp);
            }
        }
//        uri = "https://szzhcg.com";
        options.path ="/api/socketApi/?accessToken="+AccessToken.getInstance(context).getSafety()+"&";
        LogUtils.e("--->>>>>>>>>>>>>>>>>>>>>>>>"+uri);
//        options.transports = new String[]{Polling.NAME,WebSocket.NAME};
        options.transports = new String[]{WebSocket.NAME};

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .hostnameVerifier(myHostnameVerifier)
//                .sslSocketFactory(mySSLContext.getSocketFactory(), myX509TrustManager)
//                .build();
//        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
//        IO.setDefaultOkHttpCallFactory(okHttpClient);
//        options = new IO.Options();
//        options.callFactory = okHttpClient;
//        options.webSocketFactory = okHttpClient;
        // options.transports = new String[]{Polling.NAME,WebSocket.NAME};
        //  Socket socket = IO.socket("https://socket-io-chat.now.sh/",options);

        if (this.socket!=null){
            this.socket.disconnect();
            this.socket.close();
        }
        Socket socket = IO.socket(uri, options);
        socket.on("connect", new Emitter.Listener() {
            public void call(Object... args) {
                Log.i("socket","socket-io connected:" + baseUrl);
                for (Listener listener : listeners) {
                    listener.onConnect();
                }
            }
        });
        heartBeat();
        socket.on("disconnect", new Emitter.Listener() {
            public void call(Object... args) {
                Log.i("socket","socket-io disconnected:");
                Log.i("socket","socket-io disconnected1:" + args[0].toString());
//                if (socket!=null){
//                    socket.disconnect();
//                    socket=null;
//                }
                socket.disconnect();
                connect();
                for (Listener listener : listeners) {
                    listener.onDisconnect();
                }
            }
        });
        socket.on("business_message", new Emitter.Listener() {
            public void call(Object... args) {
                Log.i("socket","socket-io text_message：");
                JSONObject obj = (JSONObject) args[0];
                Ack ack = (Ack) args[2];
                if (obj != null) {
                    Log.i("socket","socket-io text_message："+obj.toString());
                    for (Listener listener : listeners) {
                        listener.onMessage(obj, ack);
                    }
                }
            }
        });
        socket.on("connect_timeout", new Emitter.Listener() {
            public void call(Object... paramAnonymousVarArgs) {
                Log.i("socket","socket-io connect_timeout:" + baseUrl);
            }
        });
        socket.on("reconnect", new Emitter.Listener() {
            public void call(Object... paramAnonymousVarArgs) {
                Log.i("socket","socket-io reconnect:" + baseUrl);
                Logger.i("socket-io reconnect:" + baseUrl);
                login();
            }
        });
        socket.on("error", new Emitter.Listener() {
            public void call(Object... paramAnonymousVarArgs) {
                Exception err = (Exception) paramAnonymousVarArgs[0];
                Log.i("socket","socket-io reconnects:" + err);
                Logger.i("socket-io reconnect:" + err);
                Logger.e(err, "");
            }
        });

        return socket;
    }

    public void connect() {
        try {
            String uri = baseUrl;
            this.socket = createSocket(uri).connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (this.socket != null) {
            Log.d("socket disconnect",  "disconnect");
            this.socket.disconnect();
            this.socket = null;
        }
    }

    public void heartBeat(){
        if (this.socket != null) {
            Log.d("socket heartBeat",  "ping");
            this.socket.send("ping");

        }
    }

    public void login() {
//        AccessToken accessToken = AccessToken.getInstance(context);
//        Log.d("accessToken",  accessToken.get());
//        socket.emit("login_message", accessToken.get(), new Ack() {
//            @Override
//            public void call(Object... args) {
//                Log.d("login_message1", "socket-io login: Ack successful!");
//            }
//        });
    }

    public void logout() {
        socket.emit("logout_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.i("socket-io logout: successful!");
            }
        });
    }

    public void readMessage(Ack ack) {
        Logger.i("socket-io readMsg:" );
        ack.call();
    }


    public void sendMessage(String msg) {
        Log.d("sendMessage","sendMessage");
        if (this.socket != null) {
//            Logger.i("socket-io sendMessage:" + msg);
//            this.socket.send("position_message", msg);

            this.socket.emit("position_message", msg, new Ack() {
                @Override
                public void call(Object... args) {
//                    Log.d("position_message",args[0]+"");
                }
            });

        }
    }

    public static interface Listener {
        void onConnect();

        void onDisconnect();

        void onMessage(JSONObject msgObj, Ack ack);
    }
}
