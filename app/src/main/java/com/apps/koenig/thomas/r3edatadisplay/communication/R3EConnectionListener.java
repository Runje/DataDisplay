package com.apps.koenig.thomas.r3edatadisplay.communication;

/**
 * Created by Thomas on 10.03.2017.
 */

public interface R3EConnectionListener {
    void receiveMessage(R3EMessage message);
    void onConnectionChange(boolean connected);
}
