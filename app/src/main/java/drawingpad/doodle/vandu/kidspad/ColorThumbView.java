package drawingpad.doodle.vandu.kidspad;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * Created by vandu on 10-05-2015.
 */

public class ColorThumbView
        extends View
{
    int rectColor = Color.WHITE;

    public ColorThumbView(Context context)
    {
        super(context);
    }

    void setRectColor(int color)
    {
        this.rectColor = color;
    }

    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        canvas.drawColor(this.rectColor);
    }
}

