package ua.malta.camerashots;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitrofany4 on 17/10/13.
 */
public class GridViewAdapter extends ArrayAdapter<ImageItem> {
    private Context context;
    private int layoutResourceId;
    private List<ImageItem> data = new ArrayList<ImageItem>();

    public GridViewAdapter(Context context, int resource, List<ImageItem> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = objects;
    }


    static class ViewHolder{
        ImageView image;
        TextView title;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ImageItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.title = (TextView) row.findViewById(R.id.title);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        String path = data.get(position).getImage();
        String name = data.get(position).getImageTitle();
        Bitmap bmp = BitmapFactory.decodeFile(path);
        holder.image.setImageBitmap(bmp);
        holder.title.setText(name);
        return row;
    }
}
