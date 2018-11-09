package no.hiof.sichqu.sichqu.Products;

public class UPC_data
{
    private String upcnumber;

    private String type;

    private String size;

    private String unit;

    private String st0s;

    private String title;

    private String category;

    private String msrp;

    private String color;

    private String description;

    public String getUpcnumber ()
    {
        return upcnumber;
    }

    public void setUpcnumber (String upcnumber)
    {
        this.upcnumber = upcnumber;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getSize ()
    {
        return size;
    }

    public void setSize (String size)
    {
        this.size = size;
    }

    public String getUnit ()
    {
        return unit;
    }

    public void setUnit (String unit)
    {
        this.unit = unit;
    }

    public String getSt0s ()
    {
        return st0s;
    }

    public void setSt0s (String st0s)
    {
        this.st0s = st0s;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public String getMsrp ()
    {
        return msrp;
    }

    public void setMsrp (String msrp)
    {
        this.msrp = msrp;
    }

    public String getColor ()
    {
        return color;
    }

    public void setColor (String color)
    {
        this.color = color;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }


    @Override
    public String toString()
    {
        return upcnumber;
    }
}