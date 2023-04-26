package controllers;

import exceptions.ItemCountIsOutOfStockException;
import models.Product;
import models.ShoppingCart;
import models.ShoppingCartItem;
import views.usersApplication.CartFrame;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class CartController {

    public static void addToCart(Product product, Integer itemsCount) throws ItemCountIsOutOfStockException {
        if (product.getStockLeft() < itemsCount || product.getStockLeft() == 0) {
            throw new ItemCountIsOutOfStockException();
        }

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setProduct(product)
                .setProductCount(itemsCount);

        if (ShoppingCart.shoppingCart.contains(shoppingCartItem)) {
            int index = ShoppingCart.shoppingCart.indexOf(shoppingCartItem);
            ShoppingCartItem itemInCart = ShoppingCart.shoppingCart.get(index);
            itemInCart.addToProductCount(shoppingCartItem.getProductCount());
            ShoppingCart.shoppingCart.set(index, itemInCart);
        } else {
            ShoppingCart.shoppingCart.add(shoppingCartItem);
        }
    }

    public static void showShoppingCart(JFrame parent) {
        CartFrame cartFrame = new CartFrame();

        if(ShoppingCart.getNumberOfItemsInCart() > 0) {
            cartFrame.setVisible(true);
            parent.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(parent, "Máte prázdný košík. Pro přístup do souhrnu košíku, musíte přidat alespoň jednu položku");
        }
    }


    public static void removeItemFromCart(ShoppingCartItem shoppingCartItem, AbstractTableModel model) {
        ShoppingCart.shoppingCart.remove(shoppingCartItem);
        model.fireTableDataChanged();
    }

    public static void confirmOrder() {

    }
}
