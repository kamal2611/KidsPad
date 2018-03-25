package drawingpad.doodle.vandu.kidspad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by vandu on 21-06-2015.
 */
public class BrushWidthView extends View {

    private Paint drawPaint;
    int canvasBackColor = Color.WHITE;
    int lineColor = Color.BLACK;
    int lineWidth;
    int lineMargin;

    public BrushWidthView(Context context) {
        this(context, null);
    }

    public BrushWidthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrushWidthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        lineWidth = (int)context.getResources().getDimension(R.dimen.stoke_width);
        lineMargin = (int)context.getResources().getDimension(R.dimen.margin_width);

        this.drawPaint = new Paint();
        this.drawPaint.setColor(this.lineColor);
        this.drawPaint.setAntiAlias(true);
        this.drawPaint.setStrokeWidth(this.lineWidth);
        this.drawPaint.setStyle(Paint.Style.STROKE);
        this.drawPaint.setStrokeJoin(Paint.Join.ROUND);
        this.drawPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    void setCanvasBackgroundColor(int color) {
        canvasBackColor = color;
    }

    void setLineWidth(int width) {
        lineWidth = width;
        this.drawPaint.setStrokeWidth(this.lineWidth);
    }

    void setLineColor(int color) {
        lineColor = color;
        this.drawPaint.setColor(this.lineColor);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(this.canvasBackColor);
        canvas.drawLine(lineMargin , (float)getHeight()/(float)2 ,
                getWidth() - lineMargin, (float)getHeight()/(float)2 , drawPaint);
    }
}
