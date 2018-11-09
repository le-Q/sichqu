package no.hiof.sichqu.sichqu.Products;


public class Products {
    private String id;
    private String gross_price;
    private String name;
    private Images[] images;

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

    public String getGross_price ()
    {
        return gross_price;
    }

    public void setGross_price (String gross_price)
    {
        this.gross_price = gross_price;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Images[] getImages ()
    {
        return images;
    }

    public void setImages (Images[] images)
    {
        this.images = images;
    }
}
