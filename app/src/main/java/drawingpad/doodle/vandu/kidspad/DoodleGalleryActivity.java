
package drawingpad.doodle.vandu.kidspad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by vandu on 10-05-2015.
 */
public class DoodleGalleryActivity
        extends android.support.v7.app.AppCompatActivity
        implements AdapterView.OnItemClickListener
{
    DoodleGalleryAdapter doodleAdpater;
    ArrayList<Integer> selectedPos = new ArrayList<Integer>();
    enum GalleryState {
        DELETE,
        SHARE,
        NONE
    }

    private boolean isCurModeChl = false;
    private GalleryState curState = GalleryState.NONE;

    protected Object mActionMode;
    GridView gallery;
    boolean noGallery = false;

    AdView mAdView;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.doodle_gallery_view);
        gallery = (GridView)findViewById(R.id.gallery_view);

        mAdView = new AdView(getApplicationContext());//(AdView) findViewById(R.id.adView);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        RelativeLayout.LayoutParams ly = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        ly.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ly.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ((RelativeLayout)findViewById(R.id.galry_cont)).addView(mAdView,ly);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        this.doodleAdpater = new DoodleGalleryAdapter(this);
        gallery.setAdapter(this.doodleAdpater);
        int imgCount = doodleAdpater.getCount();
        if(imgCount <= 0) {
            gallery.setVisibility(View.GONE);

            TextView tw = new TextView(this);
            tw.setTextSize(getResources().getDimension(R.dimen.no_gallery_text_size));
            tw.setGravity(Gravity.CENTER);
            tw.setText(R.string.toast_no_pic);
            setContentView(tw);

            noGallery = true;
        }
        gallery.setOnItemClickListener(this);

    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        switch(curState) {
            case NONE:
                File file = this.doodleAdpater.getPath(position);
                Uri uri = Uri.fromFile(file);

                Intent intent = new Intent();
                intent.setData(uri);

                String playFileName = file.getAbsolutePath().substring(0,
                        file.getAbsolutePath().lastIndexOf(".")) + Utility.plyTyp;
                intent.putExtra(Utility.doodlePlayFilename, playFileName);
                setResult(Utility.RESULT_OK, intent);
                finish();
            break;

            case DELETE:
            case SHARE:
                selectedPos.add(position);
               break;

        }
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.gallery_context_menu, menu);
        return true;
    }

    Menu optionMenu;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        optionMenu = menu;
        updateMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    void updateMenu()
    {
        if(optionMenu == null)
            return;
        if(isCurModeChl) {
            optionMenu.findItem(R.id.delete_menu).setVisible(false);
            optionMenu.findItem(R.id.share_menu).setVisible(false);
            optionMenu.findItem(R.id.select_all_menu).setVisible(false);
            optionMenu.findItem(R.id.share_menu).setEnabled(false);
            optionMenu.findItem(R.id.delete_menu).setEnabled(false);
            optionMenu.findItem(R.id.select_all_menu).setEnabled(false);

            //optionMenu.findItem(R.id.mode_toggel_menu).setIcon(R.drawable.kid_selected);
        }
        else {
            optionMenu.findItem(R.id.delete_menu).setVisible(true);
            optionMenu.findItem(R.id.select_all_menu).setVisible(true);
            optionMenu.findItem(R.id.share_menu).setVisible(true);
            optionMenu.findItem(R.id.share_menu).setEnabled(true);
            optionMenu.findItem(R.id.delete_menu).setEnabled(true);
            optionMenu.findItem(R.id.select_all_menu).setEnabled(true);

            //optionMenu.findItem(R.id.mode_toggel_menu).setIcon(R.drawable.kid_unselected);
            if (doodleAdpater.chkSelList.size() > 0)
                optionMenu.findItem(R.id.select_all_menu).setTitle(R.string.action_unselect_all);
            else
                optionMenu.findItem(R.id.select_all_menu).setTitle(R.string.action_select_all);
        }
    }

    ArrayList<File> getSelectedFilesList()
    {
        ArrayList<File> selectedFiles = new ArrayList<File>();
        Iterator trv = doodleAdpater.chkSelList.iterator();
        while(trv.hasNext())
        {
            Integer delPos = (Integer)trv.next();
            File selFile = doodleAdpater.getPath(delPos);
            selectedFiles.add(selFile);

        }
        return selectedFiles;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.share_menu) {
            if(noGallery)
                return true;

            if(getSelectedFilesList().size() <= 0) {
                toastNoSelected();
                return true;
            }
            ArrayList<Uri> filesToShare = new ArrayList<Uri>();
            Iterator trv = doodleAdpater.chkSelList.iterator();
            while(trv.hasNext())
            {
                Integer delPos = (Integer)trv.next();
                File fileToShare = doodleAdpater.getPath(delPos);
                filesToShare.add(Uri.fromFile(fileToShare));

            }


            Intent shareIntent = new Intent();

            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.setType("image/jpg");
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filesToShare);

            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_using)));

            updateMenu();
            return true;
        }
        else if(id == R.id.delete_menu)
        {
            if(noGallery)
                return true;

            if(getSelectedFilesList().size() <= 0) {
                toastNoSelected();
                return true;
            }

            showConfirmatonDialog(getString(R.string.action_delete) ,
                    getString(R.string.del_conformation) ,
                    android.R.drawable.ic_menu_delete, DEL_CMD , null);
            return true;
        }
        else if(id == R.id.select_all_menu)
        {
            if(noGallery)
                return true;

            if(doodleAdpater.chkSelList.size() > 0) {
                item.setTitle(R.string.action_select_all);
                doodleAdpater.chkSelList.clear();
                doodleAdpater.notifyDataSetChanged();
                gallery.invalidate();
            }
            else {
                item.setTitle(R.string.action_unselect_all);
                int totFiles = doodleAdpater.files.size();
                for(int cnt = 0; cnt < totFiles; cnt++)
                {
                    doodleAdpater.chkSelList.add(cnt);
                }
                doodleAdpater.notifyDataSetChanged();
                gallery.invalidate();

            }return true;
        }
        /*else if(id == R.id.mode_toggel_menu) {
            changeMode();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    void changeMode() {
        isCurModeChl = Utility.getChlidMode(this);
        LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        if(!isCurModeChl) {
            showConfirmatonDialog(getString(R.string.enable_chl_mode) ,
                    getString(R.string.enable_chl_mode_desc) ,
                    R.drawable.kid_selected, CHANGE_TO_CHL_MODE_CMD ,
                    inflator.inflate(R.layout.enable_child_layout , null));

        } else {

            showConfirmatonDialog(getString(R.string.disable_chl_mode) ,
                    getString(R.string.enter_number) ,
                    R.drawable.kid_unselected,
                    CHANGE_FROM_CHL_MODE_CMD ,
                    inflator.inflate(R.layout.mode_toggle_layout , null));
        }

    }


    void toggleMode() {
        isCurModeChl = Utility.getChlidMode(getApplicationContext());
        Utility.putChlidMode(getApplicationContext(), !isCurModeChl);
        isCurModeChl = !isCurModeChl;
        doodleAdpater.setChlMode(isCurModeChl);
    }

    void deleteCmd() {
        ArrayList<File> filesToDel = getSelectedFilesList();
        deleteFilesAdPlayFiles(filesToDel);
    }

    void toastNoSelected() {
        Toast.makeText(this, R.string.toast_no_sel , Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        isCurModeChl = Utility.getChlidMode(getApplicationContext());
        doodleAdpater.setChlMode(isCurModeChl);
        super.onResume();

        mAdView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mAdView.pause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }

    void deleteFilesAdPlayFiles(ArrayList<File> fileList)
    {
        for(int cnt = 0; cnt < fileList.size(); cnt++)
        {
            File fileToDel = fileList.get(cnt);
            String playFileName = fileToDel.getAbsolutePath().substring(0,
                    fileToDel.getAbsolutePath().lastIndexOf(".")) + Utility.plyTyp;

            File playFileToDel = new File(playFileName);

            if(fileToDel.exists())
                fileToDel.delete();
            if(playFileToDel.exists())
                playFileToDel.delete();

            doodleAdpater.notifyDataSetChanged();
        }
        doodleAdpater.chkSelList.clear();


        int imgCount = doodleAdpater.getCount();
        if(imgCount <= 0) {
            gallery.setVisibility(View.GONE);

            TextView tw = new TextView(this);
            tw.setTextSize(getResources().getDimension(R.dimen.no_gallery_text_size));
            tw.setGravity(Gravity.CENTER);
            tw.setText(R.string.toast_no_pic);
            setContentView(tw);
            noGallery = true;
        }
        updateMenu();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    private  final int DEL_CMD = 5;
    private  final int CHANGE_TO_CHL_MODE_CMD = 7;
    private  final int CHANGE_FROM_CHL_MODE_CMD = 8;
    String genNum = "";
    View tmpView;
    void showConfirmatonDialog(String title,
                               String msg , int iconRes, final int curCmd , View vw) {
        tmpView = vw;
        if(curCmd == CHANGE_FROM_CHL_MODE_CMD) {
            genNum = Utility.generateString();
            ((TextView) vw.findViewById(R.id.corect_text)).setText(
                    Utility.digToNum(this ,genNum));
        }
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setView(vw)
                .setIcon(iconRes)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (curCmd == DEL_CMD)
                            deleteCmd();
                        else if (curCmd == CHANGE_TO_CHL_MODE_CMD) {
                            toggleMode();
                            updateMenu();
                        } else if (curCmd == CHANGE_FROM_CHL_MODE_CMD) {
                            EditText edit = (EditText) tmpView.findViewById(R.id.lock_edit);
                            String enterText = edit.getText().toString().trim();
                            if (genNum.trim().equals(enterText)) {
                                toggleMode();
                                updateMenu();
                            } else {
                                Toast.makeText(DoodleGalleryActivity.this, R.string.toast_wrong_input,
                                        Toast.LENGTH_LONG).show();
                            }

                        }


                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


}

