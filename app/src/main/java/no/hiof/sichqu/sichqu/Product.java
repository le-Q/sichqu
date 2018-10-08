package no.hiof.sichqu.sichqu;

import android.widget.ImageView;

public class Product {

    private int id;
    private String title, shortDesc;
    private ImageView image;

    public Product(int id, String title, String shortDesc, ImageView image) {
        this.id = id;
        this.title = title;
        this.shortDesc = shortDesc;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
