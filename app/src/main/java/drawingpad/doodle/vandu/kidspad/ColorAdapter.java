package drawingpad.doodle.vandu.kidspad;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * Created by vandu on 10-05-2015.
 */
public class ColorAdapter
        extends BaseAdapter
{
    private Context mContext;

    public ColorAdapter(Context c)
    {
        this.mContext = c;
    }

    public int getCount()
    {
        return this.mThumbIds.length;
    }

    public Object getItem(int position)
    {
        return null;
    }

    public long getItemId(int position)
    {
        return 0L;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ColorThumbView imageView;
        if (convertView == null)
        {
            imageView = new ColorThumbView(this.mContext);
            int val = (int)this.mContext.getResources().getDimension(R.dimen.color_box_size);
            imageView.setLayoutParams(new GridView.LayoutParams(val, val));
            imageView.setPadding(5, 5, 5, 5);
        }
        else
        {
            imageView = (ColorThumbView)convertView;
        }
        imageView.setRectColor(this.mThumbIds[position].intValue());
        return imageView;
    }

    int getColor(int pos)
    {
        return this.mThumbIds[pos].intValue();
    }

    private Integer[] mThumbIds = {
            Color.BLACK, Color.RED, Color.GREEN, Integer.valueOf(Color.rgb(180, 103, 77)),
            Integer.valueOf(Color.rgb(255, 170, 204)),
            Integer.valueOf(Color.rgb(0, 100, 0)), Color.YELLOW, Integer.valueOf(Color.rgb(255, 117, 56)),
            Color.BLUE, Integer.valueOf(Color.rgb(116, 66, 200)),
            Color.MAGENTA, Color.CYAN };
}

