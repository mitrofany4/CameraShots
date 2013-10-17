package ua.malta.camerashots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by mitrofany4 on 17/10/13.
 */
public class ImageAdapter extends BaseAdapter
{
    private Context mContext;
    private String[] data = {"1"};

    BitmapFactory.Options options = new BitmapFactory.Options();
    Bitmap btm;
    ImageView view;

    public ImageAdapter(Context сontext)
    {
        mContext = сontext;

        options.inSampleSize = 2;

        // получаю список фотографий в папке /DCIM/Camera на sd-карте
        File rootsd = Environment.getExternalStorageDirectory();
        File dcim = new File(rootsd.getAbsolutePath() + "/DCIM/Camera");

        File[] imagelist = dcim.listFiles(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return ((name.endsWith(".jpg"))||(name.endsWith(".png")));
            }
        });

        // сохраняю пути к фотографиям в стринг массиве
        if (imagelist != null)
        {
            data = new String[imagelist.length];

            for (int i = 0; i < imagelist.length; i++)
            {
                data[i] = imagelist[i].getAbsolutePath();
            }
        }
    }

    public int getCount()
    {
        return data.length;
    }

    public Object getItem(int position)
    {
        return null;
    }

    public long getItemId(int position)
    {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            view = new ImageView(mContext);
            view.setLayoutParams(new GridView.LayoutParams(100, 100));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else
        {
            view = (ImageView)convertView;
        }

        btm = BitmapFactory.decodeFile(data[position], options);
        view.setImageBitmap(btm);

        return view;
    }
}