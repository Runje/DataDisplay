package com.apps.koenig.thomas.r3edatadisplay;

import android.util.Log;

import com.apps.koenig.thomas.r3edatadisplay.communication.R3EMessage;

/**
 * Created by Thomas on 02.03.2017.
 */

public class R3EDisplayData {
    public static final float INVALID_DISTANCE = -1;
    public static final float INVALID_FACTOR = Float.MAX_VALUE;
    public static final float INVALID_TIREWEAR = -1;

    public static final float INVALID_FUEL = -1;
    public static final int FUEL_AVERAGE_N = 3;
    private static final float START_LAP_METER = 50;
    public static final int INVALID_LAPS = -1;
    private final int DISTANCE_THRESHOLD = 20;
    public Sectors sectorTimesSelfTheoreticalLap;
    public float usedFuelLastLap = INVALID_FUEL;
    private float fuelLeftBegin = INVALID_FUEL;
    private float lastLapDistance = INVALID_DISTANCE;
    private float[] usedFuelPerLap = new float[FUEL_AVERAGE_N];
    public float averageFuelPerLap = INVALID_FUEL;
    private int indexFuelPerLap = 0;
    public int fuelLaps = INVALID_LAPS;
    public int estimatedLapsToGo = INVALID_LAPS;
    private String LogKey = "R3EDisplayData";
    public float estimatedRefill = INVALID_FUEL;
    private float lastLeaderLapDistance = INVALID_DISTANCE;
    private int lastSessionType = -1;
    private boolean quitStint;
    private boolean lastQuitStint;
    private boolean leaderHasFinished;
    private boolean playerHasFinished;
    public int[] currentPos = new int[4];
    public float[] meterOnTires = new float[] { INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE };
    public float[] lastMeterOnTires = new float[] { INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE };
    public float[] lastTireWear = new float[] {INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR};
    public float[] secondLastTireWear = new float[] {INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR};
    private float[] secondLastMeterOnTires  = new float[] { INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE };
    private static final float MeasureTireEveryMeter = 5000;
    private float[] tireWearFactorA = new float[] { INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR};
    private float[] tireWearFactorB  = new float[] { INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR};
    private float layoutLength;

    public R3EDisplayData() {
        sectorTimesSelfTheoreticalLap = new Sectors(-1f, -1f, -1f);
    }

    public void reset() {
        Log.i(LogKey, "Resetting");
        sectorTimesSelfTheoreticalLap = new Sectors();
        usedFuelLastLap = INVALID_FUEL;
        fuelLeftBegin = INVALID_FUEL;
        lastLapDistance = INVALID_DISTANCE;
        lastLeaderLapDistance = INVALID_DISTANCE;
        usedFuelPerLap = new float[FUEL_AVERAGE_N];
        for (int i = 0; i < FUEL_AVERAGE_N; i++) {
            usedFuelPerLap[i] = INVALID_FUEL;
        }
        averageFuelPerLap = INVALID_FUEL;
        fuelLaps = INVALID_LAPS;
        estimatedRefill = INVALID_FUEL;
        leaderHasFinished = false;
        playerHasFinished = false;
        meterOnTires = new float[] { INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE };
        lastMeterOnTires = new float[] { INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE };
        secondLastMeterOnTires = new float[] { INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE };
        lastTireWear = new float[] {INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR};
        secondLastTireWear = new float[] {INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR};
        tireWearFactorB  = new float[] { INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR};
        tireWearFactorA  = new float[] { INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR};
    }

    public void update(R3EMessage message)
    {
        if (startOfNewSession(message)) {
            Log.i(LogKey, "New Session");
            reset();
        }
        layoutLength = message.layoutLength;

        quitStint = message.inMenu && !message.paused;

        if (quitStint) {
            fuelLeftBegin = INVALID_FUEL;
            fuelLaps = INVALID_LAPS;
            lastSessionType = message.sessionType;

        }
        //Log.i(LogKey,"Update: distance: " + message.lapDistance);

        if (message.controlType == R3EData.HUMAN) {
            updateTB(message);
        }
        float diff = averageFuelPerLap != INVALID_FUEL ? message.lapDistanceFraction * averageFuelPerLap : 0;
        float fuelLeft = message.fuelLeft - diff;
        if (newStint()) {
            Log.i(LogKey, "New Stint");
            fuelLeftBegin = INVALID_FUEL;
            updateFuelLaps(fuelLeft);
            meterOnTires = new float[] { INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE };
            lastMeterOnTires = new float[] { INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE };
            secondLastMeterOnTires = new float[] { INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE, INVALID_DISTANCE };
            lastTireWear = new float[] {INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR};
            secondLastTireWear = new float[] {INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR};
            tireWearFactorA  = new float[] { INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR};
            tireWearFactorB  = new float[] { INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR, INVALID_FACTOR};
        }


        if (beginOfNewLap(message) && !playerHasFinished)
        {
            Log.i(LogKey, "New Lap");

            // calculate fuel use if average is there or if it is a good measurement
            if (averageFuelPerLap != INVALID_LAPS || message.lapDistance < START_LAP_METER) {

                if (fuelLeftBegin != INVALID_FUEL) {
                    usedFuelLastLap = fuelLeftBegin - fuelLeft;
                    usedFuelPerLap[indexFuelPerLap] = usedFuelLastLap;
                    indexFuelPerLap = (indexFuelPerLap + 1) % FUEL_AVERAGE_N;
                    updateAverageFuel();
                    updateFuelLaps(fuelLeft);


                    Log.i(LogKey, "begin: " + fuelLeftBegin + ", average: " + averageFuelPerLap + ", lastLap: " + usedFuelLastLap + ", fuelLaps: " + fuelLaps);
                }

                Log.i(LogKey, "New Lap, fuel left begin: " + fuelLeft);
                fuelLeftBegin = fuelLeft;
                // add all meter of new lap to tires meters
                addMeterToTires(message.lapDistance);

            }
            else {
                Log.i(LogKey, "Invalid start of lap: " + message.lapDistance);
                // invalid start of lap
                fuelLeftBegin = INVALID_FUEL;
                updateFuelLaps(fuelLeft);
            }

            if (message.sessionTimeRemaining == 0 && !playerHasFinished) {
                playerHasFinished = true;
                Log.i(LogKey, "Player has finished");
            }




        } else if (lastLapDistance != INVALID_DISTANCE){
            // add difference meter to tires if it is not a new lap
            addMeterToTires(message.lapDistance - lastLapDistance);
        }


        // Race specific updates
        if (isRace(message) && !leaderHasFinished) {
            if (leaderBeginsNewLap(message)) {
                Log.i(LogKey, "New Lap from Leader");
                if (message.sessionTimeRemaining != 0) {
                    Log.i(LogKey, "Remaining time: " + message.sessionTimeRemaining);
                    updateEstimatedLaps(message);
                } else {
                    Log.i(LogKey, "Leader has finished");
                    leaderHasFinished = true;
                }
            }

            updateEstimatedFuelToEnd(message);
        }

        if (isQualy(message)) {
            updateCurrentPos(message);
        }

        updateTires(message);
        lastLapDistance = message.lapDistance;
        lastQuitStint = quitStint;
        if (message.numCars > 0)
        {
            lastLeaderLapDistance = message.drivers[0].lapDistance;
        }

        lastSessionType = message.sessionType;

    }

    private void updateTires(R3EMessage message) {
        for (int i = 0; i < 4; i++) {
            float meterOnTire = meterOnTires[i];
            float secondLastMeterOnTire = secondLastMeterOnTires[i];
            float lastMeterOnTire = lastMeterOnTires[i];
            float tireWear = getTireWear(message, i);
            if (tireWear == INVALID_TIREWEAR) {
                secondLastMeterOnTires[i] = INVALID_DISTANCE;
                continue;
            }

            if (secondLastMeterOnTire == INVALID_DISTANCE) {
                // initialize
                secondLastMeterOnTires[i] = meterOnTire;
                secondLastTireWear[i] = tireWear;
            } else if (meterOnTire - secondLastMeterOnTire > 2 * MeasureTireEveryMeter) {
                // rotate to next
                secondLastMeterOnTires[i] = lastMeterOnTires[i];
                secondLastTireWear[i] = lastTireWear[i];
                lastMeterOnTires[i] = meterOnTire;
                lastTireWear[i] = tireWear;
            } else if (meterOnTire - lastMeterOnTire > MeasureTireEveryMeter && lastMeterOnTire == INVALID_DISTANCE) {
                // init
                lastMeterOnTires[i] = meterOnTire;
                lastTireWear[i] = tireWear;
            }


            tireWearFactorA[i] = (tireWear - secondLastTireWear[i]) / (meterOnTire - secondLastMeterOnTires[i]);
            tireWearFactorB[i] = tireWear;
        }
    }

    private float getTireWear(R3EMessage message, int index) {
        switch (index) {
            case 0:
                return message.frontLeftWear;
            case 1:
                return message.frontRightWear;
            case 2:
                return message.rearLeftWear;
            case 3:
                return message.rearRightWear;

        }

        return 0;
    }

    public float[] calcTireDurationInLapsUntil(float tireWear) {
        float[] result = new float[] { INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR, INVALID_TIREWEAR};
        for (int i = 0; i < result.length; i++) {
            if (secondLastMeterOnTires[i] != INVALID_DISTANCE) {
                float y = tireWear;
                float b = tireWearFactorB[i];
                float a = tireWearFactorA[i];
                // y = ax + b in meter
                result[i] = (y - b) / a;

                // to laps
                result[i] = result[i] / layoutLength;
            }
        }
        return result;
    }

    private void addMeterToTires(float meter) {

        if (meter < 0) {
            return;
        }

        for (int i = 0; i < meterOnTires.length; i++) {
            meterOnTires[i] += meter;
        }
    }

    private void updateCurrentPos(R3EMessage message) {
        float[] currentSec = message.sectorTimesSelfCurrLap.array();
        for (int i = 0; i < 4; i++) {

            float current = currentSec[i];
            int pos = 1;
            if (current == R3EData.INVALID_SECTOR) {
                pos = -1;
            }
            else {

                for (int j = 0; j < message.numCars; j++) {
                    R3EDriver driver = message.drivers[j];
                    float driverCur = driver.sectorTimesCurrent.array()[i];
                    float driverBest = driver.sectorTimesBest.array()[i];
                    if (driverCur != R3EData.INVALID_SECTOR && driverCur < current || driverBest != R3EData.INVALID_SECTOR && driverBest < current) {
                        pos++;
                    }


                }
            }

            currentPos[i] = pos;
        }

        // show on lap pos estimated position, if 3. sector is not over yet
        if (currentSec[2] == R3EData.INVALID_SECTOR) {
            if (currentSec[1] == R3EData.INVALID_SECTOR) {
                // use pos from first time
                currentPos[3] = currentPos[0];
            } else {
                int pos = 1;
                float current = message.sectorTimesSelfCurrLap.absSec2();
                for (int j = 0; j < message.numCars; j++) {
                    R3EDriver driver = message.drivers[j];
                    float driverCur = driver.sectorTimesCurrent.absSec2();
                    float driverBest = driver.sectorTimesBest.absSec2();
                    if (driverCur != R3EData.INVALID_SECTOR && driverCur < current || driverBest != R3EData.INVALID_SECTOR && driverBest < current) {
                        pos++;
                    }
                }

                currentPos[3] = pos;
            }
        }

    }

    private boolean isQualy(R3EMessage message) {
        return message.sessionType == R3EData.QUALY;
    }

    private boolean newStint() {
        return lastQuitStint && !quitStint;
    }

    private boolean isRace(R3EMessage message) {
        return message.sessionType == R3EData.RACE;
    }

    private void updateTB(R3EMessage message) {
        float[] tb = sectorTimesSelfTheoreticalLap.array();
        float[] cur = message.sectorTimesSelfCurrLap.array();
        for (int i = 0; i < 3; i++) {
            if (cur[i] > 0 && (tb[i] == R3EData.INVALID_SECTOR || tb[i] > cur[i]))
            {
                tb[i] = cur[i];
            }
        }

        sectorTimesSelfTheoreticalLap.set(tb);
    }

    private boolean startOfNewSession(R3EMessage message) {

        return message.sessionType != -1 && lastSessionType != message.sessionType;
    }

    private void updateFuelLaps(float fuelLeft) {
        if (averageFuelPerLap > 0) {
            fuelLaps = (int) ((fuelLeft - 1) / averageFuelPerLap);
        } else {
            fuelLaps = INVALID_LAPS;
        }
    }


    private void updateEstimatedFuelToEnd(R3EMessage message) {
        if (averageFuelPerLap <= 0)
        {
            estimatedRefill = INVALID_FUEL;
        }
        else {
            float restLap = (1 - message.lapDistanceFraction) * averageFuelPerLap;
            estimatedRefill = restLap + (estimatedLapsToGo - message.completedLaps - 1) * averageFuelPerLap - message.fuelLeft;
        }
    }

    private void updateEstimatedLaps(R3EMessage message) {
        if (message.numCars > 0) {
            R3EDriver driver = message.drivers[0];
            float fastestLap = driver.sectorTimesBest.lap();
            int completedLaps = driver.completedLaps;
            if (fastestLap != R3EData.INVALID_SECTOR){
                estimatedLapsToGo = completedLaps + (int) (message.sessionTimeRemaining / fastestLap) + 1;
                Log.i(LogKey, "fastest Lap " + fastestLap);
                Log.i(LogKey, "estimated Laps " + estimatedLapsToGo);
                Log.i(LogKey, "remaining time " + message.sessionTimeRemaining);
            } else {
                estimatedLapsToGo = INVALID_LAPS;
            }
        }
    }

    private void updateAverageFuel() {
        int count = 0;
        float sum = 0;
        for (int i = 0; i < FUEL_AVERAGE_N; i++) {
            if (usedFuelPerLap[i] != INVALID_FUEL) {
                count++;
                sum += usedFuelPerLap[i];
            }
        }

        averageFuelPerLap = count != 0 ? sum / count : INVALID_FUEL;
    }

    private boolean beginOfNewLap(R3EMessage message) {
        if (message.lapDistance == INVALID_DISTANCE) {
            return false;
        }
        // second case is because sometimes the udp messages come in wrong order
        boolean lapDistanceRel = message.lapDistance < lastLapDistance;
        float abs = Math.abs(message.lapDistance - lastLapDistance);
        boolean newLap = lapDistanceRel && abs > DISTANCE_THRESHOLD;
        if (newLap) {
            Log.i(LogKey, "lapDistance: " + message.lapDistance + ", lastLapDistance: " + lastLapDistance + ", abs: " + abs);
        }
        return newLap;
    }

    private boolean leaderBeginsNewLap(R3EMessage message) {
        // second case is because sometimes the udp messages come in wrong order
        return  (message.drivers[0].lapDistance < lastLeaderLapDistance && Math.abs(message.drivers[0].lapDistance - lastLeaderLapDistance) > DISTANCE_THRESHOLD);
    }
}
