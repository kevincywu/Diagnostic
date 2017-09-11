package com.msi.diagnostic.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {
    private boolean mFirstDraw;

    private float mDirection = 0;

    private float[] accelerometerValues, magneticFieldValues;

    private float ANGLE_SCREEN = 90;

    private float RADIAN = (float) (3.1415 / 180);

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CompassView(Context context) {
        super(context);
        init();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public boolean getFirstDraw() {
        return mFirstDraw;
    }

    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(30);
        mFirstDraw = true;
        accelerometerValues = new float[3];
        magneticFieldValues = new float[3];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cxCompass = getMeasuredWidth() / 2;
        int cyCompass = getMeasuredHeight() / 2;
        float radiusCompass = (float) (Math.min(cxCompass, cyCompass) * 0.9);

        canvas.drawCircle(cxCompass, cyCompass, radiusCompass, mPaint);
        if (!mFirstDraw) {
            canvas.drawLine(
                    cxCompass,
                    cyCompass,
                    (float) (cxCompass + radiusCompass * Math.sin((double) (-mDirection) * RADIAN)),
                    (float) (cyCompass - radiusCompass * Math.cos((double) (-mDirection) * RADIAN)),
                    mPaint);
        }
    }

    public void updateDirection(SensorEventInfo sensorEventInfo) {

        mFirstDraw = false;
        mDirection = handleCompassDirect(sensorEventInfo);
        invalidate();
    }

    private float handleCompassDirect(SensorEventInfo sensorEventInfo) {
        switch (sensorEventInfo.TYPE_SESOR) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticFieldValues = sensorEventInfo.SENSOR_EVENT_VALUE;
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = sensorEventInfo.SENSOR_EVENT_VALUE;
                break;
        }
        if (magneticFieldValues != null && accelerometerValues != null) {
            float[] values = new float[3];
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
            SensorManager.getOrientation(R, values);

            values[0] = (float) Math.toDegrees(values[0]) + ANGLE_SCREEN;
            return values[0];
        }
        return 0;
    }
}
