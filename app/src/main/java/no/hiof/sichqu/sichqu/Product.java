package no.hiof.sichqu.sichqu;

public class Product {

    private int id;
    private String title, shortDesc;
    private int price;

    public Product(int id, String title, String shortDesc, int price) {
        this.id = id;
        this.title = title;
        this.shortDesc = shortDesc;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
