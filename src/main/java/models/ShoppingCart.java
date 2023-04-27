package models;

import services.NumberServices;

import java.util.ArrayList;

public class ShoppingCart {
    public static ArrayList<ShoppingCartItem> shoppingCart = new ArrayList<>();

    public static double getCartPrice() {
        return NumberServices.roundDouble(shoppingCart.stream().mapToDouble(shoppingCartItem -> shoppingCartItem.getProduct().getPrice() * shoppingCartItem.getProductCount()).sum());
    }

    public static int getNumberOfItemsInCart() {
        return shoppingCart.stream().mapToInt(ShoppingCartItem::getProductCount).sum();
    }

    public static int getCountOfOneProductInCart(Product product) {
        ShoppingCartItem item = new ShoppingCartItem().setProduct(product);
        if(shoppingCart.contains(item)) {
            return shoppingCart.get(shoppingCart.indexOf(item)).getProductCount();
        }
        return 0;
    }
}
