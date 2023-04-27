package views.administration;

import controllers.ProductController;
import listeners.StockTableMouseListener;
import renderers.JTableButtonRenderer;
import views.BaseFrameLayout;
import views.administration.tableModels.StockTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;

public class AdministrationFrame extends BaseFrameLayout {

    public AdministrationFrame() {
        super("Administrace");
        this.setHeight(600)
                .setWidth(800);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        this.add(panel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTable table = new JTable(new StockTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        TableCellRenderer tableRenderer;
        tableRenderer = table.getDefaultRenderer(JButton.class);
        table.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableRenderer));
        table.setRowHeight(30);
        table.addMouseListener(new StockTableMouseListener(this, table));
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(this.getWidth(), 200);

        JPanel actionButtonsWrapper = new JPanel();
        actionButtonsWrapper.setLayout(new FlowLayout());

        JButton addProduct = new JButton("Přidat produkt");
        addProduct.addActionListener((e -> {
            ProductController.showInsertProductWindow(this, table);
            this.setVisible(false);
        }));

        JButton exportToCSVButton = new JButton("Exportovat produkty");

        exportToCSVButton.addActionListener((l)-> {
            try {
                ProductController.exportToCSV();
                JOptionPane.showMessageDialog(this, "Produkty byly úspěšně exportovány");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Při exportování souboru došlo k neošekávané chybě");
            }
        });

        actionButtonsWrapper.add(addProduct);
        actionButtonsWrapper.add(exportToCSVButton);
        panel.add(scrollPane);
        panel.add(actionButtonsWrapper);

        this.setVisible(true);
    }

}
