/*
 * Copyright Airbus Defence and Space Deutschland GmbH
 *
 * Version: $Revision$
 * Last author: $Author$
 * Last changed date: $Date$
 */
package com.apps.koenig.thomas.r3edatadisplay.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Receives UDP-Packets on port 25700
 * @author Thomas Horn
 */
public class UDPReceiveConnection
{
    /**
     * Executorservice runs tasks asynchronously.
     */
    protected ExecutorService executorService;
    /**
     * Flag whether the udp connection is canceled.
     */
    boolean canceled = false;

    /**
     * ConnectionHandler which handles the messages.
     */
    private BytesReceivedListener handler;

    /**
     * Receiving port.
     */
    private int  port;

    /**
     * Length of the receiving data packet.
     */
    private final int MAX_LENGTH = 50000;

    /**
     * Socket.
     */
    private DatagramSocket socket;

    /**
     * Constructor
     * @param connectionHandler Handles the messages.
     */
    public UDPReceiveConnection(int port, BytesReceivedListener connectionHandler)
    {
        this.port = port;
        handler = connectionHandler;

    }

    public void start()
    {
        if (socket != null && !socket.isClosed())
        {
            System.out.println("Socket is still open");
            return;
        }


        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("Listening to udp messages on port " + port);
                executorService = Executors.newSingleThreadExecutor();
                try
                {
                    socket = new DatagramSocket(port);
                }
                catch (SocketException e)
                {
                    e.printStackTrace();
                    cancel();
                }

                while (!canceled)
                {
                    try
                    {
                        byte[] bytes = new byte[MAX_LENGTH];
                        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                        socket.receive(packet);
                        int length = packet.getLength();
                        //System.out.println("Packet received: " + length);
                        final ByteBuffer buffer = ByteBuffer.allocate(length);

                        buffer.put(bytes, 0, length);
                        buffer.flip();

                        executorService.submit(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                handler.onBytesReceived(buffer);
                            }
                        });
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (RejectedExecutionException e)
                    {
                        e.printStackTrace();
                    }
                }

                if (socket != null)
                {
                    socket.close();
                }

                System.out.println("Stopped");
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

    }

    /**
     * Cancels the connection.
     */
    public void cancel()
    {
        canceled = true;
        System.out.println("Shutting down");
        if (executorService != null)
        {
            executorService.shutdown();
        }

        System.out.println("Shut down completed");
        if (socket != null)
        {
            socket.close();
        }
    }

    /**
     * Getter.
     * @return Whether the connection is canceled.
     */
    public boolean isCanceled()
    {
        return canceled;
    }


}
