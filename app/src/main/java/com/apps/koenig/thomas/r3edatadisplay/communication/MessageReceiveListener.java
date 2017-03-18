package com.apps.koenig.thomas.r3edatadisplay.communication;

/**
 * Created by Thomas on 26.02.2017.
 */
public interface MessageReceiveListener {

    void onReceiveMessage(R3EMessage bytes);
}
