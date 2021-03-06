package com.apps.koenig.thomas.r3edatadisplay.communication;

import com.apps.koenig.thomas.r3edatadisplay.model.DisplayData;
import com.apps.koenig.thomas.r3edatadisplay.model.QualyData;
import com.apps.koenig.thomas.r3edatadisplay.model.RaceData;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thomas on 26.02.2017.
 */

public class R3EConnection {
    public static final int R3EPORT = 56678;
    private MessageReceiveListener listener;
    private DateTime lastMessageReceived;
    private boolean connected;
    private UDPReceiveConnection connection;
    private ScheduledExecutorService executorService;
    private boolean running;

    public void setConnectionChangelListener(ConnectionChangelListener connectionChangelListener) {
        this.connectionChangelListener = connectionChangelListener;
    }

    public R3EConnection(int port) {
        this.port = port;
    }

    private ConnectionChangelListener connectionChangelListener;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private int port;

    public void setListener(MessageReceiveListener listener) {
        this.listener = listener;
    }

    public void start() {
        System.out.println("Start");
        startConnectionListener();
        connection = new UDPReceiveConnection(port, new BytesReceivedListener() {
            @Override
            public void onBytesReceived(ByteBuffer buffer) {
                DisplayData displayData = parseMessage(buffer);

                //System.out.println(msg.toString());
                lastMessageReceived = DateTime.now();
                if (listener != null) {
                }
            }


        });
        connection.start();
        running = true;
    }

    private DisplayData parseMessage(ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        short version = buffer.getShort();
        byte type = buffer.get();
        DisplayData data = null;
        if (type == 0)
        {
            data = new DisplayData(buffer);
        }
        else if(type == 1)
        {
            // qualy
            data = new QualyData(buffer);
        }
        else if (type == 2)
        {
            // race
            data = new RaceData(buffer);
        }
        else
        {
            // wrong type
        }

        return data;
    }

    private void startConnectionListener() {
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (lastMessageReceived == null || DateTime.now().getMillis() - lastMessageReceived.getMillis() > 1000)
                {
                    setConnected(false);
                }
                else
                {
                    setConnected(true);
                }
            }
        }, 100, 300, TimeUnit.MILLISECONDS);
    }

    private void setConnected(boolean connected) {
        if (connected == this.connected)
        {
            return;
        }

        this.connected = connected;
        if (connectionChangelListener != null)
        {
            connectionChangelListener.onConnectionChange(connected);
        }
    }

    public void stop() {
        running = false;
        if (connection != null)
        {
            connection.cancel();
        }
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
