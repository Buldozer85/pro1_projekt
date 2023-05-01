package views.usersApplication;

import controllers.CartController;
import listeners.StoreTableMouseListener;
import models.ShoppingCart;
import renderers.JTableButtonRenderer;
import services.ApplicationServices;
import views.BaseFrameLayout;
import views.usersApplication.tableModels.StoreTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ApplicationFrame extends BaseFrameLayout {

    private final JLabel priceLabel, numberOfItemsInCartLabel;

    private double shoppingCartPrice;

    private int numberOfItemsInCart;

    private StoreTableModel tableModel = null;

    public ApplicationFrame() {
        super("Obchod");
        this.setHeight(600)
                .setWidth(800);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        this.add(panel);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        try {
            this.tableModel = new StoreTableModel(this);
            JTable table = new JTable(this.tableModel);
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
        } catch (IOException e) {
           JOptionPane.showMessageDialog(this, "Obchod zatím nenabízí žádné produkty");
           System.exit(0);
        }



        JPanel cartInfoWrapper = new JPanel();

        this.shoppingCartPrice = ShoppingCart.getCartPrice();
        this.priceLabel = new JLabel("Celková cena: " + this.shoppingCartPrice + " Kč");

        this.numberOfItemsInCart = ShoppingCart.getNumberOfItemsInCart();
        this.numberOfItemsInCartLabel = new JLabel("Počet položek v košíku: " + this.numberOfItemsInCart);

        JButton moveToCartButton = new JButton("Přejít do košíku");

        moveToCartButton.addActionListener((e)-> CartController.showShoppingCart(this));

        cartInfoWrapper.add(this.priceLabel);
        cartInfoWrapper.add(this.numberOfItemsInCartLabel);
        cartInfoWrapper.add(moveToCartButton);

        panel.add(cartInfoWrapper);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ApplicationServices.handleExit((JFrame) e.getWindow());
            }
        });

    }

    public void setShoppingCartPrice(double shoppingCartPrice) {
        this.shoppingCartPrice = shoppingCartPrice;
        this.priceLabel.setText("celková cena: " + this.shoppingCartPrice + " Kč");
    }

    public void setNumberOfItemsInCart(int numberOfItemsInCart) {
       this.numberOfItemsInCart = numberOfItemsInCart;
        this.numberOfItemsInCartLabel.setText("Počet položek v košíku: " + this.numberOfItemsInCart);
    }

    public StoreTableModel getTableModel() {
        return this.tableModel;
    }


}
