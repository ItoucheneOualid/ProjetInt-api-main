package be.eafcuccle.tshirtshop.dto;

import be.eafcuccle.tshirtshop.models.Product;

public class ProductDTO {
    private Integer id;
    private String name;
    private float price;
    private String category;
    private String imgPath;

    public ProductDTO(Product p) {
        id = p.getProductId();
        name = p.getProductName();
        price = p.getUnitPrice();
        category = p.getCategory().getName();
        imgPath = p.getImagePath();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
