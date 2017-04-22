package com.apps.koenig.thomas.r3edatadisplay.model;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 22.04.2017.
 */

public class Tires {
    public static final int Length = 4 * 8;
    public float FrontLeft;
    public float FrontRight;
    public float RearLeft;
    public float RearRight;

    public Tires() {
        FrontLeft = -1;
        FrontRight= -1;
        RearLeft = -1;
        RearRight = -1;
    }

    public Tires(float frontLeft, float frontRight, float rearLeft, float rearRight)
    {
        this.FrontLeft = frontLeft;
        this.FrontRight = frontRight;
        this.RearLeft = rearLeft;
        this.RearRight = rearRight;
    }

    public Tires(ByteBuffer reader)
    {
        this.FrontLeft = reader.getFloat();
        this.FrontRight = reader.getFloat();
        this.RearLeft = reader.getFloat();
        this.RearRight = reader.getFloat();
    }

    public boolean Valid() { return FrontLeft != DisplayData.INVALID_POSITIVE && FrontRight != DisplayData.INVALID_POSITIVE && RearLeft != DisplayData.INVALID_POSITIVE && RearRight != DisplayData.INVALID_POSITIVE; }
    public float[] Array()
    {
        return new float[] { FrontLeft, FrontRight, RearLeft, RearRight };
    }

    public static Tires add(Tires a, Tires b)
    {
        if (!a.Valid() || !b.Valid())
        {
            throw new IllegalArgumentException("Invalid tires argument");
        }

        return new Tires(a.FrontLeft + b.FrontLeft, a.FrontRight + b.FrontRight, a.RearLeft + b.RearLeft, a.RearRight + b.RearRight);
    }

    public static Tires subtract(Tires a, Tires b)
    {
        if (!a.Valid() || !b.Valid())
        {
            throw new IllegalArgumentException("Invalid tires argument");
        }

        return new Tires(a.FrontLeft - b.FrontLeft, a.FrontRight - b.FrontRight, a.RearLeft - b.RearLeft, a.RearRight - b.RearRight);
    }

}
