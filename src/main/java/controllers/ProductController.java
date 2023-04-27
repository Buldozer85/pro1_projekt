package controllers;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Product;
import models.ShoppingCart;
import models.ShoppingCartItem;
import views.administration.EditProductFrame;
import views.administration.InsertProductFrame;
import views.administration.tableModels.StockTableModel;
import javax.swing.*;

public class ProductController {

    private static final String STORED_PRODUCTS_FILE = "src/main/java/resources/products.json";
    private static final String EXPORTED_PRODUCTS_FILE = "src/main/java/resources/export.csv";

    public static List<Product> getProducts() {
        try {
            byte[] mapData = Files.readAllBytes(Paths.get(STORED_PRODUCTS_FILE));
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<Product> list = new ArrayList<>(Arrays.asList(objectMapper.readValue(mapData, Product[].class)));
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<Product, Integer> getProductsToStore() {
        HashMap<Product, Integer> products = new HashMap<>();

        for (Product product: getProducts()) {
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem().setProduct(product);
            System.out.println(ShoppingCart.shoppingCart.contains(shoppingCartItem));
            if(ShoppingCart.shoppingCart.contains(shoppingCartItem)) {
                products.put(shoppingCartItem.getProduct(), shoppingCartItem.getProductCount());
            } else {
                if (product.getStockLeft() == 0) {
                    products.put(product, 0);
                } else {
                    products.put(product, 1);
                }
            }

        }
        return products;
    }

    public static void showInsertProductWindow(JFrame parent, JTable table) {
        JFrame insertWindow = new InsertProductFrame(parent, table);
    }

    public static void addNewProduct(Product product) throws IOException {
        String formattedString = removeClosingBracketFromFile();

        try (FileWriter fw = new FileWriter(STORED_PRODUCTS_FILE)) {
            fw.write(formattedString);
            ObjectMapper mapper = new ObjectMapper();
            SequenceWriter sequenceWriter = mapper.writerWithDefaultPrettyPrinter().writeValues(fw);
            sequenceWriter.write(product);
            fw.append("]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkIfProductExists(Product product) throws NumberFormatException {
        List<Product> products = getProducts();

        return products.contains(product);
    }

    public static void showEditProductFrame(Product product, JFrame parent, JTable table) {
        JFrame editWindow = new EditProductFrame(product, parent, table);
        editWindow.setVisible(true);
    }

    public static void delete(Product product) throws IOException {

        List<Product> products = getProducts();

        System.out.println(products.contains(product));

        products.remove(product);

        try (FileWriter fw = new FileWriter(STORED_PRODUCTS_FILE)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(fw, products);
        }

    }

    public static void update(Product product, String productName) throws IOException {
        JsonNode newNode;
        if(product != null && productName != null) {
            newNode = getUpdatedJsonNode(product, productName);
        } else {
            newNode = getUpdatedJsonNode(null, null);
        }

        try (FileWriter fw = new FileWriter(STORED_PRODUCTS_FILE)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(fw, newNode);
        }
    }

    public static void exportToCSV() throws IOException {
        try(BufferedWriter wr = new BufferedWriter(new FileWriter(EXPORTED_PRODUCTS_FILE, StandardCharsets.UTF_16))) {
            List<Product> products = getProducts();

            wr.write(String.join(",", StockTableModel.getColumnNamesWithoutActions() ));

            for (Product p: products) {
                String line = String.format("\"%s\",%s,%.1f",
                        p.getName(), p.getStockLeft(), p.getPrice());
                wr.newLine();
                wr.write(line);
            }

        }
    }

/*    public static JsonNode getJsonNodeWithUpdatedStockLeft() throws IOException {
        try (FileReader fr = new FileReader(STORED_PRODUCTS_FILE)) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode newNode = mapper.readTree(fr);

            for (JsonNode p : newNode) {
                ShoppingCartItem item = ShoppingCart.shoppingCart.stream().filter(shoppingCartItem -> {
                    return shoppingCartItem.getProduct().getName().equals(p.get("name").asText());
                }).toList().get(0);

                if (item != null) {
                    ObjectNode objectNode = (ObjectNode) p;
                    objectNode.put("stock_left", item.getProduct().getStockLeft() - item.getProductCount());
                    break;
                }
            }
            fr.close();
            return newNode;
        }
    }*/

    private static JsonNode getUpdatedJsonNode(Product product, String productName) throws IOException {
        try (FileReader fr = new FileReader(STORED_PRODUCTS_FILE)) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode newNode = mapper.readTree(fr);

            if(product != null && productName != null) {
                for (JsonNode p : newNode) {
                    if (Objects.equals(p.get("name").asText(), productName)) {
                        ObjectNode objectNode = (ObjectNode) p;
                        objectNode.put("name", product.getName());
                        objectNode.put("price", product.getPrice());
                        objectNode.put("stock_left", product.getStockLeft());
                        break;
                    }
                }
            } else {
                for (JsonNode p : newNode) {
                    List<ShoppingCartItem> item = ShoppingCart.shoppingCart.stream().filter(shoppingCartItem -> {
                        return shoppingCartItem.getProduct().getName().equals(p.get("name").asText());
                    }).toList();

                    System.out.println(ShoppingCart.shoppingCart);
                    System.out.println(p.get("name"));

                    if (item.size() == 1) {
                        ObjectNode objectNode = (ObjectNode) p;
                        objectNode.put("stock_left", item.get(0).getProduct().getStockLeft() - item.get(0).getProductCount());
                    }
                }
            }
            fr.close();
            return newNode;
        }
    }

    private static String removeClosingBracketFromFile() throws IOException {
        StringBuilder fileContent = new StringBuilder(Files.readString(Paths.get(STORED_PRODUCTS_FILE)));

        int indexOfBracket = fileContent.lastIndexOf("]");
        fileContent.replace(indexOfBracket, indexOfBracket + 1, ",");
        return fileContent.toString();
    }
}
