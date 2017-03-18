package com.apps.koenig.thomas.r3edatadisplay.view;

import android.view.View;
import android.widget.TextView;

import com.apps.koenig.thomas.r3edatadisplay.R;
import com.apps.koenig.thomas.r3edatadisplay.R3EDisplayData;

/**
 * Created by Thomas on 10.03.2017.
 */

public class RaceHeader {
    private final TextView position;
    private final TextView diffBehind;
    private final TextView diffAhead;
    private final TextView fuelLaps;
    private final TextView laps;

    public RaceHeader(View parent)
    {
        position = (TextView) parent.findViewById(R.id.textPos);
        diffAhead = (TextView) parent.findViewById(R.id.textDiffAhead);
        diffBehind = (TextView) parent.findViewById(R.id.textDiffBehind);
        fuelLaps = (TextView) parent.findViewById(R.id.textFuelLaps);
        laps = (TextView) parent.findViewById(R.id.textLaps);
    }

    public void setPos(int pos) {
        position.setText(GUIUtils.posToString(pos));
    }

    public void setLaps(int completedLaps, int estimatedLapsToGo) {
        StringBuilder text = new StringBuilder(Integer.toString(completedLaps + 1));
        if (estimatedLapsToGo != -1)
        {
            text.append("/" + (estimatedLapsToGo));
        }

        laps.setText(text.toString());
    }

    public void setFuelLaps(int laps)
    {
        fuelLaps.setText(laps == R3EDisplayData.INVALID_LAPS ? "-" : Integer.toString(laps));
    }

    public void setAhead(float diff) {
        diffAhead.setText(GUIUtils.diffToString(diff));
    }

    public void setBehind(float diff) {
        diffBehind.setText(GUIUtils.diffToString(diff));
    }
}
