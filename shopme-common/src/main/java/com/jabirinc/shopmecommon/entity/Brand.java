package com.jabirinc.shopmecommon.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Getinet on 10/11/21
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

@ToString(of = {"id", "name", "categories"})

@Entity()
@Table(name = "brands")
public class Brand {

    public static final String DEFAULT_BRAND_IMAGE_NAME = "image-thumbnail.png";
    public static final String DEFAULT_BRAND_IMAGE_PATH = "/images/" + DEFAULT_BRAND_IMAGE_NAME;
    public static final String BRAND_IMAGES_DIR = "/brand-images/";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(length = 128, nullable = false)
    private String logo;

    @ManyToMany
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();


    @Transient
    public String getBrandImagePath() {

        if (id == null) {
            return DEFAULT_BRAND_IMAGE_PATH;
        }
        return BRAND_IMAGES_DIR + this.id + "/" + this.logo;
    }

}
