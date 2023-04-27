package listeners;

import controllers.ProductController;
import models.Product;
import views.administration.StockTableModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class StockTableMouseListener extends MouseAdapter {

    private final JFrame parent;
    private final JTable table;


    public StockTableMouseListener(JFrame parent, JTable table) {
        super();
        this.parent = parent;
        this.table = table;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JTable table = (JTable)e.getSource();
        StockTableModel model = (StockTableModel) table.getModel();

        Product product = model.getProductAtRow(table.convertRowIndexToModel(table.getSelectedRow()));

        switch (table.getSelectedColumn()) {
            case StockTableModel.EDIT_PRODUCT_ROW -> ProductController.showEditProductFrame(product, this.parent, table);
            case StockTableModel.DELETE_PRODUCT_ROW -> handleDeleteConfirmation(product);
        }
    }

    private void handleDeleteConfirmation(Product product) {
        int input = JOptionPane.showConfirmDialog(parent, "Opravdu si přejete smazat tento produkt?", "Potvrdit smazání", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (input == JOptionPane.OK_OPTION) {
            try {
                ProductController.delete(product);
                StockTableModel.setData(ProductController.getProducts());
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
