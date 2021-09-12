package com.jabirinc.shopmebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        com.jabirinc.shopmecommon.entity.Role.class,
        com.jabirinc.shopmebackend.user.RoleRepository.class})
public class ShopmeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopmeBackendApplication.class, args);
    }

}
