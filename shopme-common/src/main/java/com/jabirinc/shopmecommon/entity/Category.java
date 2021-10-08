package com.jabirinc.shopmecommon.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Getinet on 10/3/21
 */

//@Data
@Setter
@Getter
@NoArgsConstructor

@Entity()
@Table(name = "categories")
public class Category {

    public static final String DEFAULT_CATEGORY_IMAGE_NAME = "image-thumbnail.png";
    public static final String DEFAULT_CATEGORY_IMAGE_PATH = "/images/" + DEFAULT_CATEGORY_IMAGE_NAME;

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

    @Transient
    public String getCategoryImagePath() {

        if (id == null || image == null) {
            return DEFAULT_CATEGORY_IMAGE_PATH;
        }
        return "/category-images/" + this.id + "/" + this.image;
    }
}
