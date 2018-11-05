package no.hiof.sichqu.sichqu;

import no.hiof.sichqu.sichqu.Products.Products;

public class Product extends Products {

    private String id, title, shortDesc;
    private int price;
    private int bilde;

    public Product(String title) {
        this.title = title;
    }

    public Product(String id, String title, int bilde) {
        this.id = id;
        this.title = title;
        this.bilde = bilde;
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

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBilde() {
        return bilde;
    }

    public void setBilde(int bilde) {
        this.bilde = bilde;
    }
}
