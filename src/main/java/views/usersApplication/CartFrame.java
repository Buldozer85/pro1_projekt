package views.usersApplication;

import controllers.CartController;
import controllers.ProductController;
import listeners.CartTableMouseListener;
import models.ShoppingCart;
import renderers.JTableButtonRenderer;
import views.BaseFrameLayout;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;

public class CartFrame extends BaseFrameLayout {

    JLabel priceLabel;
    JFrame previousFrame;

    public CartFrame(JFrame previousFrame) {
        super("Nákupní košík");

        this.previousFrame = previousFrame;

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
        table.addMouseListener(new CartTableMouseListener(this));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(this.getWidth(), 200);
        panel.add(scrollPane);

        JPanel informationAboutOrderWrapper = new JPanel();

        this.priceLabel = new JLabel("Celková cena: " + ShoppingCart.getCartPrice() + " Kč");
        informationAboutOrderWrapper.add(this.priceLabel);

        JButton confirmOrderButton = new JButton("Dokončit objednávku");
        confirmOrderButton.addActionListener((e) -> {
            int input = JOptionPane.showConfirmDialog(this, "Opravdu si přejete dokončit objednávku?","Potvrzení objednávky", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if(input == JOptionPane.OK_OPTION) {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int state = fileChooser.showOpenDialog(this);

                if(state == JFileChooser.APPROVE_OPTION) {
                    try {
                        CartController.confirmOrder(fileChooser.getSelectedFile().getAbsolutePath());
                        JOptionPane.showMessageDialog(this, "Objednávka byla úspěšně vytvořena");
                        ((ApplicationFrame)this.previousFrame).getTableModel().setData(ProductController.getProductsToStore());
                        ((ApplicationFrame)this.previousFrame).getTableModel().fireTableDataChanged();
                        this.previousFrame.dispose();
                        this.dispose();
                        CartController.showStore();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Potvrzení objednávky se nepodařilo");
                    }
                }
            }
        });

        if(ShoppingCart.shoppingCart.size() != 0) {
            informationAboutOrderWrapper.add(confirmOrderButton);
        }

        JButton backToStoreButton = new JButton("Zpět do obchodu");
        backToStoreButton.addActionListener((e)-> {
            this.dispose();
            this.previousFrame.setVisible(true);
            ((ApplicationFrame)this.previousFrame).setShoppingCartPrice(ShoppingCart.getCartPrice());
            ((ApplicationFrame)this.previousFrame).setNumberOfItemsInCart(ShoppingCart.getNumberOfItemsInCart());
        });
        informationAboutOrderWrapper.add(backToStoreButton);

        panel.add(informationAboutOrderWrapper);
    }

    public void updatePriceLabel() {
        this.priceLabel.setText("Celková cena: " + ShoppingCart.getCartPrice() + " Kč");
    }
}
