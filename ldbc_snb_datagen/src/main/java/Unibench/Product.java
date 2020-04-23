package Unibench;

public class Product {
    public String id = null;
    public String imUrl = null;
    public String title = null;
    public double Price = 0;
    public String salesRank = null;
    public String description = null;
    public String brand = null;
    public String Categories = null;
    public String tag = null;
    public Product(String id, String title, double price, String imUrl, String salesRank, String categories, String description,
                   String brand, String tag) {
        super();
        this.id = id;
        this.imUrl = imUrl;
        Categories = categories;
        this.title = title;
        Price = price;
        this.salesRank = salesRank;
        this.description = description;
        this.brand = brand;
        this.tag = tag;
    }
}