package views.usersApplication;

import controllers.ProductController;
import exceptions.ItemCountIsOutOfStockException;
import models.Product;
import models.ShoppingCart;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;

public class StoreTableModel extends AbstractTableModel {

    JFrame parent;
    StoreTableModel(JFrame parent) {
        super();
        this.parent = parent;

    }

    private static final String[] columnNames = { "Název", "Počet položek skladem", "Cena (Kč)", "Počet ks v košíku", "Počet ks pro přidání do košíku", ""};

    private HashMap<Product, Integer> data = ProductController.getProductsToStore();
    private final Class<?>[] columnTypes = new  Class<?>[] {String.class, Integer.class, Double.class, Integer.class, Integer.class, JButton.class};

    public static final int NAME_COLUMN_INDEX = 0;
    public static final int STOCK_LEFT_COLUMN_INDEX = 1;
    private static final int PRICE_COLUMN_INDEX = 2;
    public static final int ITEMS_IN_CART_COLUMN_INDEX = 3;
    public static final int ITEMS_TO_ADD_AREA_INDEX = 4;
    public static final int ADD_ITEM_TO_CART_BUTTON = 5;

    @Override
    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    public Product getProductAtRow(int rowIndex) {
        return (Product) data.keySet().toArray()[rowIndex];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return (column == ITEMS_TO_ADD_AREA_INDEX && getProductAtRow(row).getStockLeft() > 0);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product productAtRow = getProductAtRow(rowIndex);

        return switch (columnIndex) {
            case NAME_COLUMN_INDEX -> productAtRow.getName();
            case STOCK_LEFT_COLUMN_INDEX -> productAtRow.getStockLeft();
            case PRICE_COLUMN_INDEX -> productAtRow.getPrice();
            case ITEMS_IN_CART_COLUMN_INDEX -> ShoppingCart.getCountOfOneProductInCart(productAtRow);
            case ITEMS_TO_ADD_AREA_INDEX -> data.get(productAtRow);
            case ADD_ITEM_TO_CART_BUTTON -> new JButton("Přidat do košíku");
            default -> throw new IllegalArgumentException("Odkaz na neexistující sloupec");
        };
    }
    @Override
    public void setValueAt(Object value, int row, int column) {

        switch (column) {
            case STOCK_LEFT_COLUMN_INDEX -> getProductAtRow(row).setStockLeft((Integer) value);
            case ITEMS_TO_ADD_AREA_INDEX -> {
                Product product = getProductAtRow(row);
                if(product.getStockLeft() < (Integer) value) {
                    JOptionPane.showMessageDialog(parent, new ItemCountIsOutOfStockException().getMessage());
                }
            }
        }
    }

    public void setData(HashMap<Product,Integer> data) {
        this.data = data;
    }
}
