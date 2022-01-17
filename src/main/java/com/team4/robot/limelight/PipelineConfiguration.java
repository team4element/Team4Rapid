package com.team4.robot.limelight;

public class PipelineConfiguration {
    public final double zoomFactor;
    public final double crosshairX;
    public final double crosshairY;

    private final double offsetX;
    private final double offsetY;

    public PipelineConfiguration(double zoomFactor) {
        this(zoomFactor, 0.5, 0.5);
    }

    public PipelineConfiguration(double zoomFactor, double crosshairX, double crosshairY) {
        this.zoomFactor = Math.round(zoomFactor);
        this.crosshairX = crosshairX;
        this.crosshairY = crosshairY;

        offsetX = crosshairX - 1.0 / (2.0 * zoomFactor);
        offsetY = crosshairY - 1.0 / (2.0 * zoomFactor);
    }

    public double[] getOffset()
    {
        double[] offset = {offsetX, offsetY};
        return offset;
    }
}
