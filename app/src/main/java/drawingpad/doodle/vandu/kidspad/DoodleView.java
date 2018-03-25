package drawingpad.doodle.vandu.kidspad;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by vandu on 10-05-2015.
 */

public class DoodleView extends View
{
    Path drawPlayPath;
    Paint playPaint;
    Handler mHandler = new PlayHandler();
    private Canvas playCanvas;
    private Bitmap playBitmap;
    private Bitmap loadedBitmap;
    Context mContext;
    int curWidth;
    int curHeight;
    private SurfaceHolder holder;
    private Path drawPath;
    private FloatPoint drawPointForSave;
    private FloatPoint drawPointForDraw;
    private Paint drawPaint;
    private Paint canvasPaint;
    private int paintColor = Color.BLACK;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private int brushColor;
    private int strokWidth;
    private int eraserWidth;
    private int brushWidth;
    private final long playDelay = 15;
    boolean playMode = false;
    private Bitmap backBitmap = null;

    int prvBrushWidth = brushWidth;
    int prvBrushColor = brushColor;
    public DoodleView(Context context)
    {
        this(context, null, 0);
    }

    public DoodleView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.brushColor = Color.BLACK;//Color.TRANSPARENT;

        this.strokWidth = (int)mContext.getResources().getDimension(R.dimen.stoke_width);
        this.eraserWidth = (int)mContext.getResources().getDimension(R.dimen.erase_width);

        init();

    }



    /*PlayImageArray getPlayObjFromFile(String fileName) {
        Log.e("rt" , "rtrt " + fileName);
        PlayImageArray retObj = null;
        FileInputStream _fpl = null;
        try {
            _fpl = new FileInputStream(fileName);
            ObjectInputStream objPly = new ObjectInputStream(_fpl);
            retObj = (PlayImageArray)objPly.readObject();
            objPly.close();
            _fpl.close();
            Log.e("rt" , "rtrt uyuyuyu" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retObj;
    }*/

    PlayImageArray getPlayObjFromFile(String fileName) {
        StringBuilder jsonStr = new StringBuilder();
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader bufReader = new BufferedReader(reader);

            String line = bufReader.readLine();
            while(line != null) {
                jsonStr.append(line);
                line = bufReader.readLine();
            }
            bufReader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return PlayImageArray.createPlayImage(jsonStr.toString(), mContext,
                /*((Activity)mContext).getWindow().getDecorView().*/getWidth(),
                /*((Activity)mContext).getWindow().getDecorView().*/getHeight());
    }

    PlayImageArray plyImageArray;
    void setPlayImageArray(PlayImageArray tmpImageArray) {
        if(tmpImageArray != null) {
            plyImageArray = tmpImageArray;
            //PlayImageArray.createPlayImage(plyImageArray.getJson());
        }
    }

    interface PlayFinishListner {
        void onPlayComplete();
    }
    PlayFinishListner playFinishListner;
    void setPlayListner(PlayFinishListner playFinish) {
        playFinishListner = playFinish;
    }

    void init()
    {
        this.brushWidth = this.strokWidth;

        this.drawPath = new Path();
        this.drawPaint = new Paint();
        this.drawPaint.setColor(this.brushColor);
        this.drawPaint.setAntiAlias(true);
        this.drawPaint.setStrokeWidth(this.strokWidth);
        this.drawPaint.setStyle(Paint.Style.STROKE);
        this.drawPaint.setStrokeJoin(Paint.Join.ROUND);
        this.drawPaint.setStrokeCap(Paint.Cap.ROUND);

        this.drawPlayPath = new Path();
        this.playPaint = new Paint();
        this.playPaint.setColor(this.brushColor);
        this.playPaint.setAntiAlias(true);
        this.playPaint.setStrokeWidth(this.strokWidth);
        this.playPaint.setStyle(Paint.Style.STROKE);
        this.playPaint.setStrokeJoin(Paint.Join.ROUND);
        this.playPaint.setStrokeCap(Paint.Cap.ROUND);

        this.loadedBitmap = null;

        this.canvasPaint = new Paint(Paint.DITHER_FLAG);

        if (this.playCanvas != null) {
            this.playCanvas.drawColor(Color.WHITE);
        }

        plyImageArray = new PlayImageArray();

        drawPointForSave = null;
        drawPointForDraw = null;
        playMode = false;
        this.mHandler.removeMessages(PLAY_MSG);
        backBitmap = null;
    }


    void setBackBitmap(Bitmap bkBitmap) {
        backBitmap = bkBitmap;
    }



    final int PLAY_MSG = 5;

    class PlayHandler
            extends Handler
    {
        PlayHandler() {}

        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case PLAY_MSG:
                    if ((DoodleView.this.mCurCnt < DoodleView.this.plyImageArray.playPathArray.size()) && (DoodleView.this.mCurCt < ((ArrayList)DoodleView.this.plyImageArray.playPathArray.get(DoodleView.this.mCurCnt)).size()))
                    {
                        if (DoodleView.this.mCurCt == 0)
                        {
                            if (DoodleView.this.playCanvas != null)
                            {
                                DoodleView.this.playCanvas.drawPath(DoodleView.this.drawPlayPath, DoodleView.this.playPaint);
                                DoodleView.this.drawPlayPath.reset();

                            }
                            DoodleView.this.drawPlayPath.moveTo(((FloatPoint) ((ArrayList) DoodleView.this.plyImageArray.playPathArray.get(DoodleView.this.mCurCnt)).get(0)).xVal,
                                    ((FloatPoint) ((ArrayList) DoodleView.this.plyImageArray.playPathArray.get(DoodleView.this.mCurCnt)).get(0)).yVal);
                            DoodleView.this.playPaint.setColor(((Integer) ((ArrayList) DoodleView.this.plyImageArray.playPaintColorArray.get(DoodleView.this.mCurCnt)).get(DoodleView.this.mCurCt)).intValue());
                            DoodleView.this.playPaint.setStrokeWidth(((Integer) ((ArrayList) DoodleView.this.plyImageArray.playPaintStrokeWidthArray.get(DoodleView.this.mCurCnt)).get(DoodleView.this.mCurCt)).intValue());

                            if (DoodleView.this.playCanvas != null)
                                DoodleView.this.playCanvas.drawPoint(((FloatPoint)((ArrayList)DoodleView.this.plyImageArray.playPathArray.get(DoodleView.this.mCurCnt)).get(0)).xVal,
                                    ((FloatPoint)((ArrayList)DoodleView.this.plyImageArray.playPathArray.get(DoodleView.this.mCurCnt)).get(0)).yVal , DoodleView.this.playPaint);

                            drawPointForDraw = new FloatPoint(((FloatPoint)((ArrayList)DoodleView.this.plyImageArray.playPathArray.get(DoodleView.this.mCurCnt)).get(0)).xVal,
                                    ((FloatPoint)((ArrayList)DoodleView.this.plyImageArray.playPathArray.get(DoodleView.this.mCurCnt)).get(0)).yVal);

                        }
                        else
                        {
                            DoodleView.this.drawPlayPath.lineTo(((FloatPoint)((ArrayList)DoodleView.this.plyImageArray.playPathArray.get(DoodleView.this.mCurCnt)).get(DoodleView.this.mCurCt)).xVal,
                                    ((FloatPoint)((ArrayList)DoodleView.this.plyImageArray.playPathArray.get(DoodleView.this.mCurCnt)).get(DoodleView.this.mCurCt)).yVal);
                            DoodleView.this.playPaint.setColor(((Integer)((ArrayList)DoodleView.this.plyImageArray.playPaintColorArray.get(DoodleView.this.mCurCnt)).get(DoodleView.this.mCurCt)).intValue());
                            DoodleView.this.playPaint.setStrokeWidth(((Integer)((ArrayList)DoodleView.this.plyImageArray.playPaintStrokeWidthArray.get(DoodleView.this.mCurCnt)).get(DoodleView.this.mCurCt)).intValue());
                        }
                        DoodleView.this.invalidate();
                        DoodleView.this.mCurCt += 1;
                        if (DoodleView.this.mCurCt >= ((ArrayList)DoodleView.this.plyImageArray.playPathArray.get(DoodleView.this.mCurCnt)).size())
                        {
                            DoodleView.this.mCurCnt += 1;
                            DoodleView.this.mCurCt = 0;
                        }
                        Message plMsg = Message.obtain();
                        plMsg.what = PLAY_MSG;
                        sendMessageDelayed(plMsg, playDelay);
                    }
                    else
                    {
                        DoodleView.this.playCanvas.drawPath(DoodleView.this.drawPlayPath, DoodleView.this.playPaint);
                        DoodleView.this.playMode = false;
                        playFinishListner.onPlayComplete();
                    }
                    break;
            }
        }
    }

    private ColorMatrix getColorMatrix()
    {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0F);

        float m = 255.0F;
        float t = -32640.0F;
        ColorMatrix threshold = new ColorMatrix(new float[] { m, 0.0F, 0.0F, 1.0F, t, 0.0F, m, 0.0F, 1.0F, t, 0.0F, 0.0F, m, 1.0F, t, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F });

        colorMatrix.postConcat(threshold);

        return colorMatrix;
    }

    void setBrushColor(int color)
    {
        this.brushColor = color;
        this.brushWidth = this.strokWidth;
        //drawPaint.setXfermode(null);
        setBrush();
    }

    int getBrushColor()
    {
        return this.brushColor;
    }

    void setBrushWidth(int strokWidth)
    {
        this.brushWidth = strokWidth;
        this.strokWidth = strokWidth;
        setBrush();
    }

    boolean isInPlayMode() {
        return playMode;
    }

    Bitmap getPlayBitmap() {
        return playBitmap;
    }

    int getBrushWidth()
    {
        return this.brushWidth;
    }

    public void setEraserWidth(int eraserW)
    {
        this.eraserWidth = eraserW;
        setEraser();
    }

    public void setEraser()
    {
        this.brushColor = Color.WHITE;//Integer.MIN_VALUE;
        this.brushWidth = this.eraserWidth;
        //drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setBrush();
    }

    private void setBrush()
    {
        this.drawPaint.setColor(this.brushColor);
        this.drawPaint.setStrokeWidth(this.brushWidth);
        //drawPaint.setXfermode(null);
    }



    public void newCanvas()
    {
        this.drawPaint.setColor(this.brushColor);
        this.drawPaint.setStrokeWidth(this.strokWidth);
        this.drawCanvas.drawColor(Color.WHITE); //org
        //this.drawCanvas.drawColor(Color.TRANSPARENT); // mod
        /*if(backBitmap == null) {

            drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        else {

            drawCanvas.drawColor(Color.WHITE);
        }*/
        this.playCanvas.drawColor(Color.WHITE);
        //this.playCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        init();
        invalidate();
    }

    public void playDoodle()
    {
        if(playCanvas == null)
            return;
        this.drawPlayPath = new Path();
        this.playCanvas.drawColor(Color.WHITE);
        if (this.loadedBitmap != null) {
            this.playCanvas.drawBitmap(this.loadedBitmap, 0.0F, 0.0F, this.playPaint);
        }
        playDraw();
    }

    int mCurCnt = 0;
    int mCurCt = 0;

    public void playDraw()
    {
        if(!playMode) {
            startPlay();
        }
        else {
            stopPlay();
        }
    }

    public void startPlay() {
        this.mCurCnt = 0;
        this.mCurCt = 0;
        this.playMode = true;
        //saveDraw();    //////// On Finish we can draw just bu JSON, No need to save

        Message msg = Message.obtain();
        msg.what = PLAY_MSG;
        this.mHandler.sendMessageDelayed(msg, playDelay);
    }


    public void stopPlay() {
        playMode = false;
        this.mHandler.removeMessages(PLAY_MSG);
        //invalidate();
        createDrawcanvasFromPlaycanvas();
    }



    void saveDraw()
    {
        if (this.drawCanvas != null) {
            //this.drawCanvas.drawPath(this.drawPath, this.drawPaint);
            if(drawPointForSave != null) {
                drawCanvas.drawPoint(drawPointForSave.xVal, drawPointForSave.yVal, drawPaint);
                drawPointForSave = null;
            }
            /*else*/ {
                this.drawCanvas.drawPath(this.drawPath, this.drawPaint);
            }
        }
    }

    public void setImage(Bitmap tmpBitmap)
    {
        if (tmpBitmap != null)
        {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(tmpBitmap, this.curWidth, this.curHeight, false);

            init();
            this.drawCanvas.drawBitmap(resizedBitmap, 0.0F, 0.0F, this.drawPaint);
            invalidate();
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        long bmpVal = w * h;
        if(bmpVal > (long)Integer.MAX_VALUE || w < 0 || h < 0)
            return;
        this.curWidth = w;
        this.curHeight = h;
        this.canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //canvasBitmap.eraseColor(Color.TRANSPARENT);

        this.drawCanvas = new Canvas(this.canvasBitmap);
        this.drawCanvas.drawColor(Color.WHITE);
        //drawCanvas.drawColor(Color.WHITE , PorterDuff.Mode.CLEAR);

        this.playBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.playCanvas = new Canvas(this.playBitmap);
        this.playCanvas.drawColor(Color.WHITE);
        //playCanvas.drawColor(Color.WHITE , PorterDuff.Mode.CLEAR);


        /*this.backBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas backCanvas = new Canvas(this.backBitmap);
        backCanvas.drawColor(Color.WHITE);
*/
        //((MainActivity)mContext).doodleWidthAfterLayout = w;
        //((MainActivity)mContext).doodleHeightAfterLayout = h;

    }


    /*
    HashMap<Integer,Integer> touchMap = new HashMap<Integer,Integer>();
    public boolean onTouchEvent(MotionEvent event)
    {
        if(this.playMode) {
            return true;
        }
        Log.e("^^^^^^" , event.getActionMasked()+ " " + event.getActionIndex());
        int pointerCount = event.getPointerCount();

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        float touchX;
        float touchY;
        //for (int cnt = 0; cnt < pointerCount; cnt++) {





            switch (maskedAction) {
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_DOWN:
                    touchX = event.getX(pointerIndex);
                    touchY = event.getY(pointerIndex);
                    this.drawPath.reset();
                    this.drawPath.moveTo(touchX, touchY);
                    this.drawPath.lineTo(touchX, touchY);

                    touchMap.put(pointerId , this.plyImageArray.playPathArray.size());
                    this.plyImageArray.playPathArray.add(new ArrayList());
                    ((ArrayList) this.plyImageArray.playPathArray.get(touchMap.get((Integer)pointerId))).add(new FloatPoint(touchX, touchY));

                    this.plyImageArray.playPaintColorArray.add(new ArrayList());
                    ((ArrayList) this.plyImageArray.playPaintColorArray.get(touchMap.get((Integer)pointerId))).add(Integer.valueOf(this.brushColor));

                    this.plyImageArray.playPaintStrokeWidthArray.add(new ArrayList());
                    ((ArrayList) this.plyImageArray.playPaintStrokeWidthArray.get(touchMap.get((Integer)pointerId))).add(Integer.valueOf(this.brushWidth));
                    break;
                case MotionEvent.ACTION_MOVE:
                    for(int count = 0; count < event.getPointerCount(); count++) {
                        touchX = event.getX(count);
                        touchY = event.getY(count);
                        this.drawPath.lineTo(touchX, touchY);

                        if (plyImageArray.playPathArray.size() > 0)
                            ((ArrayList) this.plyImageArray.playPathArray.
                                    get(touchMap.get((Integer) event.getPointerId(count)))).add(new FloatPoint(touchX, touchY));

                        if (plyImageArray.playPaintColorArray.size() > 0)
                            ((ArrayList) this.plyImageArray.playPaintColorArray.
                                    get(touchMap.get((Integer) event.getPointerId(count)))).add(Integer.valueOf(this.brushColor));

                        if (plyImageArray.playPaintStrokeWidthArray.size() > 0)
                            ((ArrayList) this.plyImageArray.playPaintStrokeWidthArray.
                                    get(touchMap.get((Integer) event.getPointerId(count)))).add(Integer.valueOf(this.brushWidth));
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP:
                    saveDraw();
                    this.drawPath.reset();
                    touchMap.remove(pointerId);
                    break;
                //default:
                //    return false;
            }

        //}
        invalidate();

        return true;
    }

     */


    boolean isUndo = false;
    void undo() {
        int lastPathLen = plyImageArray.playPathArray.size();
        if(lastPathLen <= 0 || playMode)
            return;
        isUndo = true;
        prvBrushWidth = brushWidth;
        prvBrushColor = brushColor;

        plyImageArray.playPathArray.remove(lastPathLen-1);
        plyImageArray.playPaintStrokeWidthArray.remove(lastPathLen-1);
        plyImageArray.playPaintColorArray.remove(lastPathLen-1);

        createDrawcanvasFromPlaycanvas();

    }

    void createDrawcanvasFromPlaycanvas() {
        drawCanvas.drawColor(Color.WHITE);
        for(int curCnt = 0; curCnt < plyImageArray.playPathArray.size(); curCnt++) {
            ArrayList<FloatPoint> allPoints = plyImageArray.playPathArray.get(curCnt);
           this.drawPath.moveTo(allPoints.get(0).xVal, allPoints.get(0).yVal);
           drawPointForSave = new FloatPoint(allPoints.get(0).xVal, allPoints.get(0).yVal);
            //drawPointForDraw = new FloatPoint(allPoints.get(0).xVal, allPoints.get(0).yVal);
            //invalidate();
            brushWidth = plyImageArray.playPaintStrokeWidthArray.get(curCnt).get(0);
            brushColor = plyImageArray.playPaintColorArray.get(curCnt).get(0);
            setBrush();
            saveDraw();
            for(int cnt = 1; cnt < allPoints.size(); cnt++) {
                this.drawPath.lineTo(allPoints.get(cnt).xVal, allPoints.get(cnt).yVal);
            }
            saveDraw();
            drawPath.reset();
        }
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if(this.playMode) {
            return true;
        }
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.drawPath.moveTo(touchX, touchY);
                //this.drawPath.lineTo(touchX, touchY);

                int cnt = this.plyImageArray.playPathArray.size();
                this.plyImageArray.playPathArray.add(new ArrayList());
                ((ArrayList) this.plyImageArray.playPathArray.get((Integer)cnt)).add(new FloatPoint(touchX, touchY));

                this.plyImageArray.playPaintColorArray.add(new ArrayList());
                ((ArrayList) this.plyImageArray.playPaintColorArray.get(((Integer)cnt))).add(Integer.valueOf(this.brushColor));

                this.plyImageArray.playPaintStrokeWidthArray.add(new ArrayList());
                ((ArrayList) this.plyImageArray.playPaintStrokeWidthArray.get(((Integer)cnt))).add(Integer.valueOf(this.brushWidth));
                drawPointForSave = new FloatPoint(touchX , touchY);
                drawPointForDraw = new FloatPoint(touchX , touchY);
                saveDraw();
                break;
            case MotionEvent.ACTION_MOVE:
                this.drawPath.lineTo(touchX, touchY);

                int cntv = this.plyImageArray.playPathArray.size()-1;
                if (plyImageArray.playPathArray.size() > 0)
                    ((ArrayList) this.plyImageArray.playPathArray.get(((Integer)cntv))).add(new FloatPoint(touchX, touchY));

                if (plyImageArray.playPaintColorArray.size() > 0)
                    ((ArrayList) this.plyImageArray.playPaintColorArray.get(((Integer)cntv))).add(Integer.valueOf(this.brushColor));

                if (plyImageArray.playPaintStrokeWidthArray.size() > 0)
                    ((ArrayList) this.plyImageArray.playPaintStrokeWidthArray.get(((Integer)cntv))).add(Integer.valueOf(this.brushWidth));
                break;
            case MotionEvent.ACTION_UP:
                saveDraw();
                this.drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void draw(Canvas canvas)
    {
        if(backBitmap != null)
            canvas.drawBitmap(this.backBitmap, 0.0F, 0.0F, this.canvasPaint);

        if (!this.playMode) {
            canvas.drawBitmap(this.canvasBitmap, 0.0F, 0.0F, this.canvasPaint);
            if(drawPointForDraw != null) {
                canvas.drawPoint(drawPointForDraw.xVal, drawPointForDraw.yVal, drawPaint);
                drawPointForDraw = null;
            }
            /*else*/ {
                canvas.drawPath(this.drawPath, this.drawPaint);
            }
            if(isUndo) {
                isUndo = false;
                brushWidth = prvBrushWidth;
                brushColor = prvBrushColor;
                setBrush();
                //invalidate();
            }

        }
        else {
            canvas.drawBitmap(this.playBitmap, 0.0F, 0.0F, this.canvasPaint);
            if(drawPointForDraw != null) {
                canvas.drawPoint(drawPointForDraw.xVal, drawPointForDraw.yVal, this.playPaint);
                drawPointForDraw = null;
            }
            /*else*/ {
                canvas.drawPath(this.drawPlayPath, this.playPaint);
            }
        }
    }

    boolean writeToSdCard() {
        String fileName = Utility.imgPrx + System.currentTimeMillis();
        return writeToTmp(fileName);
    }

    boolean writeToLocalFile() {
        String fileName = Utility.imgPrx + System.currentTimeMillis();
        return writeToTmp(fileName, true, getWidth(), getHeight(), true);
    }

    boolean writeToTmp(String fileName) {
        return writeToTmp(fileName , true);
    }
    boolean writeToTmp(String fileName , boolean jsonSave) {
        return writeToTmp(fileName, jsonSave, getWidth(), getHeight(), false);
    }

    boolean writeToTmp(String fileName , boolean jsonSave , int doodleWidth, int doodleHeight
    , boolean  localFile)
    {
        //setDrawingCacheEnabled(true);
        File _picFile;
        if(localFile)
            _picFile = new File(Utility.getLocalFileDir(mContext), fileName + Utility.imgTyp);
        else
            _picFile = new File(Utility.getDoodleDir(), fileName + Utility.imgTyp);
        try
        {
            FileOutputStream _fos = new FileOutputStream(_picFile);
            Log.e("", "&&&&&&&&&&&&&&&&&&&&&&&&&& 7" );
            Bitmap bmt = canvasBitmap;//getDrawingCache();
            bmt.compress(Bitmap.CompressFormat.JPEG, 100, _fos);
            _fos.flush();
            _fos.close();
            Log.e("", "&&&&&&&&&&&&&&&&&&&&&&&&&& 8" );
            if(jsonSave)
            plyImageArray.savePlayImage(fileName, mContext,
                    /*((Activity)mContext).getWindow().getDecorView().*/doodleWidth,
                    /*((Activity)mContext).getWindow().getDecorView().*/doodleHeight,
                    localFile);
            Log.e("", "&&&&&&&&&&&&&&&&&&&&&&&&&& 78" );
            //destroyDrawingCache();
            return  true;
        }
        catch (Exception ex)
        {
            if(_picFile != null && _picFile.exists())
                _picFile.delete();
            ex.printStackTrace();
        }
        //destroyDrawingCache();
        return false;
    }

}

