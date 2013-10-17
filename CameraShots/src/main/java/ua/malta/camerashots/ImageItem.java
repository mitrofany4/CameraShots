package ua.malta.camerashots;

import java.io.File;

/**
 * Created by mitrofany4 on 17/10/13.
 */
public class ImageItem {
    String image;
    String imageTitle;

    public ImageItem() {
    }

    public ImageItem(String uri) {
        File f= new File(String.valueOf(uri));
        this.image = f.getPath();
        this.imageTitle = f.getName();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }
}
