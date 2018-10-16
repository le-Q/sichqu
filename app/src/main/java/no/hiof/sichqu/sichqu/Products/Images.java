package no.hiof.sichqu.sichqu.Products;

public class Images
{
    private Thumbnail thumbnail;

    private Large large;

    public Thumbnail getThumbnail ()
    {
        return thumbnail;
    }

    public void setThumbnail (Thumbnail thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public Large getLarge ()
    {
        return large;
    }

    public void setLarge (Large large)
    {
        this.large = large;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [thumbnail = "+thumbnail+", large = "+large+"]";
    }
}