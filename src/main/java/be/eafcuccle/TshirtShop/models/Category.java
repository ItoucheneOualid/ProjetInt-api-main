package be.eafcuccle.tshirtshop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The Category entity represents a product category in the T-shirt shop application.
 * Each category has a unique identifier and name, and is associated with a collection of products.
 */
@Entity
@Table(name = "category")
public class Category {

    /**
     * The unique identifier for each category, generated as a UUID string.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * The name of the category. It must be unique and cannot be null or blank.
     */
    @NotBlank(message = "The category name cannot be empty")
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * The list of products associated with the category.
     * When a category is deleted, its associated products are also deleted.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();

    /**
     * Default constructor for JPA.
     */
    public Category() {}

    /**
     * Constructs a Category with the specified name.
     *
     * @param name the name of the category
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Returns the unique identifier of the category.
     *
     * @return the category's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the category.
     *
     * @return the category name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the category.
     *
     * @param name the category name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of products associated with this category.
     *
     * @return a list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Checks if this category is equal to another object.
     * Categories are considered equal if they have the same ID.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        return id.equals(((Category) o).id);
    }

    /**
     * Returns the hash code of the category, based on its ID.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of the category.
     *
     * @return a string representation of the category
     */
    @Override
    public String toString() {
        return "Category{id='" + id + "', name='" + name + "'}";
    }
}
