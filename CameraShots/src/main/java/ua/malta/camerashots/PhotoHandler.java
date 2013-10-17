package ua.malta.camerashots;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mitrofany4 on 17/10/13.
 */

public class PhotoHandler implements Camera.PictureCallback {

        private final Context context;

        public PhotoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

           File pictureFileDir = getDir();
//            pictureFileDir.mkdirs();

            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + MainActivity.APP_PATH_SD_CARD;
            try {
                pictureFileDir = new File(fullPath);
                if (!pictureFileDir.exists()) {
                    pictureFileDir.mkdirs();
                }
            }
            catch(Exception e){
                Log.w("creating file error", e.toString());
            }

            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

                Log.d(MainActivity.DEBUG_TAG, "Can't create directory to save image.");
                Toast.makeText(context, "Can't create directory to save image.",
                        Toast.LENGTH_LONG).show();
                return;

            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_SSS");
            String date = dateFormat.format(new Date());
            String photoFile = date + ".jpg";

            String filename = pictureFileDir.getPath() + File.separator + photoFile;

            File pictureFile = new File(filename);

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Toast.makeText(context, "New Image saved:" + photoFile,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception error) {
                Log.d(MainActivity.DEBUG_TAG, "File" + filename + "not saved: "
                        + error.getMessage());
                Toast.makeText(context, "Image could not be saved.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        private File getDir() {
            File sdDir = Environment
                    .getExternalStorageDirectory();
            return new File(sdDir, "CameraPhotos");
        }

    }
