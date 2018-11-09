package no.hiof.sichqu.sichqu.Products;

public class Images
{
    private Thumbnail thumbnail;

    private Large large;

    public Images(Thumbnail thumbnail){
        this.thumbnail = thumbnail;
    }
    public Thumbnail getThumbnail ()
    {
        return thumbnail;
    }

    public void setThumbnail (Thumbnail thumbnail)
    {
        this.thumbnail = thumbnail;
    }


    @Override
    public String toString()
    {
        return thumbnail.getUrl();
    }
}