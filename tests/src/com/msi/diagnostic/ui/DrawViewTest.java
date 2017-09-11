package com.msi.diagnostic.ui;

import java.util.ArrayList;

import com.msi.diagnostic.app.DrawView;

import android.graphics.Color;
import android.test.AndroidTestCase;
import android.util.Log;

public class DrawViewTest extends AndroidTestCase {
    private static String TAG = "DrawViewTest";
    private DrawView mDrawView;

    @Override
    protected void setUp() throws Exception {
        Log.d(TAG, "setUp...");
        mDrawView = new DrawView(getContext());
    }

    public void test_setColor() {
        int dotColor = Color.BLACK;
        int textColor = Color.BLUE;
        mDrawView.setColor(dotColor, textColor);
        assertEquals(dotColor, mDrawView.getDotColor());
        assertEquals(textColor, mDrawView.getTextColor());
    }

    public void test_setDotRadius() {
        int radius = 5;
        mDrawView.setDotRadius(radius);
        assertEquals(radius, mDrawView.getDotRadius());
    }

    public void test_setNote() {
        String str = "test";
        mDrawView.setNote(str);
        assertEquals(str, mDrawView.getNote());
    }

    public void test_setPosition() {
        ArrayList<Integer> xPos = new ArrayList<Integer>();
        ArrayList<Integer> yPos = new ArrayList<Integer>();
        mDrawView.setPosition(xPos, yPos);
        assertEquals(xPos, mDrawView.getXPosition());
        assertEquals(yPos, mDrawView.getYPosition());
    }

    public void test_setWindowScale() {
        int width = 100;
        int height = 200;
        mDrawView.setWindowScale(width, height);
        assertEquals(width, mDrawView.getWindowScaleWidth());
        assertEquals(height, mDrawView.getWindowScaleHeight());
    }

    @Override
    protected void tearDown() throws Exception {
        Log.d(TAG, "tearDown...");
    }
}
