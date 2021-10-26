package com.jabirinc.shopmecommon.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Getinet on 10/26/21
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter


@Entity()
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 256, nullable = false, unique = true)
    private String name;

    @Column(length = 256, nullable = false, unique = true)
    private String alias;

    @Column(name = "short_description", length = 512, nullable = false)
    private String shortDescription;

    @Column(name = "full_description", length = 4096, nullable = false)
    private String fullDescription;

    @Column(name = "created_time")
    private Date createdDate;

    @Column(name = "updated_time")
    private Date updatedDate;

    private boolean enabled;

    @Column(name = "in_stock")
    private boolean inStock;

    private float cost;
    private float price;

    @Column(name = "discount_percent")
    private float discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
}
