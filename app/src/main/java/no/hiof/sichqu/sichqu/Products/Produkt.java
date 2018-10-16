package no.hiof.sichqu.sichqu.Products;

public class Produkt
{
    private String[] categories;

    private Products[] products;

    public String[] getCategories ()
    {
        return categories;
    }

    public void setCategories (String[] categories)
    {
        this.categories = categories;
    }

    public Products[] getProducts ()
    {
        return products;
    }

    public void setProducts (Products[] products)
    {
        this.products = products;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [categories = "+categories+", products = "+products+"]";
    }
}