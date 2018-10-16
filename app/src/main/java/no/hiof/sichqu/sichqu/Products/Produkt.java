package no.hiof.sichqu.sichqu.Products;

public class Produkt {
    private Alternative_products[] alternative_products;

    private String gross_unit_price;

    private String[] client_classifiers;

    private String unit_price_quantity_name;

    private String brand_id;

    private String unit_price_quantity_abbreviation;

    private String contents_html;

    private String name_extra;

    private String id;

    private String front_url;

    private String gross_price;

    private String nutrition_html;

    private String name;

    private String brand;

    private Images[] images;

    private Categories[] categories;

    private Category_items[] category_items;

    private Availability availability;

    private String full_name;

    public Alternative_products[] getAlternative_products ()
    {
        return alternative_products;
    }

    public void setAlternative_products (Alternative_products[] alternative_products)
    {
        this.alternative_products = alternative_products;
    }

    public String getGross_unit_price ()
    {
        return gross_unit_price;
    }

    public void setGross_unit_price (String gross_unit_price)
    {
        this.gross_unit_price = gross_unit_price;
    }

    public String[] getClient_classifiers ()
    {
        return client_classifiers;
    }

    public void setClient_classifiers (String[] client_classifiers)
    {
        this.client_classifiers = client_classifiers;
    }

    public String getUnit_price_quantity_name ()
    {
        return unit_price_quantity_name;
    }

    public void setUnit_price_quantity_name (String unit_price_quantity_name)
    {
        this.unit_price_quantity_name = unit_price_quantity_name;
    }

    public String getBrand_id ()
    {
        return brand_id;
    }

    public void setBrand_id (String brand_id)
    {
        this.brand_id = brand_id;
    }

    public String getUnit_price_quantity_abbreviation ()
    {
        return unit_price_quantity_abbreviation;
    }

    public void setUnit_price_quantity_abbreviation (String unit_price_quantity_abbreviation)
    {
        this.unit_price_quantity_abbreviation = unit_price_quantity_abbreviation;
    }

    public String getContents_html ()
    {
        return contents_html;
    }

    public void setContents_html (String contents_html)
    {
        this.contents_html = contents_html;
    }

    public String getName_extra ()
    {
        return name_extra;
    }

    public void setName_extra (String name_extra)
    {
        this.name_extra = name_extra;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getFront_url ()
    {
        return front_url;
    }

    public void setFront_url (String front_url)
    {
        this.front_url = front_url;
    }

    public String getGross_price ()
    {
        return gross_price;
    }

    public void setGross_price (String gross_price)
    {
        this.gross_price = gross_price;
    }

    public String getNutrition_html ()
    {
        return nutrition_html;
    }

    public void setNutrition_html (String nutrition_html)
    {
        this.nutrition_html = nutrition_html;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getBrand ()
    {
        return brand;
    }

    public void setBrand (String brand)
    {
        this.brand = brand;
    }

    public Images[] getImages ()
    {
        return images;
    }

    public void setImages (Images[] images)
    {
        this.images = images;
    }

    public Categories[] getCategories ()
    {
        return categories;
    }

    public void setCategories (Categories[] categories)
    {
        this.categories = categories;
    }

    public Category_items[] getCategory_items ()
    {
        return category_items;
    }

    public void setCategory_items (Category_items[] category_items)
    {
        this.category_items = category_items;
    }

    public Availability getAvailability ()
    {
        return availability;
    }

    public void setAvailability (Availability availability)
    {
        this.availability = availability;
    }

    public String getFull_name ()
    {
        return full_name;
    }

    public void setFull_name (String full_name)
    {
        this.full_name = full_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [alternative_products = "+alternative_products+", gross_unit_price = "+gross_unit_price+", client_classifiers = "+client_classifiers+", unit_price_quantity_name = "+unit_price_quantity_name+", brand_id = "+brand_id+", unit_price_quantity_abbreviation = "+unit_price_quantity_abbreviation+", contents_html = "+contents_html+", name_extra = "+name_extra+", id = "+id+", front_url = "+front_url+", gross_price = "+gross_price+", nutrition_html = "+nutrition_html+", name = "+name+", brand = "+brand+", images = "+images+", categories = "+categories+", category_items = "+category_items+", availability = "+availability+", full_name = "+full_name+"]";
    }
}