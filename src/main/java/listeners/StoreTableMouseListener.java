package listeners;

import controllers.CartController;
import exceptions.ItemCountIsOutOfStockException;
import models.Product;
import models.ShoppingCart;
import models.ShoppingCartItem;
import views.usersApplication.ApplicationFrame;
import views.usersApplication.StoreTableModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StoreTableMouseListener extends MouseAdapter {

    ApplicationFrame parent;
    JTable table;

    public StoreTableMouseListener(ApplicationFrame parent, JTable table) {
        super();
        this.parent = parent;
        this.table = table;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        StoreTableModel model = (StoreTableModel) table.getModel();

        int selectedRow = table.convertRowIndexToModel(table.getSelectedRow());
        int selectedColumn = table.getSelectedColumn();

        Product product = model.getProductAtRow(selectedRow);
        int itemsToCart = (Integer) model.getValueAt(selectedRow, StoreTableModel.ITEMS_TO_ADD_AREA_INDEX);

        switch (selectedColumn) {
            case StoreTableModel.ADD_ITEM_TO_CART_BUTTON -> {
                try {
                    CartController.addToCart(product, itemsToCart);
                    this.parent.setShoppingCartPrice(ShoppingCart.getCartPrice());
                    this.parent.setNumberOfItemsInCart(ShoppingCart.getNumberOfItemsInCart());
                    model.setValueAt(product.getStockLeft() - itemsToCart, selectedRow, StoreTableModel.STOCK_LEFT_COLUMN_INDEX);
                    model.fireTableDataChanged();
                    for (ShoppingCartItem item:ShoppingCart.shoppingCart
                         ) {
                        System.out.println(item.getProduct().getName());
                    }
                    System.out.println(ShoppingCart.getCartPrice());
                } catch (ItemCountIsOutOfStockException ex) {
                    JOptionPane.showMessageDialog(this.parent, ex.getMessage());
                }
            }
        }
    }
}
