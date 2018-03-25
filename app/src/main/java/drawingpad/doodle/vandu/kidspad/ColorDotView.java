package drawingpad.doodle.vandu.kidspad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by vandu on 04-07-2015.
 */
public class ColorDotView extends ColorThumbView {

    private float strokWidth;
    private int brushColor = Color.WHITE;
    private int backColor = Color.BLACK;
    Paint drawPaint;

    public ColorDotView(Context context)
    {
        super(context);
        strokWidth = (int)context.getResources().getDimension(R.dimen.erase_width);
        this.drawPaint = new Paint();
        this.drawPaint.setColor(this.brushColor);
        this.drawPaint.setAntiAlias(true);
        this.drawPaint.setStyle(Paint.Style.FILL);
    }

    void setBrushColor(int color)
    {
        this.brushColor = color;
        setBrush();
    }

    int getBrushColor()
    {
        return this.brushColor;
    }

    void setBrushWidth(int strokWidth)
    {
        this.strokWidth = strokWidth;
        setBrush();
    }

    void setBackColor(int bkColor)
    {
        this.backColor = bkColor;
        setRectColor(bkColor);
    }


    private void setBrush()
    {
        this.drawPaint.setColor(this.brushColor);
        //this.drawPaint.setStrokeWidth(this.strokWidth/2);
    }

    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        canvas.drawCircle(getWidth()/2 , getHeight()/2 , strokWidth/2, drawPaint);
    }

}
