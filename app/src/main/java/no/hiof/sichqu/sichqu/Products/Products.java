package no.hiof.sichqu.sichqu.Products;


import com.google.firebase.database.Exclude;

public class Products {
    private String id;
    private String name;
    private Images[] images;
    private String thumbnail;



    public Products(){
    }

    public Products(String name) {
        this.name = name;
    }

    public Products(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Exclude
    public Images[] getImages ()
    {
        return images;
    }

    public void setImages (Images[] images)
    {
        this.images = images;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
