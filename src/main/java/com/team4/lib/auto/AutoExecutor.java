package com.team4.lib.auto;

import com.team254.lib.util.CrashTrackingRunnable;

public class AutoExecutor {
    private Thread mRunner = null;


    public void setAutoMode(AutoBase mode)
    {
        mRunner = new Thread(new CrashTrackingRunnable() {
            @Override
            public void runCrashTracked() {
                if (mode != null)
                {
                    mode.routine();
                }
            }
        });
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
