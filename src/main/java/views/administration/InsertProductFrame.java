package views.administration;

import controllers.ProductController;
import models.Product;
import views.BaseFrameLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertProductFrame extends BaseFrameLayout {

    private final JFrame parent;

    public InsertProductFrame(JFrame parent, JTable table) {
        super("Přidání nového produktu");
        this.setHeight(400).setWidth(300);
        this.parent = parent;
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleUnsavedWorkExit();
            }
        });

        JPanel mainPanel = new JPanel();
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockLeftField = new JTextField();
        JLabel nameLabel = new JLabel("Název");
        JLabel priceLabel = new JLabel("Cena");
        JLabel stockLeftLabel = new JLabel("Kusů na skladě");
        JButton addProductButton = new JButton("Uložit produkt");
        JButton closeWindowButton = new JButton("Zpět");

        nameField.setMaximumSize(new Dimension(200, 100));
        priceField.setMaximumSize(new Dimension(200, 100));
        stockLeftField.setMaximumSize(new Dimension(200, 100));

        closeWindowButton.setBackground(new Color(255, 0, 0));
        closeWindowButton.setForeground(new Color(255, 255, 255));
        addProductButton.setBackground(new Color(65, 105, 225));
        addProductButton.setForeground(new Color(255, 255, 255));

        addProductButton.addActionListener((e) -> {
            try {
                if (checkIfEmpty(nameField.getText(), priceField.getText(), stockLeftField.getText())) {
                    JOptionPane.showMessageDialog(this, "Jeden nebo více polí není vyplněných");
                    return;
                }

                Product newProduct = null;

                try {
                    Pattern patternPrice =  Pattern.compile("^[0-9]+\\.?[0-9]*");
                    Matcher matcherPrice = patternPrice.matcher(priceField.getText());

                    if(!matcherPrice.find()) {
                        JOptionPane.showMessageDialog(this, "Cena byl zadaná ve špatném formátu");
                        return;
                    }

                    Pattern patterStockLeft = Pattern.compile("^[0-9]+");
                    Matcher matcherStockLeft = patterStockLeft.matcher(stockLeftField.getText());

                    if(!matcherStockLeft.find()) {
                        JOptionPane.showMessageDialog(this, "Počet položek byl zadán ve špatném formátu");
                        return;
                    }

                    newProduct = new Product(nameField.getText(), Double.parseDouble(priceField.getText()), Integer.parseInt(stockLeftField.getText()));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Jedno nebo více polí je zadáno ve špatném formátu");
                }

                if (newProduct != null) {
                    ProductController.addNewProduct(newProduct);
                }

                StockTableModel.setData(ProductController.getProducts());
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();

                parent.setVisible(true);
                this.setVisible(false);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Vyskytla se neočekávaná chyba" + ex.getMessage());
            }
        });

        closeWindowButton.addActionListener((e) -> {
            try {
                if (!ProductController.checkIfProductExists(
                        new Product(
                                nameField.getText(),
                                Double.parseDouble(priceField.getText()),
                                Integer.parseInt(stockLeftField.getText()))
                )
                ) {
                    handleUnsavedWorkExit();
                }
            } catch (NumberFormatException ex) {
                handleUnsavedWorkExit();
            }

        });

        GroupLayout gl = new GroupLayout(mainPanel);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);


        gl.setHorizontalGroup(
                gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup()
                                .addComponent(nameLabel)
                                .addComponent(priceLabel)
                                .addComponent(stockLeftLabel)
                                .addComponent(addProductButton)
                        )
                        .addGroup(
                                gl.createParallelGroup()
                                        .addComponent(nameField)
                                        .addComponent(priceField)
                                        .addComponent(stockLeftField)
                                        .addComponent(closeWindowButton)
                        )
        );

        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGroup(
                                gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(nameLabel)
                                        .addComponent(nameField)
                        )
                        .addGroup(
                                gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(priceLabel)
                                        .addComponent(priceField)
                        )
                        .addGroup(
                                gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(stockLeftLabel)
                                        .addComponent(stockLeftField)
                        )
                        .addGroup(
                                gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(addProductButton)
                                        .addComponent(closeWindowButton)
                        )
        );

        mainPanel.setLayout(gl);
        this.add(mainPanel);
        this.setVisible(true);
    }

    private void handleUnsavedWorkExit() {
        int input = JOptionPane.showConfirmDialog(this, "Opravdu si přejete odejít bez uložení produktu?", "Potvedit odchod", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (input == JOptionPane.OK_OPTION) {
            this.setVisible(false);
            this.parent.setVisible(true);
        } else {
            this.setVisible(true);
        }
    }

    private boolean checkIfEmpty(String name, String price, String stockLeft) {
        return name.isEmpty() || price.isEmpty() || stockLeft.isEmpty();
    }


}
