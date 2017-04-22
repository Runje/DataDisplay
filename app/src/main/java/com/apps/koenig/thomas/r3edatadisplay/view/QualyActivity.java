package com.apps.koenig.thomas.r3edatadisplay.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.apps.koenig.thomas.r3edatadisplay.R;
import com.apps.koenig.thomas.r3edatadisplay.R3EApp;
import com.apps.koenig.thomas.r3edatadisplay.model.R3EData;
import com.apps.koenig.thomas.r3edatadisplay.model.R3EDisplayData;
import com.apps.koenig.thomas.r3edatadisplay.model.Sectors;

public class QualyActivity extends R3EActivity {

    private SectorView currSectors;
    private SectorView tbSectors;
    private SectorView pbSectors;
    private SectorView leaderSectors;
    private boolean startedOtherActivity;
    private QualyHeader qualy;
    private TextView[] positionsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = findViewById(R.id.activity_main);
        qualy = new QualyHeader(findViewById(R.id.header));
        textConnected = (TextView) findViewById(R.id.textConnected);
        currSectors = new SectorView(findViewById(R.id.sectorCurrent));
        leaderSectors = new SectorView(findViewById(R.id.sectorLeader));
        pbSectors = new SectorView(findViewById(R.id.sectorPB));
        tbSectors = new SectorView(findViewById(R.id.sectorTB));
        positionsView = new TextView[] {(TextView) findViewById(R.id.textPos1), (TextView) findViewById(R.id.textPos2), (TextView) findViewById(R.id.textPos3), (TextView) findViewById(R.id.textPosLap)};
        findViewById(R.id.textFuelToEnd).setVisibility(View.GONE);
        findViewById(R.id.describeFuelToEnd).setVisibility(View.GONE);
        tiresView = new TiresView(findViewById(R.id.tires));
        fuel = new FuelView(findViewById(R.id.fuel));
    }

    @Override
    protected String getTag() {
        return "QualyActivity";
    }

    @Override
    protected void updateGui(R3EData data) {
        super.updateGui(data);
        R3EDisplayData displayData = R3EApp.getR3EDisplayData();
        if (R3EData.QUALY != data.sessionType)
        {
            if (data.sessionType == R3EData.RACE && !startedOtherActivity)
            {
                // start Race Activity
                Log.i(tag, "Start Race Activity");
                startActivity(new Intent(this, RaceActivity.class));
                startedOtherActivity = true;
                displayData.reset();
            }
            else
            {
                //getLayoutInflater().inflate(R.layout.qualy_layout, content, true);
            }
        }

        // TODO: make Race/Qualy classes which handles the views
        qualy.setPos(data.position);
        qualy.setDiff(data.diffLapLeader);

        int fuelLaps = displayData.fuelLaps;
        qualy.setRemainingLaps(fuelLaps);

        qualy.setLap(data.selfBestLap);
        leaderSectors.setSectors(data.sectorTimesBestLapLeader, data.sectorTimesSelfCurrLap, Color.MAGENTA);
        tbSectors.setSectors(displayData.sectorTimesSelfTheoreticalLap, data.sectorTimesSelfCurrLap, GUIUtils.darkgreen);
        pbSectors.setSectors(data.sectorTimesSelfBestLap, data.sectorTimesSelfCurrLap, Color.GREEN);
        currSectors.setSectors(data.sectorTimesSelfCurrLap, new Sectors[]{data.sectorTimesBestLapLeader, displayData.sectorTimesSelfTheoreticalLap, data.sectorTimesSelfBestLap }, new int[]{ Color.MAGENTA, GUIUtils.darkgreen, Color.GREEN});

        for (int i = 0; i < 4; i++) {
            positionsView[i].setText(GUIUtils.posToString(displayData.currentPos[i]));
        }
    }

}
