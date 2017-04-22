package com.apps.koenig.thomas.r3edatadisplay.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.apps.koenig.thomas.r3edatadisplay.R3EApp;
import com.apps.koenig.thomas.r3edatadisplay.model.R3EData;
import com.apps.koenig.thomas.r3edatadisplay.model.R3EDisplayData;
import com.apps.koenig.thomas.r3edatadisplay.communication.R3EConnectionListener;
import com.apps.koenig.thomas.r3edatadisplay.communication.R3EMessage;

public abstract class R3EActivity extends AppCompatActivity implements R3EConnectionListener {

    protected String tag = "BaseActivity";
    protected TextView textConnected;
    protected View mainView;
    protected TiresView tiresView;
    protected FuelView fuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getTag();
        Log.i(tag, "OnCreate");
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected abstract String getTag();

    @Override
    public void receiveMessage(final R3EMessage msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateGui(new R3EData(msg));
            }
        });
    }

    @Override
    public void onConnectionChange( final boolean connected) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textConnected != null) {
                    textConnected.setText(connected ? "CONNECTED" : "NOT CONNECTED");
                    textConnected.setTextColor(connected ? Color.GREEN : Color.RED);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(tag, "OnResume");
        R3EApp.addListener(this);
        if (mainView != null) {
            mainView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(tag, "OnStop");
        R3EApp.removeListener(this);
    }

    protected void updateGui(R3EData data) {
        tiresView.setText(GUIUtils.dataToTireWear(data.frontLeftWear, data.frontRightWear, data.rearLeftWear, data.rearRightWear));
        tiresView.setTextColor(GUIUtils.dataToColors(data.frontLeftWear, data.frontRightWear, data.rearLeftWear, data.rearRightWear));
        R3EDisplayData r3EDisplayData = R3EApp.getR3EDisplayData();
        float usedFuelLastLap = r3EDisplayData.usedFuelLastLap;
        float averageFuel = r3EDisplayData.averageFuelPerLap;
        fuel.setAverageLap(averageFuel);
        fuel.setLastLap(usedFuelLastLap);
        fuel.setFuelToEnd(r3EDisplayData.estimatedRefill);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mainView != null) {
            mainView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }
}
