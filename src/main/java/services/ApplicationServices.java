package services;

import models.ShoppingCart;

import javax.swing.*;

public class ApplicationServices {

    public static void handleExit(JFrame jOptionPaneParent) {
        int input;
        if(!ShoppingCart.shoppingCart.isEmpty()) {
            input = JOptionPane.showConfirmDialog(jOptionPaneParent, "Opravdu si přejete odejít bez dokončení objednávky?", "Potvrdit odchod", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        } else {
            input = JOptionPane.showConfirmDialog(jOptionPaneParent, "Opravdu si přejete ukončit aplikaci?", "Potvrdit odchod", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        }

        if(input == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }
}
