package com.example.prodigy.travelate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by prodigy on 16/2/18.
 */

public class FocusArea extends View {
    private float X, Y;
    private Paint mPaint;

    public FocusArea(Context context,float X, float Y) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(getResources().getColor(android.R.color.white));
        canvas.drawCircle(X,Y,5,mPaint);

    }
}
