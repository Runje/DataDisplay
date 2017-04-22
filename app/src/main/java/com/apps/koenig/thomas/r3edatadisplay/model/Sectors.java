package com.apps.koenig.thomas.r3edatadisplay.model;

/**
 * Created by Thomas on 26.02.2017.
 */

public class Sectors {
    public float sector1;
    public float sector2;
    public float sector3;


    public Sectors(float sector1, float sector2, float sector3) {
        this.sector1 = sector1;
        this.sector2 = sector2;
        this.sector3 = sector3;
    }

    public Sectors() {
        this(R3EData.INVALID_SECTOR, R3EData.INVALID_SECTOR, R3EData.INVALID_SECTOR);
    }

    public float[] array()
    {
        return new float[]{sector1, sector2, sector3, lap()};
    }

    @Override
    public String toString() {
        return "Sectors{" +
                "sector1=" + sector1 +
                ", sector2=" + sector2 +
                ", sector3=" + sector3 +
                '}';
    }

    public float lap()
    {
        if (sector1 == R3EData.INVALID_SECTOR || sector2 == R3EData.INVALID_SECTOR || sector3 == R3EData.INVALID_SECTOR)
        {
            return R3EData.INVALID_SECTOR;
        }

        return sector1 + sector2 + sector3;
    }

    public void set(float[] sectors) {
        assert(sectors.length == 3);
        sector1 = sectors[0];
        sector2 = sectors[1];
        sector3 = sectors[2];
    }

    public float absSec2() {
        if (sector1 == R3EData.INVALID_SECTOR || sector2 == R3EData.INVALID_SECTOR)
        {
            return R3EData.INVALID_SECTOR;
        }

        return sector1 + sector2;
    }
}
