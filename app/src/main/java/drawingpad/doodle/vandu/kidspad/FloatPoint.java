package drawingpad.doodle.vandu.kidspad;

import java.io.Serializable;

/**
 * Created by vandu on 13-06-2015.
 */
public class FloatPoint implements Serializable {
    float xVal;
    float yVal;

    FloatPoint(float xVal, float yVal)
    {
        this.xVal = xVal;
        this.yVal = yVal;
    }
}
