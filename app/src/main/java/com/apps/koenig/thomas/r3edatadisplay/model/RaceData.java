package com.apps.koenig.thomas.r3edatadisplay.model;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 22.04.2017.
 */

public class RaceData extends DisplayData {
    /// <summary>
    /// Actual diff to driver ahead.
    /// </summary>
    public float DiffAhead;

    /// <summary>
    /// Actual diff to driver behind.
    /// </summary>
    public float DiffBehind;

    /// <summary>
    /// Diffs in the sector times to the driver ahead.
    /// </summary>
    public Lap DiffSectorsAhead;

    /// <summary>
    /// Diffs in the sector times to the driver behind.
    /// </summary>
    public Lap DiffSectorsBehind;

    /// <summary>
    /// How much fuel the driver has to refill to finish the race.
    /// </summary>
    public float FuelToRefill;

    /// <summary>
    /// Estimated total laps in this race.
    /// </summary>
    public int EstimatedRaceLaps;

    /// <summary>
    /// Estimated boxenstop delta in seconds.
    /// </summary>
    public float EstimatedBoxenstopDelta;

    /// <summary>
    /// Last Boxenstop delta in seconds.
    /// </summary>
    public float LastBoxenstopDelta;

    public RaceData()
    {
        super();
        DiffAhead = DisplayData.INVALID;
        DiffBehind = DisplayData.INVALID;
        DiffSectorsAhead = new Lap();
        DiffSectorsBehind = new Lap();
        FuelToRefill = DisplayData.INVALID_POSITIVE;
        EstimatedRaceLaps = DisplayData.INVALID_INT;
        EstimatedBoxenstopDelta = DisplayData.INVALID_POSITIVE;
        LastBoxenstopDelta = DisplayData.INVALID_POSITIVE;
    }

    public RaceData(ByteBuffer buffer)
    {
        super(buffer);
        DiffAhead = buffer.getFloat();
        DiffBehind = buffer.getFloat();
        DiffSectorsAhead = new Lap(buffer);
        DiffSectorsBehind = new Lap(buffer);
        FuelToRefill = buffer.getFloat();
        EstimatedRaceLaps = buffer.getInt();
        EstimatedBoxenstopDelta = buffer.getFloat();
        LastBoxenstopDelta = buffer.getFloat();
    }
}
