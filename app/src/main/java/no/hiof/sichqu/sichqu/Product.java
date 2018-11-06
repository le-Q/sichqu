package no.hiof.sichqu.sichqu;

import no.hiof.sichqu.sichqu.Products.Products;

public class Product extends Products {

    private String id, title;

    public Product() {
        
    }

    public Product(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Product(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
