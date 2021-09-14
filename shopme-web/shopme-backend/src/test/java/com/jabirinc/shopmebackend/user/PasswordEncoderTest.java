package com.jabirinc.shopmebackend.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Getinet on 9/14/21
 */
public class PasswordEncoderTest {

    @Test
    void testEncodePassword() {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String password = "geti2020";
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(encodedPassword);

        boolean valid = passwordEncoder.matches(password,encodedPassword);
        Assertions.assertThat(valid).isTrue();

    }
}
