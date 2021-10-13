package com.jabirinc.shopmecommon.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Getinet on 10/3/21
 */

@Setter
@Getter
@NoArgsConstructor
@ToString(of = {"name"})

@Entity()
@Table(name = "categories")
public class Category {

    public static final String DEFAULT_CATEGORY_IMAGE_NAME = "image-thumbnail.png";
    public static final String DEFAULT_CATEGORY_IMAGE_PATH = "/images/" + DEFAULT_CATEGORY_IMAGE_NAME;
    public static final String CATEGORY_IMAGES_DIR = "/category-images/";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    public Category(Integer id) {
        this.id = id;
    }

    public Category(String name) {
        this(name, name, DEFAULT_CATEGORY_IMAGE_NAME);
    }

    public Category(String name, String alias, String image) {
        this.name = name;
        this.alias = alias;
        this.image = image;
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }

    public static Category create(Integer id, String name) {
        return Category.create(id, name, null);
    }

    public static Category create(Integer id, String name, String alias) {
        Category copyCategory = new Category();
        copyCategory.setId(id);
        copyCategory.setName(name);
        copyCategory.setAlias(alias);

        return copyCategory;
    }

    public static Category deepCopy(Category category) {
        Category copyCategory = new Category();
        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());
        copyCategory.setAlias(category.getAlias());
        copyCategory.setImage(category.getImage());
        copyCategory.setEnabled(category.isEnabled());
        copyCategory.setHasChildren(category.getChildren().size() > 0);
        if (category.getParent() != null) {
            copyCategory.setParent(deepCopy(category.getParent()));
        }

        return copyCategory;
    }

    @Transient
    public String getCategoryImagePath() {

        if (id == null || "default.png".equalsIgnoreCase(image)) {
            return DEFAULT_CATEGORY_IMAGE_PATH;
        }
        return CATEGORY_IMAGES_DIR + this.id + "/" + this.image;
    }

    @Transient
    public boolean hasChildren;
}
