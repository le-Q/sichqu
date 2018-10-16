package no.hiof.sichqu.sichqu.Products;

public class Availability
{
    private String description_short;

    private String is_available;

    private String description;

    private String code;

    public String getDescription_short ()
    {
        return description_short;
    }

    public void setDescription_short (String description_short)
    {
        this.description_short = description_short;
    }

    public String getIs_available ()
    {
        return is_available;
    }

    public void setIs_available (String is_available)
    {
        this.is_available = is_available;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [description_short = "+description_short+", is_available = "+is_available+", description = "+description+", code = "+code+"]";
    }
}
