package drawingpad.doodle.vandu.kidspad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Created by vandu on 10-05-2015.
 */
public class DoodleGalleryAdapter
        extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    Context mContext;
    ArrayList<File> files;

    private boolean isCurModeChl = false;

    DoodleGalleryAdapter(Context conetxt)
    {
        this.mContext = conetxt;
        init();
    }

    void setChlMode(boolean curMode) {
        isCurModeChl = curMode;
        notifyDataSetChanged();
    }

    private void collectFile(File _picDir) {
        if (_picDir.exists()) {
            File[] tmpFileList = _picDir.listFiles();
            for(int cnt = 0; cnt < tmpFileList.length; cnt++) {
                String fileNm = tmpFileList[cnt].getPath();
                if(fileNm.equals(Utility.imgTmp+Utility.imgTyp))
                    continue;

                String ext = fileNm.substring(fileNm.lastIndexOf("."));
                if(ext.equals(Utility.imgTyp)) {
                    if(!Utility.ifImageProper(fileNm)) {
                        File fileToDel = tmpFileList[cnt];
                        String playFileName = fileToDel.getAbsolutePath().substring(0,
                                fileToDel.getAbsolutePath().lastIndexOf(".")) + Utility.plyTyp;

                        File playFileToDel = new File(playFileName);

                        if (fileToDel.exists())
                            fileToDel.delete();
                        if (playFileToDel.exists())
                            playFileToDel.delete();
                    }
                    else
                        files.add(tmpFileList[cnt]);
                }
            }
        }
    }

    private void init()
    {
        files = new ArrayList<File>();
        //File _sdCard = Environment.getExternalStorageDirectory();
        //File _picDir = new File(_sdCard, Utility.dirName);
        collectFile(Utility.getDoodleDir());
        collectFile(Utility.getLocalFileDir(mContext));
        Collections.sort(files , new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
               return lhs.lastModified() - rhs.lastModified() > 0
                      ? -1 : 1;
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        init();
        super.notifyDataSetChanged();
    }

    public int getCount()
    {
        return this.files != null ? this.files.size() : 0;
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
        FrameLayout frame;
        ImageView imageView;
        CheckBox radio;
        if (convertView == null)
        {
            frame = new FrameLayout(mContext);
            /////////////////////////////////Need to check
            frame.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            ///////////////////////////////////////
            imageView = new ImageView(this.mContext);
            radio = new CheckBox(mContext);
            radio.setBackgroundColor(Color.WHITE);
            radio.setOnCheckedChangeListener(this);
            radio.setTag(position);
            radio.setChecked(chkSelList.contains(position));
            if(isCurModeChl)
                radio.setVisibility(View.GONE);
            else
                radio.setVisibility(View.VISIBLE);

            //radio.setPadding(0, 0, 0, 0);
            //radio.setBackgroundColor(mContext.getResources().getColor(
              //      android.R.color.transparent));
            int chkBoxVal = (int)this.mContext.getResources().getDimension(R.dimen.chk_box_size);
            radio.setLayoutParams(new GridView.LayoutParams(chkBoxVal , chkBoxVal));
                    //ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(this.files.get(position).getPath(), options);
            /*if(bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.doodle_gallery);
                File fileToDel = files.get(position);
                String playFileName = fileToDel.getAbsolutePath().substring(0,
                        fileToDel.getAbsolutePath().lastIndexOf(".")) + Utility.plyTyp;

                File playFileToDel = new File(playFileName);

                if(fileToDel.exists())
                    fileToDel.delete();
                if(playFileToDel.exists())
                    playFileToDel.delete();

                notifyDataSetChanged();

            }*/

            int valW = mContext.getResources().getDisplayMetrics().widthPixels >
                    mContext.getResources().getDisplayMetrics().heightPixels ?
                    mContext.getResources().getDisplayMetrics().heightPixels:
                    mContext.getResources().getDisplayMetrics().widthPixels;//(int)this.mContext.getResources().getDimension(R.dimen.gallery_box_size);
            valW /= 3;
            valW -= 10;
            int valH;
            if(bitmap.getHeight() < bitmap.getWidth())
                valH = (int)(valW * ((float)bitmap.getWidth()/(float)bitmap.getHeight()));
            else
                valH = (int)(valW * ((float)bitmap.getHeight()/(float)bitmap.getWidth()));

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, valW, valH, false);
            imageView.setLayoutParams(new GridView.LayoutParams(valW, valH));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);

            imageView.setImageBitmap(resizedBitmap);
            //bitmap.recycle();
            frame.addView(imageView);
            frame.addView(radio);

        }
        else
        {
            frame = (FrameLayout)convertView;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(this.files.get(position).getPath(), options);
            /*if(bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.doodle_gallery);
                File fileToDel = files.get(position);
                String playFileName = fileToDel.getAbsolutePath().substring(0,
                        fileToDel.getAbsolutePath().lastIndexOf(".")) + Utility.plyTyp;

                File playFileToDel = new File(playFileName);

                if(fileToDel.exists())
                    fileToDel.delete();
                if(playFileToDel.exists())
                    playFileToDel.delete();

                notifyDataSetChanged();


            }*/
            int valW = mContext.getResources().getDisplayMetrics().widthPixels >
            mContext.getResources().getDisplayMetrics().heightPixels ?
            mContext.getResources().getDisplayMetrics().widthPixels:
            mContext.getResources().getDisplayMetrics().heightPixels;//(int)this.mContext.getResources().getDimension(R.dimen.gallery_box_size);
            valW /= 3;
            valW -= 10;
            int valH = (int)(valW * ((float)bitmap.getHeight()/(float)bitmap.getWidth()));
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, valW, valH, false);
            ImageView img = (ImageView)frame.getChildAt(0);
            img.setImageBitmap(resizedBitmap);
            //bitmap.recycle();


            CheckBox chkBox = (CheckBox)frame.getChildAt(1);
            chkBox.setTag(position);
            chkBox.setChecked(chkSelList.contains(position));
            if(isCurModeChl)
                chkBox.setVisibility(View.GONE);
            else
                chkBox.setVisibility(View.VISIBLE);
        }
        return frame;
    }

    File getPath(int pos)
    {
        return this.files.get(pos);
    }

    HashSet<Integer> chkSelList = new HashSet<Integer>();

    Object nulObj;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Integer num = (int)buttonView.getTag();
        if(isChecked)
            chkSelList.add(num);
        else {
            chkSelList.remove(num);
        }
        ((DoodleGalleryActivity)mContext).updateMenu();
    }
}

