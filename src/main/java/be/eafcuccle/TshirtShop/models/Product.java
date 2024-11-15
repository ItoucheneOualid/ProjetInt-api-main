package be.eafcuccle.tshirtshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import be.eafcuccle.tshirtshop.models.Brand;
import be.eafcuccle.tshirtshop.models.Category;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Objects;

/**
 * The Product entity represents a product in the T-shirt shop application.
 * Each product has attributes such as name, description, price, stock quantity, color, category, brand, size, and an image path.
 */
@Entity
@Table(name = "product")
public class Product {

    /**
     * The unique identifier for each product, auto-generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    /**
     * The name of the product. It cannot be null or empty.
     */
    @NotBlank(message = "The product name cannot be empty")
    @Column(nullable = false)
    private String productName;

    /**
     * The description of the product.
     */
    private String productDescription;

    /**
     * The unit price of the product. It must be a positive value and cannot be null.
     */
    @NotNull
    @Positive
    @Column(nullable = false)
    private float unitPrice;

    /**
     * The quantity of the product in stock. It must be zero or positive.
     */
    @PositiveOrZero
    @Column(nullable = false)
    private int quantityInStock = 0;

    /**
     * The color of the product. It cannot be null or empty.
     */
    @NotBlank
    @Column(nullable = false)
    private String color;

    /**
     * The category associated with the product. This relationship is mandatory.
     */
    @ManyToOne(optional = false)
    @JsonBackReference
    private Category category;

    /**
     * The brand associated with the product. This relationship is mandatory.
     */
    @ManyToOne(optional = false)
    private Brand brand;

    /**
     * The size of the product. It is an enumerated value and cannot be null.
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private Size size;

    /**
     * The file path to the product image.
     */
    private String imagePath;

    /**
     * Default constructor for JPA.
     */
    public Product() {}

    /**
     * Constructs a Product with the specified attributes.
     *
     * @param productName the name of the product
     * @param productDescription the description of the product
     * @param unitPrice the unit price of the product
     * @param quantityInStock the quantity in stock
     * @param color the color of the product
     * @param category the category of the product
     * @param brand the brand of the product
     * @param size the size of the product
     */
    public Product(String productName, String productDescription, float unitPrice, int quantityInStock, String color,
                   Category category, Brand brand, Size size) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.unitPrice = unitPrice;
        this.quantityInStock = quantityInStock;
        this.color = color;
        this.category = category;
        this.brand = brand;
        this.size = size;
    }

    // Getters and Setters

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Sets a default image path for the product using its ID.
     * This method is called before the product entity is persisted.
     */
    @PrePersist
    public void setDefaultImagePath() {
        this.imagePath = "src/main/resources/images/tshirts/" + this.productId + ".jpg";
    }

    /**
     * Checks if this product is equal to another object.
     * Products are considered equal if they have the same product ID.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return productId.equals(((Product) o).productId);
    }

    /**
     * Returns the hash code of the product, based on its ID.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    /**
     * Returns a string representation of the product.
     *
     * @return a string representation of the product
     */
    @Override
    public String toString() {
        return "Product{id=" + productId + ", name='" + productName + "', category=" + category.getName() + ", brand=" + brand.getName() + ", size=" + size + "}";
    }
}