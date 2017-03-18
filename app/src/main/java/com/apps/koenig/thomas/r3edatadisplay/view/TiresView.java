package com.apps.koenig.thomas.r3edatadisplay.view;

import android.view.View;
import android.widget.TextView;

import com.apps.koenig.thomas.r3edatadisplay.R;

/**
 * Created by Thomas on 10.03.2017.
 */

public class TiresView {
    private final TextView frontLeft;
    private final TextView frontRight;
    private final TextView rearLeft;
    private final TextView rearRight;
    private final TextView[] tires;
    private final TextView description;

    public TiresView(View parent) {
        frontLeft = (TextView) parent.findViewById(R.id.textFrontLeft);
        frontRight = (TextView) parent.findViewById(R.id.textFrontRight);
        rearLeft = (TextView) parent.findViewById(R.id.textRearLeft);
        rearRight = (TextView) parent.findViewById(R.id.textRearRight);
        tires = new TextView[] {frontLeft, frontRight, rearLeft, rearRight};
        description = (TextView) parent.findViewById(R.id.describeTire);
    }

    public void setText(String[] tireTexts) {
        for (int i = 0; i < tires.length; i++) {
            tires[i].setText(tireTexts[i]);
        }
    }

    public void setTextColor(int[] colors) {
        for (int i = 0; i < tires.length; i++) {
            tires[i].setTextColor(colors[i]);
        }
    }

    public void setValue(String s) {
        description.setText("Tires forecast in Laps to " + s);
    }
}
