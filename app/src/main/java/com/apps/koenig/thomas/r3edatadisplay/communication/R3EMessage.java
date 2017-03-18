package com.apps.koenig.thomas.r3edatadisplay.communication;

import com.apps.koenig.thomas.r3edatadisplay.R3EDriver;
import com.apps.koenig.thomas.r3edatadisplay.Sectors;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by Thomas on 26.02.2017.
 */

public class R3EMessage {
    public final float timeDeltaAhead;
    public final float timeDeltaBehind;
    public final int controlType;
    public Sectors sectorTimesSelfCurrLap;
    public float carSpeed;
    public int classId;
    public int modelId;
    public int sessionPhase;
    public int numCars;
    public String playerName;
    public float fuelLeft;
    public float fuelCapacity;
    public R3EDriver[] drivers;
    public Sectors sectorTimesSessionBestLap;
    public Sectors sectorTimesSelfBestLap;
    public float lapTimeBestLeaderClass;
    public float lapDistanceFraction;
    public float lapDistance;
    public float layoutLength;
    public int completedLaps;
    public int position;
    public float sessionTimeRemaining;
    public int raceLaps;
    public float frontLeft_CenterTemp;

    public float frontRight_CenterTemp;

    public float rearLeft_CenterTemp;

    public float rearRight_CenterTemp;

    public float frontLeftGrip;

    public float frontRightGrip;

    public float rearLeftGrip;

    public float rearRightGrip;

    public float frontLeftWear;
    public float frontRightWear;
    public float rearLeftWear;
    public float rearRightWear;

    public String trackName;
    public String layoutName;
    public boolean inMenu;
    public boolean paused;

    public int sessionType;

    public static final int STRING_LENGTH = 64;

    public R3EMessage(ByteBuffer buffer)
    {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        frontLeft_CenterTemp = buffer.getFloat();
        frontRight_CenterTemp = buffer.getFloat();
        rearLeft_CenterTemp = buffer.getFloat();
        rearRight_CenterTemp = buffer.getFloat();
        frontLeftWear = buffer.getFloat();
        frontRightWear = buffer.getFloat();
        rearLeftWear = buffer.getFloat();
        rearRightWear = buffer.getFloat();
        frontLeftGrip = buffer.getFloat();
        frontRightGrip = buffer.getFloat();
        rearLeftGrip = buffer.getFloat();
        rearRightGrip = buffer.getFloat();
        trackName = ByteUtils.getString(buffer, STRING_LENGTH);
        layoutName = ByteUtils.getString(buffer, STRING_LENGTH);
        sessionType = buffer.getInt();
        raceLaps = buffer.getInt();
        completedLaps = buffer.getInt();
        sessionTimeRemaining = buffer.getFloat();
        position = buffer.getInt();
        lapDistanceFraction = buffer.getFloat();
        lapDistance = buffer.getFloat();
        lapTimeBestLeaderClass = buffer.getFloat();
        sectorTimesSessionBestLap = new Sectors(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        sectorTimesSelfBestLap = new Sectors(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        sectorTimesSelfCurrLap = new Sectors(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        classId = buffer.getInt();
        modelId = buffer.getInt();
        sessionPhase = buffer.getInt();
        playerName = ByteUtils.getString(buffer, STRING_LENGTH);
        carSpeed = buffer.getFloat();
        fuelLeft = buffer.getFloat();
        fuelCapacity = buffer.getFloat();

        timeDeltaAhead = buffer.getFloat();
        timeDeltaBehind = buffer.getFloat();
        inMenu = buffer.getInt() != 0;
        paused = buffer.getInt() != 0;
        controlType = buffer.getInt();
        layoutLength = buffer.getFloat();
        numCars = buffer.getInt();
        if (numCars < 0)
        {
            numCars = 0;
        }

        drivers = new R3EDriver[numCars];
        for (int i = 0; i < numCars; i++) {
            drivers[i] = new R3EDriver(buffer);
        }
    }

    @Override
    public String toString() {
        return "R3EMessage{" +
                "classId=" + classId +
                ", modelId=" + modelId +
                ", sessionType=" + sessionPhase +
                ", numCars=" + numCars +
                ", playerName='" + playerName + '\'' +
                ", fuelLeft=" + fuelLeft +
                ", fuelCapacity=" + fuelCapacity +
                ", drivers=" + Arrays.toString(drivers) +
                ", sectorTimesSessionBestLap=" + sectorTimesSessionBestLap +
                ", lapTimeBestLeaderClass=" + lapTimeBestLeaderClass +
                ", lapDistanceFraction=" + lapDistanceFraction +
                ", completedLaps=" + completedLaps +
                ", position=" + position +
                ", sessionTimeRemaining=" + sessionTimeRemaining +
                ", raceLaps=" + raceLaps +
                ", frontLeft_CenterTemp=" + frontLeft_CenterTemp +
                ", frontRight_CenterTemp=" + frontRight_CenterTemp +
                ", rearLeft_CenterTemp=" + rearLeft_CenterTemp +
                ", rearRight_CenterTemp=" + rearRight_CenterTemp +
                ", frontLeftGrip=" + frontLeftGrip +
                ", frontRightGrip=" + frontRightGrip +
                ", rearLeftGrip=" + rearLeftGrip +
                ", rearRightGrip=" + rearRightGrip +
                ", frontLeftWear=" + frontLeftWear +
                ", frontRightWear=" + frontRightWear +
                ", rearLeftWear=" + rearLeftWear +
                ", rearRightWear=" + rearRightWear +
                ", trackName='" + trackName + '\'' +
                ", layoutName='" + layoutName + '\'' +
                ", sessionType=" + sessionType +
                '}';
    }
}
