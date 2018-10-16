package no.hiof.sichqu.sichqu.Products;

public class Category_items
{
    private String id;

    private String ordering;

    private String product_category_id;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getOrdering ()
    {
        return ordering;
    }

    public void setOrdering (String ordering)
    {
        this.ordering = ordering;
    }

    public String getProduct_category_id ()
    {
        return product_category_id;
    }

    public void setProduct_category_id (String product_category_id)
    {
        this.product_category_id = product_category_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", ordering = "+ordering+", product_category_id = "+product_category_id+"]";
    }
}