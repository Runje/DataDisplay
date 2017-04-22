package com.apps.koenig.thomas.r3edatadisplay.view;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.apps.koenig.thomas.r3edatadisplay.R;
import com.apps.koenig.thomas.r3edatadisplay.model.R3EDisplayData;

/**
 * Created by Thomas on 10.03.2017.
 */

public class QualyHeader {
    private final TextView position;
    private final TextView lap;
    private final TextView diff;
    private final TextView remainingLaps;

    public QualyHeader(View parent)
    {
        position = (TextView) parent.findViewById(R.id.textPos);
        diff = (TextView) parent.findViewById(R.id.textDiff);
        lap = (TextView) parent.findViewById(R.id.textLap);
        remainingLaps = (TextView) parent.findViewById(R.id.textRemainingLaps);
    }

    public void setPos(int pos) {
        position.setText(GUIUtils.posToString(pos));
    }

    public void setRemainingLaps(int laps)
    {
        remainingLaps.setText(laps == R3EDisplayData.INVALID_LAPS ? "-" : Integer.toString(laps));

        if (laps == 1) {
            remainingLaps.setTextColor(Color.YELLOW);
        } else if (laps == 0) {
            remainingLaps.setTextColor(Color.RED);
        } else {
            remainingLaps.setTextColor(Color.WHITE);
        }
    }

    public void setDiff(float diff) {
        this.diff.setText(GUIUtils.diffToString(diff));
    }

    public void setLap(float laptime) {
        lap.setText(GUIUtils.lapToString(laptime));
    }
}
