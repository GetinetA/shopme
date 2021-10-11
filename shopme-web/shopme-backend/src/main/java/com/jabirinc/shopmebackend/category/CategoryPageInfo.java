package com.jabirinc.shopmebackend.category;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Getinet on 10/11/21
 */
@Setter
@Getter
@NoArgsConstructor
public class CategoryPageInfo {

    private int totalPages;
    private long totalElements;
}
