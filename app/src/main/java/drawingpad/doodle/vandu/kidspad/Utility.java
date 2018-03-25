package drawingpad.doodle.vandu.kidspad;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;

import java.io.File;
import java.util.Random;

/**
 * Created by vandu on 10-05-2015.
 */

public class Utility
{
    static final String dirName = "doodle.vandu.drawingpadportrait";
    static final String imgTmp = "doodle_tmp";
    static final String imgTmpShare = "doodle_public";
    static final String imgPrx = "doodle_";
    static final String imgTyp = ".jpg";
    //static final String plyTyp = ".myv";

    static final String plyTyp = ".json";
    static final String doodleTyp = ".dood";
    static final String packageName = "drawingpad.doodle.vandu.kidspad";
    static final String classGalleryName = "drawingpad.doodle.vandu.kidspad.DoodleGalleryActivity";

    static final String locDirName = "myimages";
    static final String fbShareUrl = "content://drawingpad.doodle.vandu.doodlepad/"+locDirName+"/"+imgTmp+imgTyp;

    static final String doodlePlayFilename = "doodlePlayFilename";

    static final String perfName = "ModePref";
    static final String chlModeKey = "ChildMode";
    static final String firstTime = "firstTime";

    static final int RESULT_OK = 1;
    static final int RESULT_FAIL = -1;

    static final int itemCount = 7;

    static public int dpToPx(Context context , int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }



    static public File getDoodleDir() {
        File _sdCard = Environment.getExternalStorageDirectory();
        File _picDir = new File(_sdCard, Utility.dirName);
        _picDir.mkdirs();
        return _picDir;
    }

    static public File getLocalFileDir(Context ctx) {
        File _localFile = ctx.getFilesDir();
        File _picDir = new File(_localFile, Utility.locDirName);
        _picDir.mkdirs();
        return _picDir;
    }

    static public Uri getFileLocalUri(Context ctx) {
        //File imagePath = new File(ctx.getFilesDir(), Utility.locDirName);
        File newFile = new File(getLocalFileDir(ctx), imgTmp+imgTyp);
        Uri contentUri = FileProvider.getUriForFile(ctx, "drawingpad.doodle.vandu.doodlepad", newFile);
        return contentUri;
    }

    static public boolean ifImageProper(String imgPath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imgPath,opt);
        if(opt.outWidth != -1 && opt.outHeight != -1) {
            return true;
        }
        return false;

    }

    static public boolean getChlidMode(Context context) {
        return false;
        //SharedPreferences pref = context.getSharedPreferences(perfName , 0);
        //return pref.getBoolean(chlModeKey , false);
    }

    static public void putChlidMode(Context context , boolean val) {
        SharedPreferences pref = context.getSharedPreferences(perfName , Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(chlModeKey, val);
        editor.commit();
    }

    static public boolean getFirstTime(Context context) {
        SharedPreferences pref = context.getSharedPreferences(perfName , 0);
        return pref.getBoolean(firstTime , true);
    }

    static public void putFirstTime(Context context , boolean val) {
        SharedPreferences pref = context.getSharedPreferences(perfName , Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(firstTime, val);
        editor.commit();
    }



    static public String generateString(){
        String ret = "";
        Random ran = new Random();
        for(int cnt = 0; cnt <= 3; cnt++)
        ret += (ran.nextInt(9) + 1);

        return ret;
    }




    static public String digToNum(Context context , String num) {
        String ret = "";
        String arr[] = {context.getString(R.string.zero),
                context.getString(R.string.one),
                context.getString(R.string.two),
                context.getString(R.string.three),
                context.getString(R.string.four),
                context.getString(R.string.five),
                context.getString(R.string.six),
                context.getString(R.string.seven),
                context.getString(R.string.eight),
                context.getString(R.string.nine)

        };

        String arr5[] = {context.getString(R.string.eleven),
                context.getString(R.string.twelve),
                context.getString(R.string.thirteen),
                context.getString(R.string.fourteen),
                context.getString(R.string.fifteen),
                context.getString(R.string.sixteen),
                context.getString(R.string.seventeen),
                context.getString(R.string.eighteen),
                context.getString(R.string.nineteen)
        };

        String arr7[] = {context.getString(R.string.ten),
                context.getString(R.string.twenty),
                context.getString(R.string.thirty),
                context.getString(R.string.forty),
                context.getString(R.string.fifty),
                context.getString(R.string.sixty),
                context.getString(R.string.seventy),
                context.getString(R.string.eighty),
                context.getString(R.string.ninety)
        };


        for(int cnt = 0; cnt <= 3; cnt++) {
            int curNum = num.charAt(cnt) - '0';
            if(cnt == 0) {
                ret += arr[curNum];
                ret += " ";
                ret += (curNum == 1 ? context.getString(R.string.thousand)
                                    : context.getString(R.string.thousands));
                ret += " ";
            }
            else if(cnt == 1) {
                ret += arr[curNum];
                ret += " ";
                ret += (curNum == 1 ? context.getString(R.string.hundred)
                        : context.getString(R.string.hundreds));
                ret += " ";
            }
            else if(cnt == 2) {
                if(curNum == 1) {
                    cnt++;
                    int nxtNum = num.charAt(cnt) - '0';
                    ret += arr5[nxtNum-1];
                    ret += " ";
                }
                else {
                    ret += arr7[curNum-1];
                    ret += " ";
                }

            }
            else if(cnt == 3) {
                ret += arr[curNum];
                ret += " ";
            }

        }

        return ret.trim();

    }


}
