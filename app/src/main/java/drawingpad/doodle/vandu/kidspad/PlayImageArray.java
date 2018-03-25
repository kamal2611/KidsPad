package drawingpad.doodle.vandu.kidspad;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vandu on 13-06-2015.
 */
public class PlayImageArray implements Serializable {
    ArrayList<ArrayList<FloatPoint>> playPathArray;
    ArrayList<ArrayList<Integer>> playPaintColorArray;
    ArrayList<ArrayList<Integer>> playPaintStrokeWidthArray;

    static final long serialVersionUID = -8235562215073233703L;


    PlayImageArray() {
        this.playPathArray = new ArrayList();
        this.playPaintColorArray = new ArrayList();
        this.playPaintStrokeWidthArray = new ArrayList();



    }

    /*void savePlayImage(String fileName) {
        try {
            File _sdCard = Environment.getExternalStorageDirectory();
            File _picDir = new File(_sdCard, Utility.dirName);
            _picDir.mkdirs();
            File _picFile = new File(_picDir, fileName + Utility.plyTyp);
            FileOutputStream _fos = new FileOutputStream(_picFile);
            ObjectOutputStream plObj = new ObjectOutputStream(_fos);
            plObj.writeObject(this);
            plObj.flush();
            plObj.close();
            _fos.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }*/

    void savePlayImage(String fileName,Context ctx, int doodleWidth, int doodleHeight) {
        savePlayImage(fileName,ctx,doodleWidth,doodleHeight, false);
    }
    void savePlayImage(String fileName,Context ctx, int doodleWidth,
                       int doodleHeight, boolean localFile) {
        try {
            /*File _sdCard = Environment.getExternalStorageDirectory();
            File _picDir = new File(_sdCard, Utility.dirName);
            _picDir.mkdirs();
            File _picFile = new File(_picDir, fileName + Utility.plyTyp);*/
            File _picFile;
            if(localFile)
                _picFile = new File(Utility.getLocalFileDir(ctx), fileName + Utility.plyTyp);
            else
                _picFile = new File(Utility.getDoodleDir(), fileName + Utility.plyTyp);

            FileOutputStream _fos = new FileOutputStream(_picFile);
            FileWriter writer = new FileWriter(_picFile);

            writer.write(getJson(ctx , doodleWidth, doodleHeight));
            //ObjectOutputStream plObj = new ObjectOutputStream(_fos);
            //plObj.writeObject(this);
            writer.flush();
            writer.close();
            _fos.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static final String colorArray = "colorArray";
    private static final String colorArrayArray = "colorArrayArray";
    private static final String pathArray = "pathArray";
    private static final String pathArrayArray = "pathArrayArray";
    private static final String widthArray = "widthArray";
    private static final String widthArrayArray = "widthArrayArray";
    private static final String fFloat = "fFloat";
    private static final String nFloat = "nFloat";
    private static final String fVal = "fVal";
    private static final String playArray = "playArray";
    private static final String xDensity = "xDensity";
    private static final String yDensity = "yDensity";
    private static final String wPixel = "wPixel";
    private static final String hPixel = "hPixel";

    String getJson(Context ctx , int doodleWidth , int doodleHeight) {

        //if(doodleWidth < doodleHeight)
          //  return null;

        float ctxXdpi = ctx.getResources().getDisplayMetrics().xdpi;
        float ctxYdpi = ctx.getResources().getDisplayMetrics().ydpi;
        int ctxW = ctx.getResources().getDisplayMetrics().widthPixels;
        int ctxH = ctx.getResources().getDisplayMetrics().heightPixels;
        if(doodleWidth > 0 && doodleHeight > 0) {
            ctxW = doodleWidth;
            ctxH = doodleHeight;
        }

        if(ctxW < ctxH) {
            int tmp = ctxW;
            ctxW = ctxH;
            ctxH = tmp;
        }
        StringBuilder ret = new StringBuilder();

        if(playPathArray.size() <= 0 || playPaintColorArray.size() <= 0
                || playPaintStrokeWidthArray.size() <= 0)
            return ret.toString();

        ret.append("{");
        ret.append("\"");
        ret.append(playArray);
        ret.append("\"");
        ret.append(":");
        ret.append("[");
        //////////
        ret.append("{");
        ret.append("\"");
        ret.append(pathArrayArray);
        ret.append("\"");
        ret.append(":");
        ret.append("[");
        for (int cnt = 0; cnt < playPathArray.size(); cnt++) {
            ret.append("{");
            ret.append("\"");
            ret.append(pathArray);
            ret.append("\"");
            ret.append(":");
            ret.append("[");
            ArrayList<FloatPoint> curArr = playPathArray.get(cnt);
            for(int ct = 0; ct < curArr.size(); ct++) {
                ret.append("{");
                ret.append("\"");
                ret.append(fFloat);
                ret.append("\"");
                ret.append(":");
                ret.append(curArr.get(ct).xVal);
                ret.append(",");

                ret.append("\"");
                ret.append(nFloat);
                ret.append("\"");
                ret.append(":");
                ret.append(curArr.get(ct).yVal);
                ret.append("}");
                if(ct < curArr.size() - 1)
                    ret.append(",");
            }
            ret.append("]");
            ret.append("}");
            if(cnt < playPathArray.size() - 1)
                ret.append(",");

        }
        ret.append("]");
        ret.append("},");
        //////////////

        /////////////////
        //////////
        ret.append("{");
        ret.append("\"");
        ret.append(colorArrayArray);
        ret.append("\"");
        ret.append(":");
        ret.append("[");
        for (int cnt = 0; cnt < playPaintColorArray.size(); cnt++) {
            ret.append("{");
            ret.append("\"");
            ret.append(colorArray);
            ret.append("\"");
            ret.append(":");
            ret.append("[");
            ArrayList<Integer> curArr = playPaintColorArray.get(cnt);
            for(int ct = 0; ct < curArr.size(); ct++) {
                ret.append("{");
                ret.append("\"");
                ret.append(fVal);
                ret.append("\"");
                ret.append(":");
                ret.append(curArr.get(ct));
                ret.append("}");
                if(ct < curArr.size() - 1)
                    ret.append(",");
            }
            ret.append("]");
            ret.append("}");
            if(cnt < playPaintColorArray.size() - 1)
                ret.append(",");
        }
        ret.append("]");
        ret.append("},");
        //////////////


        //////////
        ret.append("{");
        ret.append("\"");
        ret.append(widthArrayArray);
        ret.append("\"");
        ret.append(":");
        ret.append("[");

        for (int cnt = 0; cnt < playPaintStrokeWidthArray.size(); cnt++) {
            ret.append("{");
            ret.append("\"");
            ret.append(widthArray);
            ret.append("\"");
            ret.append(":");
            ret.append("[");
            ArrayList<Integer> curArr = playPaintStrokeWidthArray.get(cnt);
            for(int ct = 0; ct < curArr.size(); ct++) {
                ret.append("{");
                ret.append("\"");
                ret.append(fVal);
                ret.append("\"");
                ret.append(":");
                ret.append(curArr.get(ct));
                ret.append("}");
                if(ct < curArr.size() - 1)
                    ret.append(",");
            }
            ret.append("]");
            ret.append("}");
            if(cnt < playPaintStrokeWidthArray.size() - 1)
                ret.append(",");
        }
        ret.append("]");
        ret.append("},");
        //////////////
        /////////////
        ret.append("{");
        ret.append("\"");
        ret.append(xDensity);
        ret.append("\"");
        ret.append(":");
        ret.append(ctxXdpi);
        ret.append("}");
        ret.append(",");
        ret.append("{");
        ret.append("\"");
        ret.append(yDensity);
        ret.append("\"");
        ret.append(":");
        ret.append(ctxYdpi);
        ret.append("}");
        /////////////
        /////////////
        ret.append(",");
        ret.append("{");
        ret.append("\"");
        ret.append(wPixel);
        ret.append("\"");
        ret.append(":");
        ret.append(ctxW);
        ret.append("}");
        ret.append(",");
        ret.append("{");
        ret.append("\"");
        ret.append(hPixel);
        ret.append("\"");
        ret.append(":");
        ret.append(ctxH);
        ret.append("}");

        //////////////////
        ret.append("]");
        ret.append("}");
        return ret.toString();
    }


    void scaleX(float factor) {

        if(playPathArray.size() <= 0 || playPaintColorArray.size() <= 0
                || playPaintStrokeWidthArray.size() <= 0)
            return;

        for (int cnt = 0; cnt < playPathArray.size(); cnt++) {
            ArrayList<FloatPoint> curArr = playPathArray.get(cnt);
            for(int ct = 0; ct < curArr.size(); ct++) {
                curArr.get(ct).xVal *= factor;
                //curArr.get(ct).yVal *= factor;;
            }
        }

        /*for (int cnt = 0; cnt < playPaintStrokeWidthArray.size(); cnt++) {
            ArrayList<Integer> curArr = playPaintStrokeWidthArray.get(cnt);
            for(int ct = 0; ct < curArr.size(); ct++) {
                curArr.get(ct) = (Integer)((float)curArr.get(ct) * factor);
            }
        }*/
    }


    void scaleY(float factor) {

        if(playPathArray.size() <= 0 || playPaintColorArray.size() <= 0
                || playPaintStrokeWidthArray.size() <= 0)
            return;

        for (int cnt = 0; cnt < playPathArray.size(); cnt++) {
            ArrayList<FloatPoint> curArr = playPathArray.get(cnt);
            for(int ct = 0; ct < curArr.size(); ct++) {
                //curArr.get(ct).xVal *= factor;
                curArr.get(ct).yVal *= factor;;
            }
        }

        /*for (int cnt = 0; cnt < playPaintStrokeWidthArray.size(); cnt++) {
            ArrayList<Integer> curArr = playPaintStrokeWidthArray.get(cnt);
            for(int ct = 0; ct < curArr.size(); ct++) {
                curArr.get(ct) = (Integer)((float)curArr.get(ct) * factor);
            }
        }*/
    }


     void scaleWidth(float factor) {
         if(playPathArray.size() <= 0 || playPaintColorArray.size() <= 0
                 || playPaintStrokeWidthArray.size() <= 0)
             return;

        ArrayList<ArrayList<Integer>> tmpList = new ArrayList<ArrayList<Integer>>();
        for (int cnt = 0; cnt < playPaintStrokeWidthArray.size(); cnt++) {
            ArrayList<Integer> curArr = playPaintStrokeWidthArray.get(cnt);
            ArrayList<Integer> tmpArr = new ArrayList<Integer>();
            for(int ct = 0; ct < curArr.size(); ct++) {
                tmpArr.add(((int) ((float) curArr.get(ct) * factor)));
            }
            tmpList.add(tmpArr);
        }
        playPaintStrokeWidthArray = tmpList;
     }

    static PlayImageArray createPlayImage(String json , Context ctx, int doodleWidth, int doodleHeight) {
        //if(doodleWidth < doodleHeight)
        //    return null;

        float curXdpi = ctx.getResources().getDisplayMetrics().xdpi;
        float curYdpi = ctx.getResources().getDisplayMetrics().ydpi;
        int curW = ctx.getResources().getDisplayMetrics().widthPixels;
        int curH = ctx.getResources().getDisplayMetrics().heightPixels;
        if(doodleWidth > 0 && doodleHeight > 0) {
            curW = doodleWidth;
            curH = doodleHeight;
        }
        if(curW < curH) {
            int tmp = curW;
            curW = curH;
            curH = tmp;
        }

        float jsonXdpi = ctx.getResources().getDisplayMetrics().xdpi;
        float jsonYdpi = ctx.getResources().getDisplayMetrics().ydpi;

        int jsonW = ctx.getResources().getDisplayMetrics().widthPixels;
        int jsonH = ctx.getResources().getDisplayMetrics().heightPixels;
        if(jsonW < jsonH) {
            int tmp = jsonW;
            jsonW = jsonH;
            jsonH = tmp;
        }


        if(json.isEmpty())
            return null;
        PlayImageArray ret = new PlayImageArray();
        JSONObject obj = null;
        try {
            //json = "{\"playArray\":[{\"pathArrayArray\":[{\"pathArray\":[{\"fFloat\":276.63922,\"nFloat\":180.51974},{\"fFloat\":281.97382,\"nFloat\":180.51974},]}]},{\"colorArrayArray\":[{\"colorArray\":[{\"fVal\":-16777216},{\"fVal\":-16777216}]}]},{\"widthArrayArray\":[{\"widthArray\":[{\"fVal\":10},{\"fVal\":10}]}]} ]}";
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        JSONArray rootArr = obj.optJSONArray(playArray);

        for(int cnt = 0; cnt < rootArr.length(); cnt++) {
            if(cnt == 0) {
                JSONArray arr = rootArr.optJSONObject(cnt).optJSONArray(pathArrayArray);
                for (int ct = 0; ct < arr.length(); ct++) {
                    JSONArray tmpJosnArr = arr.optJSONObject(ct).optJSONArray(pathArray);
                    ArrayList<FloatPoint> tmpArr = new ArrayList<FloatPoint>();
                    for (int ctc = 0; ctc < tmpJosnArr.length(); ctc++) {
                        JSONObject tmpObj = tmpJosnArr.optJSONObject(ctc);
                        float fFloatVal = (float) tmpObj.optDouble(fFloat);
                        float nFloatVal = (float) tmpObj.optDouble(nFloat);
                        FloatPoint tmpFloat = new FloatPoint(fFloatVal, nFloatVal);
                        tmpArr.add(tmpFloat);
                    }
                    ret.playPathArray.add(tmpArr);

                }
            }
            else if(cnt == 1) {
                JSONArray arr = rootArr.optJSONObject(cnt).optJSONArray(colorArrayArray);
                for (int ct = 0; ct < arr.length(); ct++) {
                    JSONArray tmpJosnArr = arr.optJSONObject(ct).optJSONArray(colorArray);
                    ArrayList<Integer> tmpArr = new ArrayList<Integer>();
                    for (int ctc = 0; ctc < tmpJosnArr.length(); ctc++) {
                        JSONObject tmpObj = tmpJosnArr.optJSONObject(ctc);
                        Integer tmpVal = tmpObj.optInt(fVal);
                        tmpArr.add(tmpVal);
                    }
                    ret.playPaintColorArray.add(tmpArr);
                }
            }
            else if(cnt == 2){
                JSONArray arr = rootArr.optJSONObject(cnt).optJSONArray(widthArrayArray);;

                for (int ct = 0; ct < arr.length(); ct++) {
                    JSONArray tmpJosnArr = arr.optJSONObject(ct).optJSONArray(widthArray);
                    ArrayList<Integer> tmpArr = new ArrayList<Integer>();
                    for (int ctc = 0; ctc < tmpJosnArr.length(); ctc++) {
                        JSONObject tmpObj = tmpJosnArr.optJSONObject(ctc);
                        Integer tmpVal = tmpObj.optInt(fVal);
                        tmpArr.add(tmpVal);
                    }
                    ret.playPaintStrokeWidthArray.add(tmpArr);
                }
            }
            else if(cnt == 3) {
                jsonXdpi = (float)rootArr.optJSONObject(cnt).optDouble(xDensity);
            }
            else if(cnt == 3+1){
                jsonYdpi = (float)rootArr.optJSONObject(cnt).optDouble(yDensity);
            }
            else if(cnt == 5){
                jsonW = rootArr.optJSONObject(cnt).optInt(wPixel);
            }
            else if(cnt == 6){
                jsonH = rootArr.optJSONObject(cnt).optInt(hPixel);
            }



        }
        float curPaprW = /*(curXdpi/160.0f) * */(float)curW;
        float curPaprH = /*(curYdpi/160.0f) * */(float)curH;

        float jsonPaprW = /*(jsonXdpi/160.0f) * */(float)jsonW;
        float jsonPaprH = /*(jsonYdpi/160.0f) * */(float)jsonH;

        if(curPaprW < curPaprH) {
            float tmp = curPaprW;
            curPaprW = curPaprH;
            curPaprH = tmp;
        }
        if(jsonPaprW < jsonPaprH) {
            float tmp = jsonPaprW;
            jsonPaprW = jsonPaprH;
            jsonPaprH = tmp;
        }

        float jsonAspectRat = jsonPaprW / jsonPaprH;
        float curAspectRat = curPaprW / curPaprH;
        if(jsonPaprW == curPaprW && jsonPaprH == curPaprH)
            return ret;
        if(jsonAspectRat < curAspectRat) {
            float factorY = curPaprH/jsonPaprH;
            //float facYBase = curYdpi/160.0f;
            //ret.scaleY(factorY/facYBase);
            ret.scaleY(factorY);

            float factorX = (curPaprH * jsonAspectRat) / jsonPaprW;
            //float facXBase = curXdpi/160.0f;
            //ret.scaleX(factorX/facXBase);
            ret.scaleX(factorX);


            float factor = ( curPaprH ) / (jsonPaprH);
            ret.scaleWidth(factor);

        }
        else {
            float factorX = curPaprW/jsonPaprW;
            //float facXBase = curXdpi/160.0f;
            //ret.scaleX(factorX/facXBase);
            ret.scaleX(factorX);

            float factorY = (curPaprW / jsonAspectRat) / jsonPaprH;
            //float facYBase = curYdpi/160.0f;
            //ret.scaleY(factorY/facYBase);
            ret.scaleY(factorY);
            float factor = ( curPaprW ) / (jsonPaprW);
            ret.scaleWidth(factor);

        }

        /*///////////////////
            //if(curXdpi < jsonXdpi)
            {
            float factor = curPaprW/jsonPaprW;
            ret.scaleX(factor);
            }
            //if(curYdpi < jsonYdpi)
            {
            float factor = curPaprH/jsonPaprH;
            ret.scaleY(factor);
            }
        {
            float factor = ( curPaprW) / (jsonPaprW);
            ret.scaleWidth(factor);
        }
        /////////////////////////
        */
        return ret;
    }

}
