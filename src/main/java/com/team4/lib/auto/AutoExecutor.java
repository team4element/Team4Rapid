package com.team4.lib.auto;

import com.team254.lib.util.CrashTrackingRunnable;

public class AutoExecutor {
    private Thread mRunner = null;
    private AutoBase mAutoMode = null;

    public void setAutoMode(AutoBase mode)
    {
        mAutoMode = mode;
        mRunner = new Thread(new CrashTrackingRunnable() {
            @Override
            public void runCrashTracked() {
                if (mAutoMode != null)
                {
                    mAutoMode.routine();
                }
            }
        });
    }

    public AutoBase getAutoMode()
    {
        return mAutoMode;
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
