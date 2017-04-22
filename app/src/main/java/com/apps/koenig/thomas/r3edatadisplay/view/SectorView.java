package com.apps.koenig.thomas.r3edatadisplay.view;

import android.view.View;
import android.widget.TextView;

import com.apps.koenig.thomas.r3edatadisplay.R;
import com.apps.koenig.thomas.r3edatadisplay.model.Sectors;

/**
 * Created by Thomas on 11.03.2017.
 */

public class SectorView {

    private final TextView sector3;
    private final TextView sector2;
    private final TextView sector1;
    private final TextView lap;
    private final TextView[] array;

    public SectorView(View parent) {
        this.sector1 = (TextView) parent.findViewById(R.id.textSector1);
        this.sector2 = (TextView) parent.findViewById(R.id.textSector2);
        this.sector3 = (TextView) parent.findViewById(R.id.textSector3);
        this.lap = (TextView) parent.findViewById(R.id.textLap);
        this.array = new TextView[] {sector1, sector2, sector3, lap};
    }

    public void setTextAndColor(String[] strings, int[] colors) {
        for (int i = 0; i < 4; i++) {
            array[i].setTextColor(colors[i]);
            array[i].setText(strings[i]);
        }
    }

    public void setTextAndColor(String[] strings) {
        for (int i = 0; i < 4; i++) {
            array[i].setText(strings[i]);
        }
    }

    public void setColors(int[] colors) {
        for (int i = 0; i < 4; i++) {
            array[i].setTextColor(colors[i]);
        }
    }

    public void setSectors(Sectors sectors, Sectors compareSectors, int color) {
        setTextAndColor(GUIUtils.sectorToTextOrDiff(sectors, compareSectors), GUIUtils.getColors(sectors, compareSectors, color));
    }

    public void setSectors(Sectors sectors, Sectors[] compareSectors, int[] colors) {
        setTextAndColor(GUIUtils.sectorToText(sectors), GUIUtils.getColors(sectors, compareSectors, colors));
    }
}
