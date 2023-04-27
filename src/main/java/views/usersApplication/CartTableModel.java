package views.usersApplication;

import controllers.CartController;
import exceptions.ItemCountIsOutOfStockException;
import models.ShoppingCart;
import models.ShoppingCartItem;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class CartTableModel extends AbstractTableModel {


    private static final String[] columnNames = {"Název položky", "Počet Ks", "Cena za ks", "Celková cena", ""};
    private final Class<?>[] columnTypes = new Class<?>[]{String.class, Integer.class, Double.class, Double.class, JButton.class};

    private static ArrayList<ShoppingCartItem> data = ShoppingCart.shoppingCart;

    public static final int NAME_COLUMN_INDEX = 0;
    public static final int NUMBER_OF_ITEMS_IN_CART_COLUMN_INDEX = 1;

    public static final int PRICE_PER_PIECE_COLUMN_INDEX = 2;

    public static final int CART_PRICE_SUM_COLUMN_INDEX = 3;

    public static final int REMOVE_BUTTON_COLUMN_INDEX = 4;

    private final CartFrame parent;

    public CartTableModel(CartFrame parent) {
        super();
        this.parent = parent;
    }

    public ShoppingCartItem getShoppingCartItemAtRow(int row) {
        return data.get(row);
    }

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

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ShoppingCartItem shoppingCartItem = getShoppingCartItemAtRow(rowIndex);

        return switch (columnIndex) {
            case NAME_COLUMN_INDEX -> shoppingCartItem.getProduct().getName();
            case NUMBER_OF_ITEMS_IN_CART_COLUMN_INDEX -> shoppingCartItem.getProductCount();
            case PRICE_PER_PIECE_COLUMN_INDEX -> shoppingCartItem.getProduct().getPrice();
            case CART_PRICE_SUM_COLUMN_INDEX -> shoppingCartItem.getSumPrice();
            case REMOVE_BUTTON_COLUMN_INDEX -> new JButton("Odstranit");
            default -> throw new IllegalArgumentException("Odkaz na neexistující sloupec");
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == NUMBER_OF_ITEMS_IN_CART_COLUMN_INDEX;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ShoppingCartItem shoppingCartItem = data.get(rowIndex);

        if (shoppingCartItem.getProduct().getStockLeft() < (Integer) aValue) {
            JOptionPane.showMessageDialog(this.parent, new ItemCountIsOutOfStockException().getMessage());
        } else {
            if ((Integer) aValue == 0) {
                CartController.removeItemFromCart(shoppingCartItem, this);
                this.fireTableDataChanged();
                this.parent.updatePriceLabel();
                return;
            }
            ShoppingCart.shoppingCart.set(rowIndex, shoppingCartItem.setProductCount((Integer) aValue));
            this.fireTableDataChanged();
            this.parent.updatePriceLabel();
        }
    }

    public void setData(ArrayList<ShoppingCartItem> newData) {
        data = newData;
        this.fireTableDataChanged();
    }

    public CartFrame getParent() {
        return this.parent;
    }
}
