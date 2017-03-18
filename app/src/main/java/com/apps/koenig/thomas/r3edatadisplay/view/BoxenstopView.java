package com.apps.koenig.thomas.r3edatadisplay.view;

import android.view.View;
import android.widget.TextView;

import com.apps.koenig.thomas.r3edatadisplay.R;

/**
 * Created by Thomas on 10.03.2017.
 */

public class BoxenstopView {
    private final TextView delta;


    public BoxenstopView(View parent) {
        delta = (TextView) parent.findViewById(R.id.textDelta);
    }

    public void setText(String tireTexts) {
        delta.setText(tireTexts);
    }
}
