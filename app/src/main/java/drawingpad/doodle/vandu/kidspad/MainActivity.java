package drawingpad.doodle.vandu.kidspad;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;

import com.google.android.gms.ads.AdView;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity
        extends Activity
        implements View.OnClickListener, DoodleView.PlayFinishListner {
    private static final int SELECT_DEVICE_PHOTO = 780;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_FILE = 790;
    DoodleView doodleView;
    ImageButton playDraw;
    private static final int SHOW_FIRST = 778;
    private static final int LOAD_TMP = 779;
    private static final String TMP_RESTORE = "tmp";
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 500;
    View messangerButton;
    private boolean isCurModeChl = false;
    private Handler mainHandler;
    AdView mAdView;
    ////// FB messanger
    private boolean mPickingFBReply = false;
    private String fbPlayJson = "";


    /////////////////////
    private static class ObjectPair {
        Uri imgUri;
        String playFile;
    };

    protected void onCreate(Bundle savedInstanceState) {
        //Log.v("TET" , "********** create");
        //Debug.startMethodTracing("tet");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        isCurModeChl = true;
        mPickingFBReply = false;
        fbPlayJson = "";

        mainHandler = new Handler() {;
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case SHOW_FIRST:
                        showFirstTimeModeDialog();
                        break;
                    case LOAD_TMP:
                        loadDoodleWithUrl(/*((ObjectPair)msg.obj).imgUri*/null ,//We can draw just by JSON NO need to do with Image
                                ((ObjectPair)msg.obj).playFile);
                        break;

                }
                //Log.v("TET" , "********** not create");
                //Debug.stopMethodTracing();
                super.handleMessage(msg);
            }
        };
        //////////////////////
        Intent intent = getIntent();
        if(Intent.ACTION_PICK.equals(intent.getAction())){
            mPickingFBReply = true;
            MessengerThreadParams mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            fbPlayJson = mThreadParams.metadata;
        }

        /*//FacebookSdk.sdkInitialize(getApplicationContext());
        messangerButton = findViewById(R.id.messenger_send_button);
        messangerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*if(checkCardPerm() == false) {
                    toastNoPerm();
                    return;
                }*//*
                //if(doodleView.getWidth() >= doodleView.getHeight())
                    doodleView.writeToTmp(Utility.imgTmp , true, doodleView.getWidth()
                            , doodleView.getHeight(), true);
                ////// PLM need to fix different screen problem.
                String playJson =  doodleView.plyImageArray.getJson(MainActivity.this,
                        *//*getWindow().getDecorView().*//*doodleView.getWidth(),
                        *//*getWindow().getDecorView().*//*doodleView.getHeight());
                String fileName = Utility.imgTmp;
                File _picFile = new File(Utility.getDoodleDir(), fileName + Utility.imgTyp);

                Uri contentUri = Utility.getFileLocalUri(MainActivity.this);//Uri.fr Utility.fbShareUrl;//Uri.fromFile(_picFile);
                String mimeType = "image/jpeg";
                ShareToMessengerParams shareToMessengerParams =
                        ShareToMessengerParams.newBuilder(contentUri, mimeType)
                                .setMetaData(playJson)
                                .build();

                // Sharing from an Activity
                if(mPickingFBReply) {
                    MessengerUtils.finishShareToMessenger(MainActivity.this, shareToMessengerParams);
                }
                else {
                    MessengerUtils.shareToMessenger(
                            MainActivity.this,
                            REQUEST_CODE_SHARE_TO_MESSENGER,
                            shareToMessengerParams);
                }
            }
        }
        );*/

        // contentUri points to the content being shared to Messenger
        /*


                ShareToMessengerParams shareToMessengerParams =
                        ShareToMessengerParams.newBuilder(contentUri, mimeType)
                                .build();

            // Sharing from an Activity
                MessengerUtils.shareToMessenger(
                        this,
                        REQUEST_CODE_SHARE_TO_MESSENGER,
                        shareToMessengerParams);
        */
        //////////////////


        isCurModeChl = Utility.getChlidMode(getApplicationContext());

        this.doodleView = ((DoodleView) findViewById(R.id.doodle_pad));
        doodleView.setPlayListner(this);
        ImageButton chooserColor = (ImageButton) findViewById(R.id.choose_color);
        chooserColor.setOnClickListener(this);

        ImageButton savePic = (ImageButton) findViewById(R.id.save_pic);
        savePic.setOnClickListener(this);

        ImageButton colorEraser = (ImageButton) findViewById(R.id.color_eraser);
        colorEraser.setOnClickListener(this);

        ImageButton pageNew = (ImageButton) findViewById(R.id.pad_new);
        pageNew.setOnClickListener(this);

        ImageButton pckImg = (ImageButton) findViewById(R.id.pick_img);
        pckImg.setOnClickListener(this);

        playDraw = (ImageButton) findViewById(R.id.play_draw);
        playDraw.setOnClickListener(this);


        ImageButton undoImg = (ImageButton) findViewById(R.id.can_undo);
        undoImg.setOnClickListener(this);


        /////////////////////////////
        //ImageButton deviceIamge = (ImageButton) findViewById(R.id.device_image);
        //deviceIamge.setOnClickListener(this);

        //////////////////////////////////

        /*Uncomment if you want ad
        mAdView = new AdView(getApplicationContext());//(AdView) findViewById(R.id.adView);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        RelativeLayout.LayoutParams ly = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        ly.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ly.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ((RelativeLayout)findViewById(R.id.main_container)).addView(mAdView,ly);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        */

        applyChildMode();
        Message fMsg = Message.obtain();
        fMsg.what = SHOW_FIRST;

        //if(!isCurModeChl && !mPickingFBReply)
        //    mainHandler.sendMessageDelayed(fMsg , 3000);

        doodleView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                removeOnGlobalLayoutListener(this);
                //doodleWidthAfterLayout = doodleView.getWidth();
                //doodleHeightAfterLayout = doodleView.getHeight();
                if(mPickingFBReply) {
                    if(fbPlayJson!= null && !fbPlayJson.isEmpty()) {
                        loadJSONDoodle(fbPlayJson);
                        if(doodleView != null)
                            playDoodle(false);


                    }
                }else {
                    //At this point the layout is complete and the
                    //dimensions of myView and any child views are known.
                    String fileName = Utility.imgTmp;
                    File _picFile = new File(/*Utility.getDoodleDir()*/
                            Utility.getLocalFileDir(
                            MainActivity.this), fileName + Utility.imgTyp);
                    String filename = _picFile.getAbsolutePath();
                    if (filename != null) {
                        File tmpFile = new File(filename);
                        if (tmpFile.exists()) {
                            String playFileName = tmpFile.getAbsolutePath().substring(0,
                                    tmpFile.getAbsolutePath().lastIndexOf(".")) + Utility.plyTyp;

                            ObjectPair pr = new ObjectPair();
                            pr.imgUri = Uri.fromFile(tmpFile);
                            pr.playFile = playFileName;
                            Message fMsg = Message.obtain();
                            fMsg.what = LOAD_TMP;
                            fMsg.obj = pr;
                            mainHandler.sendMessageDelayed(fMsg , 150);

                            //loadDoodleWithUrl(Uri.fromFile(tmpFile), playFileName);
                        }
                    }
                }
                Log.v("TET" , "********** no create");
            }
        });

    }



    void checkAndRequestPermision() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_FILE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_FILE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }

    }

    boolean checkCardPerm() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    void toastNoPerm() {
        Toast.makeText(this, R.string.permcard, Toast.LENGTH_LONG).show();
    }

    void applyChildMode() {

        if (isCurModeChl) {
            //messangerButton.setVisibility(View.GONE);
            //Uncomment if you want ad
            //mAdView.setVisibility(View.GONE);
        }
        else {
            //messangerButton.setVisibility(View.VISIBLE);
            //Uncomment if you want ad
            //mAdView.setVisibility(View.VISIBLE);
        }


        if(mPickingFBReply) {
            //messangerButton.setVisibility(View.VISIBLE);
        //Uncomment if you want ad
            //mAdView.setVisibility(View.VISIBLE);
        }
    }


    public void removeOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            doodleView.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            doodleView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    @Override
    protected void onResume() {
        isCurModeChl = Utility.getChlidMode(getApplicationContext());
        //mAdView.loadAd(adRequest);
        //Uncomment if you want ad
        //mAdView.resume();/////uncomment
        makePlayBtn();
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Uncomment if you want ad
        //mAdView.pause();//////uncomment

        //if(doodleView.isInPlayMode())
        //    playDoodle(true);

        //doodleView.stopPlay();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);

    }

    private int PICK_IMAGE_REQUEST = 1;




/*
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/
    int lastColor = -1;
    int lastWidth = -1;

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            doodleView.writeToTmp(Utility.imgTmp);
            String fileName = Utility.imgTmp;
            File _picFile = new File(Utility.getDoodleDir(), fileName + Utility.imgTyp);


            Intent sharingIntent = new Intent(Intent.ACTION_SEND);

            sharingIntent.setType("image/jpg");

            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(_picFile));

            startActivity(Intent.createChooser(sharingIntent, "Share image using"));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void toastOpNotAllowed() {
        Toast.makeText(this, R.string.op_not_allowed, Toast.LENGTH_LONG).show();
    }

    BrushWidthView brushArea;
    AsyncTask asyncTask;
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_color:

                if(doodleView.isInPlayMode()) {
                    toastOpNotAllowed();
                    break;
                }

                final Dialog colorChooserDialog = new Dialog(this);
                colorChooserDialog.requestWindowFeature(1);
                colorChooserDialog.setContentView(R.layout.color_chooser);
                GridView colorGrid = (GridView) colorChooserDialog.findViewById(R.id.color_chooser_layout);
                final ColorAdapter colorAdapter = new ColorAdapter(this);
                colorGrid.setAdapter(colorAdapter);
                colorGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MainActivity.this.doodleView.setBrushColor(colorAdapter.getColor(position));

                        if (brushArea != null) {
                            brushArea.setLineColor(doodleView.getBrushColor());
                            brushArea.invalidate();
                        }
                        if (isCurModeChl)
                            colorChooserDialog.dismiss();
                    }
                });
                colorChooserDialog.show();
                brushArea = (BrushWidthView) colorChooserDialog.findViewById(R.id.brush_width_area);
                /*if(isCurModeChl)
                    brushArea.setVisibility(View.GONE);
                else
                    brushArea.setVisibility(View.VISIBLE);
*/
                brushArea.setCanvasBackgroundColor(Color.WHITE);
                brushArea.setLineColor(Color.BLACK);
                brushArea.setLineWidth(doodleView.getBrushWidth());
                brushArea.setLineColor(doodleView.getBrushColor());

                SeekBar brushBar = (SeekBar) colorChooserDialog.findViewById(R.id.brush_size_bar);
  /*              if(isCurModeChl)
                    brushBar.setVisibility(View.GONE);
                else
                    brushBar.setVisibility(View.VISIBLE);
  */
                brushBar.setProgress(doodleView.getBrushWidth());
                brushBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        doodleView.setBrushWidth(Utility.dpToPx(MainActivity.this ,progress));
                        brushArea.setLineWidth(Utility.dpToPx(MainActivity.this ,progress));
                        brushArea.invalidate();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                break;
            case R.id.save_pic:
                Boolean loc = false;
                if(checkCardPerm() == false) {
                    checkAndRequestPermision();
                    //toastNoPerm();
                    //return;
                    loc = true;
                }
                asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        if( ((Boolean)params[0]) == true)
                            return MainActivity.this.doodleView.writeToLocalFile();
                        else
                            return MainActivity.this.doodleView.writeToSdCard();
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        if(o instanceof Boolean) {
                            if((Boolean)o) {
                                Toast.makeText(MainActivity.this, R.string.toast_save,
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                };
                Object obj = new Object();
                asyncTask.execute(loc);
                /*if(this.doodleView.writeToSdCard())
                    Toast.makeText(MainActivity.this, R.string.toast_save,
                            Toast.LENGTH_LONG).show();
*/
                break;
            case R.id.color_eraser:
                if(doodleView.isInPlayMode()) {
                    toastOpNotAllowed();
                    break;
                }
                /////////////////////////
                final Dialog rubberChooserDialog = new Dialog(this);
                rubberChooserDialog.requestWindowFeature(1);
                rubberChooserDialog.setContentView(R.layout.rubber_chooser_layout);
                GridView rubberGrid = (GridView) rubberChooserDialog.findViewById(R.id.rubber_chooser_view);
                final RubberAdapter rubberAdapter = new RubberAdapter(this);
                rubberGrid.setAdapter(rubberAdapter);
                rubberGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MainActivity.this.doodleView.setEraserWidth(
                                rubberAdapter.getRubberWidth(position));
                        //if(isCurModeChl)
                        rubberChooserDialog.dismiss();
                    }
                });
                rubberChooserDialog.show();
                ////////////////////////////
                break;
            case R.id.pick_img:
                //File _sdCard = Environment.getExternalStorageDirectory();
                //File _picDir = new File(_sdCard, Utility.dirName);
                //if (_picDir.exists() && _picDir.listFiles().length > 0) {
                Intent intent = new Intent();
                intent.setClassName(Utility.packageName,
                        Utility.classGalleryName);

                startActivityForResult(intent, this.PICK_IMAGE_REQUEST);

                //}
                /*else
                {
                    Toast.makeText(this, R.string.toast_no_pic,
                            Toast.LENGTH_LONG).show();
                }*/
                break;
            case R.id.pad_new:
                //this.doodleView.newCanvas();
                showDoodleSaveAndNewPage();

                break;
            case R.id.play_draw: {
                playDoodle(true);
            }
            break;

            /*case R.id.device_image: {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image*//*");
                startActivityForResult(photoPickerIntent, SELECT_DEVICE_PHOTO);
                //doodleView.undo();
            }
            break;
*/
            case R.id.can_undo:
                if(doodleView.isInPlayMode()) {
                    toastOpNotAllowed();
                    break;
                }
                doodleView.undo();
                break;
        }
    }


    void playDoodle(boolean allowPause) {
        this.doodleView.playDoodle();
        //if (!allowPause) Allowing Pause always
        //    playDraw.setEnabled(false);
        makePlayBtn();
    }

    void makePlayBtn() {
        if (!doodleView.playMode) {
            playDraw.setImageDrawable(getResources().getDrawable(R.drawable.play_doodle));
        } else {
            playDraw.setImageDrawable(getResources().getDrawable(R.drawable.pause_doodle));
        }
    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.PICK_IMAGE_REQUEST) {
            isCurModeChl = Utility.getChlidMode(getApplicationContext());
            applyChildMode();
            if ((resultCode == Utility.RESULT_OK) && (data != null) && (data.getData() != null)) {
                Uri uri = data.getData();
                loadDoodleWithUrl(uri, data.getStringExtra(Utility.doodlePlayFilename));
            }
        }
        if(requestCode == SELECT_DEVICE_PHOTO){
            if(resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    if(yourSelectedImage != null) {
                        doodleView.newCanvas();
                        doodleView.setBackBitmap(yourSelectedImage);
                        doodleView.invalidate();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onPlayComplete() {
        playDraw.setImageDrawable(getResources().getDrawable(R.drawable.play_doodle));
        playDraw.setEnabled(true);
        //doodleView.createDrawcanvasFromPlaycanvas();
        //////// On Finish we can draw just by JSON , Loading Bitmap Not needed
        /*if (tmpPlayForReload != null && saveOnFinsh) {
            loadDoodleWithBitmap(doodleView.getPlayBitmap(), tmpPlayForReload);
            tmpPlayForReload = null;
            saveOnFinsh = false;
        }*/
    }



    //////// we can draw just by JSON
    //PlayImageArray tmpPlayForReload;
    //boolean saveOnFinsh = false;

    void loadJSONDoodle(String json) {
        PlayImageArray playArr = PlayImageArray.createPlayImage(json , this ,
                /*getWindow().getDecorView().getWidth(), getWindow().getDecorView().getHeight()*/
                doodleView.getWidth(), doodleView.getHeight());
        loadDoodleWithBitmap(null , playArr);
    }


    void loadJSONDoodle(int jsonRes) {
        PlayImageArray playArr = getPlayObjFromFile(jsonRes);
        loadDoodleWithBitmap(null , playArr);
    }

    PlayImageArray getPlayObjFromFile(int fileRes) {
        StringBuilder jsonStr = new StringBuilder();
        try {
            InputStream rawFile = getResources().openRawResource(fileRes);
            InputStreamReader fileReader = new InputStreamReader(rawFile);
            BufferedReader bufReader = new BufferedReader(fileReader);

            String line = bufReader.readLine();
            while(line != null) {
                jsonStr.append(line);
                line = bufReader.readLine();
            }
            bufReader.close();
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return PlayImageArray.createPlayImage(jsonStr.toString() , this ,
                /*getWindow().getDecorView().getWidth(), getWindow().getDecorView().getHeight()*/
                doodleView.getWidth(), doodleView.getHeight());
    }

    void loadDoodleWithUrl(Uri loadUrl, String playImageUrl) {
        Log.v("TET" , "********** loadDoodleWithUrl");

        Bitmap bitmap = null;
        try {
            if (loadUrl != null)
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), loadUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadDoodleWithBitmap(bitmap, playImageUrl);
        Log.v("TET" , "********** loadDoodleWithUrl frm");

    }

    void loadDoodleWithBitmap(Bitmap loadBitmap, String playImageUrl) {
        PlayImageArray playArr = doodleView.getPlayObjFromFile(playImageUrl);
        loadDoodleWithBitmap(loadBitmap , playArr);
    }

    void loadDoodleWithBitmap(Bitmap loadBitmap, PlayImageArray playImageArr) {
        loadDoodleWithBitmap(loadBitmap , playImageArr, true, false);
    }

    void loadDoodleWithBitmap(Bitmap loadBitmap, PlayImageArray playImageArr,
                              boolean saveWhenFinish, boolean pauseAlow) {
        //doodleView.newCanvas();
        if (loadBitmap != null) this.doodleView.setImage(loadBitmap);
        if (playImageArr != null)
            doodleView.setPlayImageArray(
                    playImageArr);


        if (loadBitmap == null) {
            //////// If Bitmap is null and we have only JSON we can just draw by JSON
            if(playImageArr != null)
                doodleView.createDrawcanvasFromPlaycanvas();
            //playDoodle(false);
            /*tmpPlayForReload = playImageArr;
            //playDoodle(false);
            saveOnFinsh = saveWhenFinish;
            playDoodle(pauseAlow);*/
        }
    }


    int doodleWidthAfterLayout = -1;
    int doodleHeightAfterLayout = -1;

  /*  @Override
    protected void onStop() {
        doodleView.writeToTmp(Utility.imgTmp);
        super.onStop();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(doodleWidthAfterLayout >= doodleHeightAfterLayout)
            doodleView.writeToTmp(Utility.imgTmp , true, doodleWidthAfterLayout
                            , doodleHeightAfterLayout);*/
        if(doodleView.getHeight() >= doodleView.getWidth())
            doodleView.writeToTmp(Utility.imgTmp , true, doodleView.getWidth()
                    , doodleView.getHeight(), true);

        //Uncomment if you want ad
        //mAdView.destroy();//uncomment
        //((RelativeLayout)findViewById(R.id.main_container)).removeAllViews();
    }


    void showDoodleSaveAndNewPage() {
        if(isCurModeChl)
            doodleView.newCanvas();
        else {
            LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            new AlertDialog.Builder(this)
                    .setTitle(R.string.new_canvas_title)
                    .setMessage(R.string.new_conformation)
                    .setIcon(R.drawable.page_add)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            doodleView.newCanvas();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //doodleView.newCanvas();
                        }
                    }).show();
        }
    }

    void showFirstTimeModeDialog() {
        if (Utility.getFirstTime(this)) {
            LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.enable_chl_mode_frt_desc)
                    .setView(inflator.inflate(R.layout.first_time_mode_layout, null))
                    .setIcon(R.drawable.kid_selected)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Utility.putChlidMode(getApplicationContext(), true);
                            isCurModeChl = true;
                            applyChildMode();
                            //loadJSONDoodle(R.raw.doodle_alpha); need to fix
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //loadJSONDoodle(R.raw.doodle_general_hi); need to fix
                        }
                    }).show();

            Utility.putFirstTime(this, false);
        }
    }


}
