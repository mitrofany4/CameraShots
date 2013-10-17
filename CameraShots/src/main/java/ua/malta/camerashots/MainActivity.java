package ua.malta.camerashots;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.hardware.Camera.CameraInfo;

public class MainActivity extends Activity {

    final static String DEBUG_TAG = "MakePhotoActivity";
    public final static String APP_PATH_SD_CARD = "/CameraShots";
    private GridView gridView;
    private Button takePhotoBtn;
    private Button galleryBtn;
    private Camera camera;
    private int cameraId = 0;
    private PowerManager.WakeLock wl;
    Context context;

    GridViewAdapter adapter;

    ArrayList<String> f = new ArrayList<String>();// list of file paths
    ArrayList<ImageItem> images = new ArrayList<ImageItem>();
    File[] listFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        gridView = (GridView) findViewById(R.id.gridView);
        takePhotoBtn = (Button) findViewById(R.id.takePhotoBtn);
        galleryBtn = (Button) findViewById(R.id.picturesBtn);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(MainActivity.this, "Please mount SD card", Toast.LENGTH_LONG).show();
            takePhotoBtn.setVisibility(View.GONE);
            galleryBtn.setVisibility(View.GONE);
        }
        else {
            // do we have a camera?
            if (!getPackageManager()
                    .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                        .show();
                takePhotoBtn.setVisibility(View.GONE);
            } else {
                cameraId = findFrontFacingCamera();
                if (cameraId < 0) {
                    Toast.makeText(this, "No front facing camera found.",
                            Toast.LENGTH_LONG).show();
                    takePhotoBtn.setVisibility(View.GONE);
                } else {
                    camera = Camera.open(cameraId);
                    SurfaceView dummy=new SurfaceView(context);
                    try {
                        camera.setPreviewDisplay(dummy.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    camera.startPreview();
                }
            }
        }

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CountDownTimer timer;
                Toast.makeText(MainActivity.this, "Start taking photos", Toast.LENGTH_LONG)
                        .show();
                timer = new CountDownTimer(10000,500) {
                    @Override
                    public void onTick(long l) {
                        if (camera!=null){

                            camera.takePicture(null, null,
                                    new PhotoHandler(context));
                        }
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this, "Photos have been taken", Toast.LENGTH_LONG)
                                .show();
                    }
                };

                timer.start();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFromSdcard();
                adapter = new GridViewAdapter(MainActivity.this, R.layout.row_image,images);
                gridView.setAdapter(adapter);
            }
        });

    }


    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(DEBUG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    public void getFromSdcard()
    {
        File file= new File(android.os.Environment.getExternalStorageDirectory(),APP_PATH_SD_CARD);

        if (file.isDirectory())
        {
            listFile = file.listFiles();


            for (int i = 0; i < listFile.length; i++)
            {

                f.add(listFile[i].getAbsolutePath());
                images.add(new ImageItem(listFile[i].getAbsolutePath()));

            }
        }
    }


}
