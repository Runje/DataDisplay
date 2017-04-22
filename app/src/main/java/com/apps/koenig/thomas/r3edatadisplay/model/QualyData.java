package com.apps.koenig.thomas.r3edatadisplay.model;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 22.04.2017.
 */

public class QualyData extends DisplayData {
    /// <summary>
    /// Current Position in the sectors and a forecast for final position if lap is not completed.
    /// </summary>
    public int[] SectorPos;

    public QualyData()
    {
        super();
        SectorPos = new int[] { DisplayData.INVALID_INT, DisplayData.INVALID_INT, DisplayData.INVALID_INT, DisplayData.INVALID_INT };
    }

    public QualyData(ByteBuffer buffer)
    {
        super(buffer);
        SectorPos = new int[4];
        for (int i = 0; i < SectorPos.length; i++) {
            SectorPos[i] = buffer.getInt();
        }
    }

}
