package models;

public class ShoppingCartItem {
    private Product product;
    private Integer productCount;

    public Product getProduct() {
        return product;
    }

    public ShoppingCartItem setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public ShoppingCartItem setProductCount(Integer productCount) {
        this.productCount = productCount;
        return this;
    }

    public ShoppingCartItem addToProductCount(Integer count) {
        if(count < 0) {
            throw new IllegalArgumentException();
        }
        this.productCount += count;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if(!(obj instanceof ShoppingCartItem)) {
            return false;
        }
        return this.getProduct().getName().equals(((ShoppingCartItem)obj).getProduct().getName());
    }
}
