package com.team4.lib.auto;

import edu.wpi.first.wpilibj.DriverStation;

public class AutoExecutor {
    private Thread mRunner = null;


    public void setAutoMode(AutoBase mode)
    {
        mRunner = new Thread(new Runnable()
        {
            @Override
            public void run() {
                try{
                    mode.routine();
                } catch (Exception e) {
                    DriverStation.reportError("Auto Mode ended earlier than expected", false);
                    return;
                }

                mode.done();
            }
        } );
    }

    public void start()
    {
        mRunner.start();
    }

    public void stop()
    {
        mRunner = null;
    }
}
