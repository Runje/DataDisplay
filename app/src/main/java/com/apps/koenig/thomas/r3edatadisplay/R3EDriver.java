package com.apps.koenig.thomas.r3edatadisplay;

import com.apps.koenig.thomas.r3edatadisplay.communication.ByteUtils;
import com.apps.koenig.thomas.r3edatadisplay.communication.R3EMessage;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 27.02.2017.
 */
public class R3EDriver {
    public final float deltaAhead;
    public final float lapDistance;
    public float deltaBehind;
    public Sectors sectorTimesCurrent;
    public Sectors sectorTimesBest;
    public int place;
    public int numPitstops;
    public int completedLaps;
    public String name;

    public R3EDriver(ByteBuffer buffer) {
        place = buffer.getInt();
        completedLaps = buffer.getInt();
        numPitstops = buffer.getInt();
        name = ByteUtils.getString(buffer, R3EMessage.STRING_LENGTH);
        sectorTimesBest = new Sectors(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        sectorTimesCurrent = new Sectors(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        deltaBehind = buffer.getFloat();
        deltaAhead = buffer.getFloat();
        lapDistance = buffer.getFloat();
    }

    @Override
    public String toString() {
        return "R3EDriver{" +
                "deltaBehind=" + deltaBehind +
                ", sectorTimesCurrent=" + sectorTimesCurrent +
                ", sectorTimesBest=" + sectorTimesBest +
                ", place=" + place +
                ", numPitstops=" + numPitstops +
                ", completedLaps=" + completedLaps +
                ", name='" + name + '\'' +
                '}';
    }
}
