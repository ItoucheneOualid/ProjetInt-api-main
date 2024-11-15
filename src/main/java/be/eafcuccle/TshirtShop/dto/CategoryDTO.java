package be.eafcuccle.tshirtshop.dto;

import be.eafcuccle.tshirtshop.models.Category;

public class CategoryDTO {
    private String id;
    private String name;

    public CategoryDTO(Category c) {
        id = c.getId();
        name = c.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
