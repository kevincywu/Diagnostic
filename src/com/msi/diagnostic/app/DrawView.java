/**
 * DrawView.java
 *
 * Written by : Qiuchen Jiang
 * Written for: N0J1 MES
 * Date : Dec 21, 2011
 * Version : 1.0
 */

package com.msi.diagnostic.app;

import java.util.ArrayList;

import com.msi.diagnostic.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * This class helps drawing, setting canvas' and painters' properties.
 *
 * @Author Qiuchen Jiang, qiuchenjiang@msi.com
 * @Version 1.0, 12/21/2011
 */
public class DrawView extends View
{
    private ArrayList<Integer> mXPos;
    private ArrayList<Integer> mYPos;
    private int mDotColor;
    private int mTextColor;
    private int mDotRadius;
    private int mWidth;
    private int mHeight;
    private String mNote;
    private static String NOTE_STRING;
    private static String RETRY_STRING;
    private static String END_STRING;

    public DrawView(Context context) {
        super(context);
        this.mXPos = new ArrayList<Integer>();
        this.mYPos = new ArrayList<Integer>();
        this.mTextColor = Color.BLUE;
        this.mDotColor = Color.BLUE;
        this.mDotRadius = 16;
        NOTE_STRING = context.getString(R.string.screen_around_retest);
        RETRY_STRING = context.getString(R.string.screen_around_retry);
        END_STRING = context.getString(R.string.screen_around_end);
    }

    public void setWindowScale(int w, int h) {
        this.mWidth = w;
        this.mHeight = h;
    }

    public void setPosition(ArrayList<Integer> x, ArrayList<Integer> y) {
        this.mXPos = x;
        this.mYPos = y;
    }

    public void setColor(int dColor, int tColor) {
        this.mDotColor = dColor;
        this.mTextColor = tColor;
    }

    public void setDotRadius(int radius) {
        this.mDotRadius = radius;
    }

    public void setNote(String str) {
        this.mNote = str;
    }

    public int getWindowScaleWidth() {
        return mWidth;
    }

    public int getWindowScaleHeight() {
        return mHeight;
    }

    public ArrayList<Integer> getXPosition() {
        return mXPos;
    }

    public ArrayList<Integer> getYPosition() {
        return mYPos;
    }

    public int getDotColor() {
        return mDotColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getDotRadius() {
        return mDotRadius;
    }

    public String getNote() {
        return mNote;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int TEXT_SIZE = 30;
        final float RETRY_TEXT_CENTER_OFFSET_X = -200;
        final float RETRY_TEXT_CENTER_OFFSET_Y = 100;
        final float END_TEXT_CENTER_OFFSET_X = 50;
        final float END_TEXT_CENTER_OFFSET_Y = 100;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mTextColor);
        paint.setTextSize(TEXT_SIZE);
        canvas.drawColor(Color.WHITE);

        float centerPosX = mWidth / 4;
        float centerPosY = mHeight / 4;
        float halfNoteWidth = paint.measureText(mNote) / 2;

        if (mNote.length() == (NOTE_STRING).length()) {
            canvas.drawText(mNote, centerPosX - halfNoteWidth, centerPosY, paint);
            canvas.drawText(RETRY_STRING, centerPosX + RETRY_TEXT_CENTER_OFFSET_X, centerPosY
                                                     + RETRY_TEXT_CENTER_OFFSET_Y, paint);
            canvas.drawText(END_STRING, centerPosX + END_TEXT_CENTER_OFFSET_X, centerPosY
                                                   + END_TEXT_CENTER_OFFSET_Y, paint);
        } else {
            canvas.drawText(mNote, centerPosX - halfNoteWidth, centerPosY, paint);
        }
        paint.setColor(mDotColor);
        int dotAmount = mXPos.size();
        for (int i = 0; i < dotAmount; ++i) {
            canvas.drawCircle(mXPos.get(i), mYPos.get(i), mDotRadius, paint);
        }
    }
}
