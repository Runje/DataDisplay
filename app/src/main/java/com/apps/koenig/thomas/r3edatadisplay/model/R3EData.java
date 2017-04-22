package com.apps.koenig.thomas.r3edatadisplay.model;

import com.apps.koenig.thomas.r3edatadisplay.communication.R3EMessage;

/**
 * Created by Thomas on 27.02.2017.
 */
public class R3EData {
    public static final float INVALID_SECTOR = -1;

    public static final int RACE = 2;
    public static final int QUALY = 1;
    public static final int PRACTICE = 0;
    public static final float INVALID_DIFF = Float.MIN_VALUE;
    public static final String NO_DIFF = "-";
    public static final int HUMAN = 0;
    public final float rearRightWear;
    public final float rearLeftWear;
    public final float frontRightWear;
    public final float frontLeftWear;
    public final float diffBehind;
    public final float diffAhead;
    public final Sectors sectorTimesAhead;
    public final Sectors sectorTimesBehind;
    public final float fuelLeft;
    public int position;
    public int sessionType;
    public int completedLaps;

    public float diffLapLeader;
    public float selfBestLap;
    public Sectors sectorTimesSelfBestLap;
    public Sectors sectorTimesSelfCurrLap;
    public Sectors sectorTimesBestLapLeader;


    public R3EData(R3EMessage msg) {
        position = msg.position;
        fuelLeft = msg.fuelLeft;
        selfBestLap = msg.sectorTimesSelfBestLap.lap();
        if (selfBestLap == INVALID_SECTOR || msg.lapTimeBestLeaderClass == INVALID_SECTOR) {
            diffLapLeader = INVALID_DIFF;
        } else {
            diffLapLeader = selfBestLap - msg.lapTimeBestLeaderClass;
        }

        sectorTimesBestLapLeader = msg.sectorTimesSessionBestLap;
        sectorTimesSelfBestLap = msg.sectorTimesSelfBestLap;
        sectorTimesSelfCurrLap = msg.sectorTimesSelfCurrLap;
        if (position > 1 && msg.drivers.length > 0) {
            sectorTimesAhead = msg.drivers[position - 2].sectorTimesCurrent;
        }
        else {
            sectorTimesAhead = new Sectors();
        }

        if (position < msg.numCars && position > 0) {
            sectorTimesBehind = msg.drivers[position].sectorTimesCurrent;
        } else {
            sectorTimesBehind = new Sectors();
        }

        sessionType = msg.sessionType;
        frontLeftWear = msg.frontLeftWear;
        frontRightWear = msg.frontRightWear;
        rearLeftWear = msg.rearLeftWear;
        rearRightWear = msg.rearRightWear;
        completedLaps = msg.completedLaps;
        diffBehind = msg.timeDeltaBehind;
        diffAhead = msg.timeDeltaAhead;
    }


}
