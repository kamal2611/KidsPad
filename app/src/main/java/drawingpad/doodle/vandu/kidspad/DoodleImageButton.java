package drawingpad.doodle.vandu.kidspad;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by vandu on 02-08-2015.
 */
public class DoodleImageButton extends ImageButton{

    Context mContext;

    public DoodleImageButton(Context context) {
        super(context);
        mContext = context;
    }

    public DoodleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public DoodleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getParent() instanceof ViewGroup) {
            setMeasuredDimension( ((ViewGroup)getParent()).getWidth()/Utility.itemCount ,
                    ((ViewGroup)getParent()).getWidth()/Utility.itemCount);
        }
        else {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
