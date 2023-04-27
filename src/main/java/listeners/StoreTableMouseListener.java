package listeners;

import controllers.CartController;
import exceptions.ItemCountIsOutOfStockException;
import models.Product;
import models.ShoppingCart;
import views.usersApplication.ApplicationFrame;
import views.usersApplication.tableModels.StoreTableModel;
import javax.swing.*;
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

        if (selectedColumn == StoreTableModel.ADD_ITEM_TO_CART_BUTTON) {
            try {
                CartController.addToCart(product, itemsToCart);
                this.parent.setShoppingCartPrice(ShoppingCart.getCartPrice());
                this.parent.setNumberOfItemsInCart(ShoppingCart.getNumberOfItemsInCart());
                model.fireTableDataChanged();

            } catch (ItemCountIsOutOfStockException ex) {
                JOptionPane.showMessageDialog(this.parent, ex.getMessage());
            }
        }
    }
}
