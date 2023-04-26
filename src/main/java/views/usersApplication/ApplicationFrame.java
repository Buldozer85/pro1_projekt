package views.usersApplication;

import controllers.CartController;
import listeners.StoreTableMouseListener;
import models.ShoppingCart;
import models.ShoppingCartItem;
import renderers.JTableButtonRenderer;
import views.BaseFrameLayout;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ApplicationFrame extends BaseFrameLayout {

    private JLabel priceLabel, numberOfItemsInCartLabel;

    private double shoppingCartPrice;

    private int numberOfItemsInCart;

    public ApplicationFrame() {
        super("Obchod");
        this.setHeight(600)
                .setWidth(800);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        this.add(panel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        StoreTableModel storeTableModel = new StoreTableModel(this);

        JTable table = new JTable(storeTableModel);
        storeTableModel.fireTableDataChanged();
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        TableCellRenderer tableRenderer;
        tableRenderer = table.getDefaultRenderer(JButton.class);
        table.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableRenderer));
        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true);
        table.addMouseListener(new StoreTableMouseListener(this, table));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(this.getWidth(), 200);
        panel.add(scrollPane);

        JPanel cartInfoWrapper = new JPanel();

        this.shoppingCartPrice = ShoppingCart.getCartPrice();
        this.priceLabel = new JLabel("Celková cena: " + this.shoppingCartPrice + " Kč");

        this.numberOfItemsInCart = ShoppingCart.getNumberOfItemsInCart();
        this.numberOfItemsInCartLabel = new JLabel("Počet položek v košíku: " + this.numberOfItemsInCart);

        JButton moveToCartButton = new JButton("Přejít do košíku");

        moveToCartButton.addActionListener((e)-> {
            CartController.showShoppingCart(this);
        });

        cartInfoWrapper.add(this.priceLabel);
        cartInfoWrapper.add(this.numberOfItemsInCartLabel);
        cartInfoWrapper.add(moveToCartButton);

        panel.add(cartInfoWrapper);

    }

    public void setShoppingCartPrice(double shoppingCartPrice) {
        this.shoppingCartPrice = shoppingCartPrice;
        this.priceLabel.setText("celková cena: " + this.shoppingCartPrice + " Kč");
    }

    public void setNumberOfItemsInCart(int numberOfItemsInCart) {
       this.numberOfItemsInCart = numberOfItemsInCart;
        this.numberOfItemsInCartLabel.setText("Počet položek v košíku: " + this.numberOfItemsInCart);
    }
}
