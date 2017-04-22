package com.apps.koenig.thomas.r3edatadisplay;

import android.app.Application;
import android.util.Log;

import com.apps.koenig.thomas.r3edatadisplay.communication.ConnectionChangelListener;
import com.apps.koenig.thomas.r3edatadisplay.communication.MessageReceiveListener;
import com.apps.koenig.thomas.r3edatadisplay.communication.R3EConnection;
import com.apps.koenig.thomas.r3edatadisplay.communication.R3EConnectionListener;
import com.apps.koenig.thomas.r3edatadisplay.communication.R3EMessage;
import com.apps.koenig.thomas.r3edatadisplay.model.R3EDisplayData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 10.03.2017.
 */

public class R3EApp extends Application implements MessageReceiveListener, ConnectionChangelListener {

    private static List<R3EConnectionListener> listeners = new ArrayList<>();
    private static R3EConnection r3eConnection;

    public static R3EDisplayData getR3EDisplayData() {
        return r3EDisplayData;
    }

    private static R3EDisplayData r3EDisplayData = new R3EDisplayData();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("App", "OnCreate");
        r3EDisplayData = new R3EDisplayData();
        r3eConnection = new R3EConnection(R3EConnection.R3EPORT);
        r3eConnection.setListener(this);
        r3eConnection.setConnectionChangelListener(this);

        if (listeners.size() > 0)
        {
            r3eConnection.start();
        }
    }

    @Override
    public synchronized void onReceiveMessage(final R3EMessage msg) {
        r3EDisplayData.update(msg);
        for(R3EConnectionListener listener : listeners) {
            listener.receiveMessage(msg);
        }
    }
    public static void addListener(R3EConnectionListener r3eListener)
    {
        listeners.add(r3eListener);
        if (r3eListener != null && r3eConnection != null) {
            r3eListener.onConnectionChange(r3eConnection.isConnected());
        }

        Log.i("App", "Adding listener: " + listeners.size());
        if (listeners.size() > 0 && r3eConnection !=null && !r3eConnection.isRunning())
        {
            r3eConnection.start();
        }
    }

    public static void removeListener(R3EConnectionListener r3eListener)
    {
        listeners.remove(r3eListener);
        Log.i("App", "Removing listener: " + listeners.size());
        if (listeners.size() <= 0 && r3eConnection != null)
        {
            r3eConnection.stop();
        }
    }

    @Override
    public synchronized void onConnectionChange(boolean connected) {
        for(R3EConnectionListener listener : listeners)
        {
            listener.onConnectionChange(connected);
        }
    }
}
