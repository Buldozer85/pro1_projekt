package controllers;

import exceptions.ItemCountIsOutOfStockException;
import models.Product;
import models.ShoppingCart;
import models.ShoppingCartItem;
import views.usersApplication.ApplicationFrame;
import views.usersApplication.CartFrame;
import views.usersApplication.tableModels.CartTableModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
            if(product.getStockLeft() < itemInCart.getProductCount() + 1) {
                throw new ItemCountIsOutOfStockException();
            }
            itemInCart.addToProductCount(shoppingCartItem.getProductCount());
            ShoppingCart.shoppingCart.set(index, itemInCart);
        } else {
            ShoppingCart.shoppingCart.add(shoppingCartItem);
        }
    }

    public static void showShoppingCart(JFrame parent) {
        CartFrame cartFrame = new CartFrame(parent);

        if (ShoppingCart.getNumberOfItemsInCart() > 0) {
            cartFrame.setVisible(true);
            parent.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(parent, "Máte prázdný košík. Pro přístup do souhrnu košíku, musíte přidat alespoň jednu položku");
        }
    }

    public static void showStore() {
        ApplicationFrame applicationFrame = new ApplicationFrame();
        applicationFrame.setVisible(true);
    }


    public static void removeItemFromCart(ShoppingCartItem shoppingCartItem, AbstractTableModel model) {
        ShoppingCart.shoppingCart.remove(shoppingCartItem);
        model.fireTableDataChanged();

        if (ShoppingCart.shoppingCart.size() == 0) {
            JOptionPane.showMessageDialog(((CartTableModel) model).getParent(), "Váš nákupní košík je prázdný, vraťte se prosím zpět do obchodu");
        }
    }

    public static void confirmOrder(String filePath) throws IOException {
        ArrayList<ShoppingCartItem> shoppingCart = ShoppingCart.shoppingCart;

        if (shoppingCart.size() == 0) {
            return;
        }

        try (FileWriter fw = new FileWriter(filePath + "\\uctenka.txt")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            fw.write("Účtenka za nákup ze : " + dtf.format(now));
            fw.write("\n-----------------------------------------");
            for (ShoppingCartItem shoppingCartItem : shoppingCart) {
                fw.write("\n" + String.format("%dx %-10s%n", shoppingCartItem.getProductCount(), shoppingCartItem.getProduct().getName()));
                fw.write(String.format("%30s Kč%n", shoppingCartItem.getSumPrice()));
                fw.write(String.format("%30s Kč/ks", shoppingCartItem.getProduct().getPrice()));
                fw.write("\n-----------------------------------------");
            }
            fw.write("\n" + String.format("Celková cena: %17s Kč", ShoppingCart.getCartPrice()));
            ProductController.update(null, null);
            ShoppingCart.shoppingCart.clear();

        }
    }
}
