package models;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Tato třída reprezertuje produkt nabízený v nabídce a skladu
 */
public class Product {

    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private double price;
    @JsonProperty("stock_left")
    private int stockLeft;

    public Product(String name, double price, int stockLeft) {
        this.name = name;
        this.price = price;
        this.stockLeft = stockLeft;
    }

    public Product() {
        //
    }

    public double getPrice() {
        return price;
    }

    public Product setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Zadali jste zápornou hodnotu");
        }
        this.price = price;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public int getStockLeft() {
        return stockLeft;
    }

    public Product setStockLeft(int stockLeft) {
        if (stockLeft < 0) {
            throw new IllegalArgumentException("Zadali jste zápornou hodnotu");
        }
        this.stockLeft = stockLeft;
        return this;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if(!(obj instanceof Product product)) {
            return false;
        }

        return  this.name.equals(product.name) && this.price == product.price && this.stockLeft == product.stockLeft;
    }
}
