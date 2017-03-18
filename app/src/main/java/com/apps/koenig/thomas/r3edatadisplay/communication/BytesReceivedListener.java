package com.apps.koenig.thomas.r3edatadisplay.communication;

import java.nio.ByteBuffer;

/**
 * @author Thomas Horn
 */
public interface BytesReceivedListener
{
    void onBytesReceived(ByteBuffer buffer);
}
