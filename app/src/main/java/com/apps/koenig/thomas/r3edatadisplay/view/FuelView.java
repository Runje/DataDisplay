package com.apps.koenig.thomas.r3edatadisplay.view;

import android.view.View;
import android.widget.TextView;

import com.apps.koenig.thomas.r3edatadisplay.R;
import com.apps.koenig.thomas.r3edatadisplay.R3EDisplayData;

/**
 * Created by Thomas on 10.03.2017.
 */

public class FuelView {
    private final TextView lastLap;
    private final TextView average;
    public static String format = "%04.2f";
    private final TextView toEnd;

    public FuelView(View parent) {
        lastLap = (TextView) parent.findViewById(R.id.textFuelLastLap);
        average = (TextView) parent.findViewById(R.id.textFuelAverage);
        toEnd = (TextView) parent.findViewById(R.id.textFuelToEnd);
    }

    public void setLastLap(float lastLapFuel) {
        lastLap.setText(lastLapFuel == R3EDisplayData.INVALID_FUEL ? "-" : String.format(format, lastLapFuel));
    }

    public void setAverageLap(float averageLapFuel) {
        average.setText(averageLapFuel == R3EDisplayData.INVALID_FUEL ? "-" : String.format(format, averageLapFuel));
    }

    public void setFuelToEnd(float fuelToEnd) {
        toEnd.setText(fuelToEnd == R3EDisplayData.INVALID_FUEL ? "-" : String.format(format, fuelToEnd));
    }

}
