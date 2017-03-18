package com.apps.koenig.thomas.r3edatadisplay.view;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.apps.koenig.thomas.r3edatadisplay.R;
import com.apps.koenig.thomas.r3edatadisplay.R3EData;
import com.apps.koenig.thomas.r3edatadisplay.Sectors;

/**
 * Created by Thomas on 10.03.2017.
 */

public class GUIUtils {
    public static int darkgreen = Color.parseColor("#1a640d");
    public static int orange = Color.parseColor("#FFF1C010");
    public static final float ORANGE_TIRES = 0.45f;
    public static final float RED_TIRES = 0.25f;
    public static final float YELLOW_TIRES = 0.7f;
    public static void showSectors(View sectorView, Sectors sectors, int[] colors) {
        TextView sec1 = (TextView) sectorView.findViewById(R.id.textSector1);
        TextView sec2 = (TextView) sectorView.findViewById(R.id.textSector2);
        TextView sec3 = (TextView) sectorView.findViewById(R.id.textSector3);
        TextView lap = (TextView) sectorView.findViewById(R.id.textLap);

        sec1.setText(lapToString(sectors.sector1));

        sec2.setText(lapToString(sectors.sector2));
        sec3.setText(lapToString(sectors.sector3));
        lap.setText(lapToString(sectors.lap()));

        sec1.setTextColor(colors[0]);
        sec2.setTextColor(colors[1]);
        sec3.setTextColor(colors[2]);
        lap.setTextColor(colors[3]);
    }

    public static int[] getColors(Sectors sectors, Sectors current, int color) {
        float[] secs = sectors.array();
        float[] curr = current.array();
        int[] colors = new int[4];
        for (int i = 0; i < 3; i++) {
            if (curr[i] == R3EData.INVALID_SECTOR || secs[i] == R3EData.INVALID_SECTOR )
            {
                colors[i] = Color.WHITE;
            } else if (secs[i] >= curr[i])
            {
                colors[i] = color;
            } else
            {
                colors[i] = Color.RED;
            }

            // test all white
            colors[i] = Color.WHITE;
        }

        if (curr[0] == R3EData.INVALID_SECTOR || curr[1] == R3EData.INVALID_SECTOR || curr[2] == R3EData.INVALID_SECTOR)
        {
            colors[3] = Color.WHITE;
        } else if (sectors.lap() < current.lap())
        {
            colors[3] = Color.RED;
        } else
        {
            colors[3] = color;
        }

        // test white
        colors[3] = Color.WHITE;
        return colors;
    }

    public static int[] getColorsCurrent(Sectors sectorTimesBestLapLeader, Sectors sectorTimesSelfBestLap, Sectors current, Sectors sectorTimesSelfTheoreticalLap) {

        float[] leader = sectorTimesBestLapLeader.array();
        float[] pb = sectorTimesSelfBestLap.array();
        float[] tb = sectorTimesSelfTheoreticalLap.array();
        float[] curr = current.array();
        int[] colors = new int[4];
        for (int i = 0; i < 3; i++) {
            if (curr[i] == R3EData.INVALID_SECTOR)
            {
                colors[i] = Color.WHITE;
            } else if (leader[i] >= curr[i])
            {
                colors[i] = Color.MAGENTA;
            }
            else if (tb[i] >= curr[i])
            {
                colors[i] = darkgreen;
            }
            else if (pb[i] >= curr[i])
            {
                colors[i] = Color.GREEN;
            } else
            {
                colors[i] = Color.RED;
            }

        }

        if (curr[0] == R3EData.INVALID_SECTOR || curr[1] == R3EData.INVALID_SECTOR || curr[2] == R3EData.INVALID_SECTOR)
        {
            colors[3] = Color.WHITE;
        } else if (sectorTimesBestLapLeader.lap() >= current.lap())
        {
            colors[3] = Color.MAGENTA;
        } else if (sectorTimesSelfTheoreticalLap.lap() >= current.lap()){
            colors[3] = darkgreen;
        }
        else if (sectorTimesSelfBestLap.lap() >= current.lap())
        {
            colors[3] = Color.GREEN;
        } else
        {
            colors[3] = Color.RED;
        }

        return colors;
    }


    public static String lapToString(float lap) {
        if (lap == R3EData.INVALID_SECTOR)
        {
            return "-";
        }

        int min = (int) (lap / 60);
        StringBuilder text = new StringBuilder();
        if (min > 0) text.append(min + ":");
        text.append(String.format("%06.3f", lap - 60 * min));
        return text.toString();
    }

    public static String diffToString(float diff)
    {
        if (diff == R3EData.INVALID_DIFF) {
            return "-";
        }

        StringBuilder text = new StringBuilder(diff > 0 ? "+" : "");
        text.append(String.format("%06.3f", diff));
        return text.toString();
    }

    public static String[] dataToTireWear(float frontLeft, float frontRight, float rearLeft, float rearRight) {
        String[] tireWears = new String[4];
        String percentFormat = "%04.1f %%";
        tireWears[0] = frontLeft < 0 ? "-" : String.format(percentFormat, frontLeft * 100);
        tireWears[1] = frontRight < 0 ? "-" : String.format(percentFormat, frontRight * 100);
        tireWears[2] = rearLeft < 0 ? "-" : String.format(percentFormat, rearLeft * 100);
        tireWears[3] = rearRight < 0 ? "-" : String.format(percentFormat, rearRight * 100);
        return tireWears;
    }

    public static int[] dataToColors(float frontLeft, float frontRight, float rearLeft, float rearRight) {
        int[] colors = new int[4];
        colors[0] = tireWearToColor(frontLeft);
        colors[1] = tireWearToColor(frontRight);
        colors[2] = tireWearToColor(rearLeft);
        colors[3] = tireWearToColor(rearRight);
        return colors;
    }

    private static int tireWearToColor(float tire) {
        if (tire < 0) {
            return Color.WHITE;
        }

        if (tire < RED_TIRES) {
            return Color.RED;
        }
        if (tire < ORANGE_TIRES) {
            return orange;
        }
        if (tire < YELLOW_TIRES) {
            return Color.YELLOW;
        }

        return Color.GREEN;
    }

    public static String[] sectorToText(Sectors sectors) {
        String[] texts = new String[4];
        float[] secs = sectors.array();
        for (int i = 0; i < 3; i++) {
            texts[i] = GUIUtils.lapToString(secs[i]);
        }

        texts[3] = GUIUtils.lapToString(sectors.lap());
        return texts;
    }

    public static float[] sectorToDiffs(Sectors sectors, Sectors currSectors) {
        float[] diffs = new float[4];
        float[] secs = sectors.array();
        float[] currs = currSectors.array();
        for (int i = 0; i < 4; i++) {
            if (currs[i] == R3EData.INVALID_SECTOR || secs[i] == R3EData.INVALID_SECTOR)
            {
                diffs[i] = R3EData.INVALID_DIFF;
            }
            else {

                diffs[i] = currs[i] - secs[i];
            }
        }

        return diffs;
    }

    public static String[] diffsToString(float[] diffs) {
        String[] texts = new String[4];
        for (int i = 0; i < 4; i++) {
            if (diffs[i] == R3EData.INVALID_DIFF)
            {
                texts[i] = R3EData.NO_DIFF;
            }
            else {
                texts[i] = GUIUtils.diffToString(diffs[i]);
            }
        }

        return texts;
    }

    public static int[] diffsToColor(float[] diffs) {
        int[] colors = new int[4];
        for (int i = 0; i < 4; i++) {
            colors[i] = GUIUtils.diffToColor(diffs[i]);
        }

        return colors;
    }

    private static int diffToColor(float diff) {
        if (diff == R3EData.INVALID_DIFF)
        {
            return Color.WHITE;
        }

        return diff <= 0 ? Color.GREEN : Color.RED;
    }

    public static int[] getColors(Sectors compareSecs, Sectors[] secs, int[] colors) {
        assert secs.length == colors.length;
        int[] result = new int[4];
        // prefill with RED
        for (int i = 0; i < result.length; i++) {
            result[i] = Color.RED;
        }

        float[] compareSectors = compareSecs.array();
        for (int i = 0; i < compareSectors.length; i++) {
            float sec = compareSectors[i];
            if (sec == R3EData.INVALID_SECTOR) {
                result[i] = Color.WHITE;
            } else {

                for (int j = 0; j < secs.length; j++) {
                    float[] sectors = secs[j].array();
                    if (sectors[i] >= sec) {
                        result[i] = colors[j];
                        break;
                    }
                }
            }
        }

        return result;
    }

    public static int[] getColorsCurrentRace(Sectors sectorTimesBestLapLeader, Sectors sectorTimesSelfBestLap, Sectors current, Sectors sectorTimesSelfTheoreticalLap) {

        float[] leader = sectorTimesBestLapLeader.array();
        float[] pb = sectorTimesSelfBestLap.array();
        float[] tb = sectorTimesSelfTheoreticalLap.array();
        float[] curr = current.array();
        int[] colors = new int[4];
        for (int i = 0; i < 3; i++) {
            if (curr[i] == R3EData.INVALID_SECTOR)
            {
                colors[i] = Color.WHITE;
            } else if (leader[i] >= curr[i])
            {
                colors[i] = Color.MAGENTA;
            }
            else if (tb[i] >= curr[i])
            {
                colors[i] = darkgreen;
            }
            else if (pb[i] >= curr[i])
            {
                colors[i] = Color.GREEN;
            } else
            {
                colors[i] = Color.WHITE;
            }

        }

        if (curr[0] == R3EData.INVALID_SECTOR || curr[1] == R3EData.INVALID_SECTOR || curr[2] == R3EData.INVALID_SECTOR)
        {
            colors[3] = Color.WHITE;
        } else if (sectorTimesBestLapLeader.lap() >= current.lap())
        {
            colors[3] = Color.MAGENTA;
        } else if (sectorTimesSelfTheoreticalLap.lap() >= current.lap()){
            colors[3] = darkgreen;
        }
        else if (sectorTimesSelfBestLap.lap() >= current.lap())
        {
            colors[3] = Color.GREEN;
        } else
        {
            colors[3] = Color.WHITE;
        }

        return colors;
    }

    public static String posToString(int pos) {
        return pos < 1 ? "-" : Integer.toString(pos);
    }

    public static String[] sectorToTextOrDiff(Sectors sectors, Sectors compareSectors) {
        String[] texts = new String[4];
        float[] secs = sectors.array();
        float[] compareSecs = compareSectors.array();
        for (int i = 0; i < 4; i++) {
            if (compareSecs[i] != R3EData.INVALID_SECTOR) {
                texts[i] = GUIUtils.diffToString(compareSecs[i] - secs[i]);
            } else {
                texts[i] = GUIUtils.lapToString(secs[i]);
            }
        }

        return texts;
    }

    public static String[] forecastsToTexts(float[] floats) {
        String[] result = new String[floats.length];
        for (int i = 0; i < floats.length; i++) {
            result[i] = forecastToText(floats[i]);
        }

        return result;
    }

    private static String forecastToText(float forecast) {
        return forecast < 0 ? "-" : String.format("%02.1f", forecast);
    }
}
