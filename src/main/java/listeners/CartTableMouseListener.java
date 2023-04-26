package listeners;

import controllers.CartController;
import models.ShoppingCart;
import views.administration.StockTableModel;
import views.usersApplication.CartFrame;
import views.usersApplication.CartTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CartTableMouseListener extends MouseAdapter {
    JTable table;
    CartFrame parent;

    public CartTableMouseListener(JTable table, CartFrame parent) {
        super();
        this.table = table;
        this.parent = parent;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JTable table = (JTable)e.getSource();
        CartTableModel model = (CartTableModel) table.getModel();

        int selectedRow = table.convertRowIndexToModel(table.getSelectedRow());

        if(table.getSelectedColumn() == CartTableModel.REMOVE_BUTTON_COLUMN_INDEX) {
           int input = JOptionPane.showConfirmDialog(this.parent, "Opravdu si přejete odstranit položku z košíku?","Potvrdit odstranění", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (input == JOptionPane.OK_OPTION) {
                CartController.removeItemFromCart(model.getShoppingCartItemAtRow(selectedRow), model);
                this.parent.updatePriceLabel();
            }
        }
    }
}
