package models;

import controllers.ProductController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stock {
    private static List<Product> productsInStock = ProductController.getProducts();

    public static List<Product> getProductsInStock() {
        return productsInStock;
    }

    public static void addProductToStock(Product product) {
        productsInStock.add(product);
    }

    public static List<Product> getRefreshedProducts() {
        return ProductController.getProducts();
    }

}
