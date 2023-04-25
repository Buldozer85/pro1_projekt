package models;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    public static ArrayList<ShoppingCartItem> shoppingCart = new ArrayList<>();

    public static double getCartPrice() {
        return shoppingCart.stream().mapToDouble(shoppingCartItem -> {
            return shoppingCartItem.getProduct().getPrice() * shoppingCartItem.getProductCount();
        }).sum();
    }

    public static int getNumberOfItemsInCart() {
        return shoppingCart.stream().mapToInt(ShoppingCartItem::getProductCount).sum();
    }
}
