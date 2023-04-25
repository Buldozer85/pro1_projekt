package views.administration;

import controllers.ProductController;
import models.Product;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class StockTableModel extends AbstractTableModel  {

    public static final int NAME_COLUMN_INDEX = 0;
    private static final int STOCK_LEFT_COLUMN_INDEX = 1;
    private static final int PRICE_COLUMN_INDEX = 2;
    public static final int EDIT_PRODUCT_ROW = 3;

    public static final int DELETE_PRODUCT_ROW = 4;
    private static final String[] columnNames = { "Název", "Počet ks", "Cena (Kč)", "Upravit", "Smazání"};
    private static final String[] columnNamesWithoutActions = { "Název", "Počet ks", "Cena (Kč)"};
    private final Class<?>[] columnTypes = new  Class<?>[] {String.class, Integer.class, Double.class, JButton.class, JButton.class};

    private static List<Product> data = ProductController.getProducts();


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
        return data.get(rowIndex);
    }




    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        JButton deleteButton = new JButton("Smazat");
        deleteButton.setBackground(new Color(255,0,0));
        deleteButton.setForeground(new Color(255,255,255));

        return switch (columnIndex) {
            case NAME_COLUMN_INDEX -> data.get(rowIndex).getName();
            case STOCK_LEFT_COLUMN_INDEX -> data.get(rowIndex).getStockLeft();
            case PRICE_COLUMN_INDEX -> data.get(rowIndex).getPrice();
            case EDIT_PRODUCT_ROW -> new JButton("Upravit");
            case DELETE_PRODUCT_ROW -> deleteButton;
            default -> throw new IllegalArgumentException("Odkaz na neexistující sloupec");
        };
    }

    public static void addData(Product data) {
        StockTableModel.data.add(data);
    }

    public static void setData(List<Product> data) {
        StockTableModel.data = data;
    }

    public static String[] getColumnNames() {
        return columnNames;
    }

    public static String[] getColumnNamesWithoutActions() {
        return columnNamesWithoutActions;
    }
}
