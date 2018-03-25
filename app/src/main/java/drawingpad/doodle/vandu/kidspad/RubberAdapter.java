package drawingpad.doodle.vandu.kidspad;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * Created by vandu on 04-07-2015.
 */
public class RubberAdapter extends BaseAdapter {
    private Context mContext;

    public RubberAdapter(Context c)
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
        ColorDotView imageView;
        if (convertView == null)
        {
            imageView = new ColorDotView(this.mContext);
            int val = (int)this.mContext.getResources().getDimension(R.dimen.rubber_box_size);
            imageView.setLayoutParams(new GridView.LayoutParams(val, val));
            imageView.setPadding(0, 0, 0, 0);
        }
        else
        {
            imageView = (ColorDotView)convertView;
        }
        imageView.setBackColor(Color.BLACK);
        imageView.setBrushColor(Color.WHITE);
        imageView.setBrushWidth(Utility.dpToPx(mContext ,
                this.mThumbIds[position].intValue()));

        return imageView;
    }

    int getRubberWidth(int pos)
    {
        return Utility.dpToPx(mContext ,
                this.mThumbIds[pos].intValue());
    }


    private Integer[] mThumbIds = {
            10 , 20 , 30 , 50 , 60};
}
