package no.hiof.sichqu.sichqu.Products;

public class Categories
{
    private String id;

    private String is_new;

    private String[] campaign_banners;

    private String description;

    private String name;

    private String[] children;

    private String ordering;

    private String parent;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getIs_new ()
    {
        return is_new;
    }

    public void setIs_new (String is_new)
    {
        this.is_new = is_new;
    }

    public String[] getCampaign_banners ()
    {
        return campaign_banners;
    }

    public void setCampaign_banners (String[] campaign_banners)
    {
        this.campaign_banners = campaign_banners;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String[] getChildren ()
    {
        return children;
    }

    public void setChildren (String[] children)
    {
        this.children = children;
    }

    public String getOrdering ()
    {
        return ordering;
    }

    public void setOrdering (String ordering)
    {
        this.ordering = ordering;
    }

    public String getParent ()
    {
        return parent;
    }

    public void setParent (String parent)
    {
        this.parent = parent;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", is_new = "+is_new+", campaign_banners = "+campaign_banners+", description = "+description+", name = "+name+", children = "+children+", ordering = "+ordering+", parent = "+parent+"]";
    }
}