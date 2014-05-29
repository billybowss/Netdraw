package com.karamanis.netdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = 0xFF660000;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;

    public DrawingView(Context c) {
	    super(c);
	    drawPath = new Path();
	    drawPaint = new Paint();
	    drawPaint.setColor(paintColor);
	    drawPaint.setAntiAlias(true);
	    drawPaint.setStrokeWidth(20);
	    drawPaint.setStyle(Paint.Style.STROKE);
	    drawPaint.setStrokeJoin(Paint.Join.ROUND);
	    drawPaint.setStrokeCap(Paint.Cap.ROUND);
	    canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
     protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    	drawCanvas = new Canvas(canvasBitmap);

    }
    @Override
    protected void onDraw(Canvas canvas) {
    	canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
    	canvas.drawPath(drawPath, drawPaint);
    }


    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	float touchX = event.getX();
    	float touchY = event.getY();
    	switch (event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    	    drawPath.moveTo(touchX, touchY);
	    	    break;
	    	case MotionEvent.ACTION_MOVE:
	    	    drawPath.lineTo(touchX, touchY);
	    	    break;
	    	case MotionEvent.ACTION_UP:
	    	    drawCanvas.drawPath(drawPath, drawPaint);
	    	    drawPath.reset();
	    	    break;
	    	default:
	    	    return false;
    	}
    	invalidate();
    	return true;
    }

	public int getPaintColor() {
		return paintColor;
	}

	public void setPaintColor(int paintColor) {
		this.paintColor = paintColor;
		drawPaint.setColor(paintColor);
	}  
}

