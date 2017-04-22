package com.apps.koenig.thomas.r3edatadisplay.model;

import com.apps.koenig.thomas.r3edatadisplay.communication.ByteUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 22.04.2017.
 */

public class DisplayData {
    /// <summary>
    /// Invalid magic number for positive int values
    /// </summary>
    public static final int INVALID_INT = -1;

    /// <summary>
    /// Invald magic number for floats.
    /// </summary>
    public static final float INVALID = Float.MIN_VALUE;

    /// <summary>
    /// Invald magic number for positive floats.
    /// </summary>
    public static final float INVALID_POSITIVE = -1;

    /// <summary>
    /// Position( 1 == first place) of player.
    /// </summary>
    public int Position;

    /// <summary>
    /// Name of the track.
    /// </summary>
    public String Track;

    /// <summary>
    /// Name of the layout.
    /// </summary>
    public String Layout;

    /// <summary>
    /// Current running lap time.
    /// </summary>
    public float CurrentTime;

    /// <summary>
    /// Current lap time in seconds of player.
    /// </summary>
    public Lap CurrentLap;

    /// <summary>
    /// Whether this sector time in CurrentLap are completed
    /// </summary>
    public boolean[] CurrentLapCompletedAndValid;

    /// <summary>
    /// Previous lap time in seconds of player.
    /// </summary>
    public Lap PreviousLap;

    /// <summary>
    /// Personal best lap time in seconds in this session of player.
    /// </summary>
    public Lap PBLap;

    /// <summary>
    /// Theoretical best lap time in seconds of player.
    /// </summary>
    public Lap TBLap;

    /// <summary>
    /// Fastest lap time in seconds of anyone.
    /// </summary>
    public Lap FastestLap;

    /// <summary>
    /// How many complete laps can be driven after completing this lap.
    /// </summary>
    public int FuelRemainingLaps;

    /// <summary>
    /// Tires wear. 1.0 == 100%, 0 = 0%.
    /// </summary>
    public Tires TiresWear;

    /// <summary>
    /// Fuel used last lap in litre.
    /// </summary>
    public float FuelLastLap;

    /// <summary>
    /// Average fuel used per lap in litre.
    /// </summary>
    public float FuelAveragePerLap;

    /// <summary>
    /// Maximum of fuel used in one lap in litre.
    /// </summary>
    public float FuelMaxLap;

    /// <summary>
    /// Completed Laps in this Session so far.
    /// </summary>
    public List<Lap> CompletedLaps;

    /// <summary>
    /// Number of completed laps, can be more than the number of items in CompletedLaps in case of missing data.
    /// </summary>
    public int CompletedLapsCount;

    /// <summary>
    /// Tire usage in percent(1 = 100%) per lap.
    /// </summary>
    public Tires TireUsedAveragePerLap;

    /// <summary>
    /// Tire usage in percent(1 = 100%) per lap.
    /// </summary>
    public Tires TireUsedLastLap;

    /// <summary>
    /// Max Tire usage in percent(1 = 100%) per lap.
    /// </summary>
    public Tires TireUsedMaxLap;

    /// <summary>
    /// Current Sector of the player.
    /// </summary>
    public int CurrentSector;

    public DisplayData() {
        Position = INVALID_INT;
        CurrentTime = INVALID_POSITIVE;
        CurrentLap = new Lap();
        CurrentLapCompletedAndValid = new boolean[]{false, false, false, false};
        PreviousLap = new Lap();
        PBLap = new Lap();
        TBLap = new Lap();
        PreviousLap = new Lap();
        FastestLap = new Lap();
        FuelRemainingLaps = INVALID_INT;
        TiresWear = new Tires();
        FuelLastLap = INVALID_POSITIVE;
        FuelAveragePerLap = INVALID_POSITIVE;
        FuelMaxLap = INVALID_POSITIVE;
        CompletedLaps = new ArrayList<>();
        TireUsedAveragePerLap = new Tires();
        TireUsedLastLap = new Tires();
        TireUsedMaxLap = new Tires();
        CurrentSector = INVALID_INT;
    }

    public DisplayData(ByteBuffer buffer)
    {
        Position = buffer.getInt();
        CurrentTime = buffer.getFloat();
        CurrentLap = new Lap(buffer);
        CurrentLapCompletedAndValid = new boolean[4];
        for (int i = 0; i < CurrentLapCompletedAndValid.length; i++) {
            CurrentLapCompletedAndValid[i] = buffer.get() != 0;
        }

        PreviousLap = new Lap(buffer);
        PBLap = new Lap(buffer);
        TBLap = new Lap(buffer);
        FastestLap = new Lap(buffer);
        FuelRemainingLaps = buffer.getInt();
        TiresWear = new Tires(buffer);
        FuelLastLap = buffer.getFloat();
        FuelAveragePerLap = buffer.getFloat();
        FuelMaxLap = buffer.getFloat();
        int count = buffer.getInt();
        CompletedLaps = new ArrayList<>(4);
        for (int i = 0; i < count; i++) {
            CompletedLaps.add(new Lap(buffer));
        }

        CompletedLapsCount = buffer.getInt();
        TireUsedAveragePerLap = new Tires(buffer);
        TireUsedLastLap = new Tires(buffer);
        TireUsedMaxLap = new Tires(buffer);
        CurrentSector = buffer.getInt();
        Track = ByteUtils.getString(buffer);
        Layout = ByteUtils.getString(buffer);
    }


    int Length() {
        return 4 + 8 + 5 * Lap.Length + CurrentLapCompletedAndValid.length + 4 + 3 * 8 + 4 + CompletedLaps.size() * Lap.Length + 4 + 3 * Tires.Length + 8 + Track.length() + 1 + Layout.length() + 1;
    }
}
