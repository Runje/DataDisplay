package com.apps.koenig.thomas.r3edatadisplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.apps.koenig.thomas.r3edatadisplay.R;
import com.apps.koenig.thomas.r3edatadisplay.R3EApp;
import com.apps.koenig.thomas.r3edatadisplay.model.R3EData;
import com.apps.koenig.thomas.r3edatadisplay.model.R3EDisplayData;
import com.apps.koenig.thomas.r3edatadisplay.communication.R3EConnectionListener;

public class RaceActivity extends R3EActivity implements R3EConnectionListener {

    private RaceHeader raceHeader;
    private boolean startedOtherActivity;
    private SectorView currentSectors;
    private SectorView aheadSectors;
    private SectorView behindSectors;
    private SectorView leaderSectors;
    private SectorView pbSectors;
    private TiresView tiresForecastView;
    private BoxenstopView boxenstopView;
    private TiresView tiresForecastView2;
    private TiresView tiresForecastView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(tag, "OnCreate");
        setContentView(R.layout.race_layout);
        mainView = findViewById(R.id.race_layout);
        raceHeader = new RaceHeader(findViewById(R.id.header));
        textConnected = (TextView) findViewById(R.id.textConnected);
        currentSectors = new SectorView(findViewById(R.id.sectorCurrent));
        aheadSectors = new SectorView(findViewById(R.id.sectorAhead));
        behindSectors = new SectorView(findViewById(R.id.sectorBehind));
        pbSectors = new SectorView(findViewById(R.id.sectorPB));
        leaderSectors = new SectorView(findViewById(R.id.sectorFastest));
        tiresView = new TiresView(findViewById(R.id.tires));
        fuel = new FuelView(findViewById(R.id.fuel));
        tiresForecastView = new TiresView(findViewById(R.id.tires_forecast));
        tiresForecastView.setValue("70%");
        tiresForecastView2 = new TiresView(findViewById(R.id.tires_forecast2));
        tiresForecastView2.setValue("45%");
        tiresForecastView3 = new TiresView(findViewById(R.id.tires_forecast3));
        tiresForecastView3.setValue("25%");
        boxenstopView = new BoxenstopView(findViewById(R.id.boxenstop));


    }

    @Override
    protected String getTag() {
        return "RaceActivity";
    }

    @Override
    protected void updateGui(R3EData data) {
        super.updateGui(data);
        if (R3EData.RACE != data.sessionType)
        {
            if (data.sessionType == R3EData.QUALY && !startedOtherActivity)
            {
                // start Qualy Activity
                Log.i("Race", "Starting Qualy");
                startActivity(new Intent(this, QualyActivity.class));
                startedOtherActivity = true;
                R3EApp.getR3EDisplayData().reset();
            }
            else
            {
                //getLayoutInflater().inflate(R.layout.qualy_layout, content, true);
            }
        }

        R3EDisplayData r3EDisplayData = R3EApp.getR3EDisplayData();

        raceHeader.setPos(data.position);
        raceHeader.setLaps(data.completedLaps, r3EDisplayData.estimatedLapsToGo);
        raceHeader.setAhead(data.diffAhead);
        raceHeader.setBehind(data.diffBehind);
        raceHeader.setFuelLaps(r3EDisplayData.fuelLaps);

        currentSectors.setTextAndColor(GUIUtils.sectorToText(data.sectorTimesSelfCurrLap));
        currentSectors.setColors(GUIUtils.getColorsCurrentRace(data.sectorTimesBestLapLeader, data.sectorTimesSelfBestLap, data.sectorTimesSelfCurrLap, r3EDisplayData.sectorTimesSelfTheoreticalLap));
        float[] diffs1 = GUIUtils.sectorToDiffs(data.sectorTimesAhead, data.sectorTimesSelfCurrLap);
        aheadSectors.setTextAndColor(GUIUtils.diffsToString(diffs1), GUIUtils.diffsToColor(diffs1));
        float[] diffs2 = GUIUtils.sectorToDiffs(data.sectorTimesBehind, data.sectorTimesSelfCurrLap);
        behindSectors.setTextAndColor(GUIUtils.diffsToString(diffs2), GUIUtils.diffsToColor(diffs2));
        pbSectors.setTextAndColor(GUIUtils.sectorToText(data.sectorTimesSelfBestLap));
        leaderSectors.setTextAndColor(GUIUtils.sectorToText(data.sectorTimesBestLapLeader));

        tiresForecastView.setText(GUIUtils.forecastsToTexts(r3EDisplayData.calcTireDurationInLapsUntil(0.7f)));
        tiresForecastView2.setText(GUIUtils.forecastsToTexts(r3EDisplayData.calcTireDurationInLapsUntil(0.45f)));
        tiresForecastView3.setText(GUIUtils.forecastsToTexts(r3EDisplayData.calcTireDurationInLapsUntil(0.25f)));
    }

}
