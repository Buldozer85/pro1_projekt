package views.usersApplication;

import listeners.CartTableMouseListener;
import models.ShoppingCart;
import renderers.JTableButtonRenderer;
import views.BaseFrameLayout;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CartFrame extends BaseFrameLayout {

    JLabel priceLabel;

    public CartFrame() {
        super("Nákupní košík");

        this.setHeight(600)
                .setWidth(800);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        this.add(panel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTable table = new JTable(new CartTableModel(this));
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        TableCellRenderer tableRenderer;
        tableRenderer = table.getDefaultRenderer(JButton.class);
        table.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableRenderer));
        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true);
        table.addMouseListener(new CartTableMouseListener(table, this));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(this.getWidth(), 200);
        panel.add(scrollPane);

        JPanel informationAboutOrderWrapper = new JPanel();

        this.priceLabel = new JLabel("Celková cena: " + ShoppingCart.getCartPrice() + " Kč");
        informationAboutOrderWrapper.add(this.priceLabel);

        JButton confirmOrderButton = new JButton("Dokončit objednávku");
        confirmOrderButton.addActionListener((e) -> {

        });
        informationAboutOrderWrapper.add(confirmOrderButton);

        JButton backToStoreButton = new JButton("Zpět do obchodu");
        informationAboutOrderWrapper.add(backToStoreButton);


        panel.add(informationAboutOrderWrapper);

    }

    public void updatePriceLabel() {
        this.priceLabel.setText("Celková cena: " + ShoppingCart.getCartPrice() + " Kč");
    }
}
