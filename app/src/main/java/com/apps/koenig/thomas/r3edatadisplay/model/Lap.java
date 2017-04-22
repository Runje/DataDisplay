package com.apps.koenig.thomas.r3edatadisplay.model;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 22.04.2017.
 */

public class Lap {
    public static int Length = 3 *8;

    public float Sector1;

    public float RelSector2;

    public float RelSector3;

    public float AbsSector2()
    {
            if (RelSector2 == DisplayData.INVALID || Sector1 == DisplayData.INVALID) {
                return DisplayData.INVALID;
            }

            return Sector1 + RelSector2;
    }

    public float Time()
    {
            if (RelSector2 == DisplayData.INVALID || Sector1 == DisplayData.INVALID || RelSector3 == DisplayData.INVALID) {
                return DisplayData.INVALID;
            }

            return Sector1 + RelSector2 + RelSector3;
    }

    public float[] RelArray()
    {
        {
            return new float[]{Sector1, RelSector2, RelSector3, Time()};
        }
    }

    public Lap() {
        Sector1 = DisplayData.INVALID;
        RelSector2 = DisplayData.INVALID;
        RelSector3 = DisplayData.INVALID;
    }

    public Lap(float absSector1, float relSec2, float relSec3) {
        this.Sector1 = absSector1;
        this.RelSector2 = relSec2;
        this.RelSector3 = relSec3;
    }

    public Lap(ByteBuffer reader) {
        Sector1 = reader.getFloat();
        RelSector2 = reader.getFloat();
        RelSector3 = reader.getFloat();
    }

    void SetRelSector(int i, float sec) {
        if (i == 0) {
            Sector1 = sec;
        } else if (i == 1) {
            RelSector2 = sec;
        } else if (i == 2) {
            RelSector3 = sec;
        } else {
            throw new IllegalArgumentException("Index > 2: " + i);
        }
    }

    public static Lap subtract (Lap a, Lap b)
    {
        Lap result = new Lap();
        if (a.Sector1 != DisplayData.INVALID && b.Sector1 != DisplayData.INVALID) {
            result.Sector1 = a.Sector1 - b.Sector1;
        }

        if (a.RelSector2 != DisplayData.INVALID && b.RelSector2 != DisplayData.INVALID) {
            result.RelSector2 = a.RelSector2 - b.RelSector2;
        }

        if (a.RelSector3 != DisplayData.INVALID && b.RelSector3 != DisplayData.INVALID) {
            result.RelSector3 = a.RelSector3 - b.RelSector3;
        }

        return result;
    }

}