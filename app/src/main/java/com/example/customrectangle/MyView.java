package com.example.customrectangle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

    Paint mPaint, mBPaint;
    Path mPath;
    Canvas mCanvas;
    Bitmap mBitmap;
    private float mX, mY;
    private int mBackgroundColor = Color.WHITE;
    private int mDrawColor = Color.RED;
    private static final float TOUCH_TOLERANCE = 4;
    boolean topChanging = false, leftChanging = false, rightChanging = false, bottomChanging = false;
    rect rec;
    float lastX , lastY ;


    public MyView(Context context) {
        super(context);
        mPath = new Path();
        // Set up the paint with which to draw.
        mPaint = new Paint();
        mPaint.setColor(mDrawColor);
        // Smoothes out edges of what is drawn without affecting shape.

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE); // default: FILL

        mBPaint = new Paint();
        mBPaint.setColor(mBackgroundColor);
        // Smoothes out edges of what is drawn without affecting shape.

        // Dithering affects how colors with higher-precision device
        // than the are down-sampled.

        mBPaint.setStyle(Paint.Style.FILL_AND_STROKE); // default: FILL

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(mBackgroundColor);
        mCanvas.drawRect(getWidth()/2-200, getHeight()/2-200, getWidth()/2+200, getHeight()/2+200, mPaint);
        rec = new rect(400, 400);
        lastY = getHeight()/2;
        lastX = getWidth()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!isTopTouched(x, y)&&!isBottomTouched(x, y)&&!isLeftTouched(x, y)&&!isRightTouched(x, y))
                    touchStart(x, y);
                else{
                    if(isTopTouched(x, y))
                        topChanging = true;
                    if(isBottomTouched(x, y))
                        bottomChanging = true;
                    if(isLeftTouched(x, y))
                        leftChanging = true;
                    if(isRightTouched(x, y))
                        rightChanging = true;
                    mX = x;
                    mY = y;
                }

                break;
            case MotionEvent.ACTION_MOVE:

                if (!topChanging&&!bottomChanging&&!leftChanging&&!rightChanging)
                touchMove(x, y);
                else{
                    if(topChanging)
                        extendTop(x, y);
                    if(bottomChanging)
                        extendBottom(x, y);
                    if(leftChanging)
                        extendLeft(x, y);
                    if(rightChanging)
                        extendRight(x, y);

                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (!topChanging&&!bottomChanging&&!leftChanging&&!rightChanging)
                touchUp(x,y);
                else{
                    if(topChanging)
                        extendTop(x, y);
                    if(bottomChanging)
                        extendBottom(x, y);
                    if(leftChanging)
                        extendLeft(x, y);
                    if(rightChanging)
                        extendRight(x, y);

                }
                topChanging = bottomChanging = leftChanging = rightChanging = false;
                break;
            default:


        }
        return true;


    }
    private void touchStart(float x, float y) {
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).

            // Reset mX and mY to the last drawn point.
            mX = x;
            mY = y;
            // Save the path in the extra bitmap,
            // which we access through its canvas.

            mCanvas.drawColor(mBackgroundColor);
            mCanvas.drawRect(x-rec.getL()/2, y-rec.getB()/2, x+rec.getL()/2, y+rec.getB()/2, mPaint);
        }
    }
    private void touchUp(float x, float y) {
// Reset the path so it doesn't get drawn again.
        lastX = x;
        lastY = y;

    }
    private boolean isTopTouched(float x,float y){
        if (y>=((lastY-rec.getB()/2.0-40))&&y<=((lastY-rec.getB()/2.0)+40))
            return (x >= lastX - rec.getL() / 2.0) && (x <= lastX + rec.getL() / 2.0);
        return false;
    }
    private boolean isBottomTouched(float x,float y){
        if (y<=((lastY+rec.getB()/2.0+40))&&y>=((lastY+rec.getB()/2.0)-40))
            return (x >= lastX - rec.getL() / 2.0) && (x <= lastX + rec.getL() / 2.0);
        return false;
    }
    private boolean isLeftTouched(float x,float y){
        if ((x>=(lastX-rec.getL()/2.0-40))&&x<=((lastX-rec.getL()/2.0)+40))
            return (y >= lastY - rec.getB() / 2.0) && (y <= lastY + rec.getB() / 2.0);
        return false;
    }
    private boolean isRightTouched(float x,float y){
        if ((x<=(lastX+rec.getL()/2.0+40))&&x>=((lastX+rec.getL()/2.0)-40))
            return (y >= lastY - rec.getB() / 2.0) && (y <= lastY + rec.getB() / 2.0);
        return false;
    }

    private void extendTop(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).

            // Reset mX and mY to the last drawn point.

            // Save the path in the extra bitmap,
            // which we access through its canvas.

            mCanvas.drawColor(mBackgroundColor);
            mCanvas.drawRect(lastX-rec.getL()/2, y, lastX+rec.getL()/2, lastY+rec.getB()/2, mPaint);
            rec.setB(lastY+rec.getB()/2-y);
            lastY = y+rec.getB()/2;

            mX = x;
            mY = y;
        }

    }
    private void extendBottom(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).

            // Reset mX and mY to the last drawn point.

            // Save the path in the extra bitmap,
            // which we access through its canvas.

            mCanvas.drawColor(mBackgroundColor);
            mCanvas.drawRect(lastX-rec.getL()/2, lastY-rec.getB()/2, lastX+rec.getL()/2, y, mPaint);
            rec.setB(y-lastY+rec.getB()/2);
            lastY = y-rec.getB()/2;

            mX = x;
            mY = y;
        }

    }
    private void extendLeft(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).

            // Reset mX and mY to the last drawn point.

            // Save the path in the extra bitmap,
            // which we access through its canvas.

            mCanvas.drawColor(mBackgroundColor);
            mCanvas.drawRect(x, lastY-rec.getB()/2, lastX+rec.getL()/2, lastY+rec.getB()/2, mPaint);
            rec.setL(lastX+rec.getL()/2-x);
            lastX = x+rec.getL()/2;

            mX = x;
            mY = y;
        }

    }
    private void extendRight(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).

            // Reset mX and mY to the last drawn point.

            // Save the path in the extra bitmap,
            // which we access through its canvas.

            mCanvas.drawColor(mBackgroundColor);
            mCanvas.drawRect(lastX-rec.getL()/2, lastY-rec.getB()/2, x, lastY+rec.getB()/2, mPaint);
            rec.setL(x-lastX+rec.getL()/2);
            lastX = x-rec.getL()/2;

            mX = x;
            mY = y;
        }

    }

}
