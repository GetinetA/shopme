package com.jabirinc.shopmebackend.brand;

import com.jabirinc.shopmecommon.entity.Brand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testCheckUniqueNewModeReturnDuplicate() {
        Integer id = null;
        String name = "Acer";

        Brand brand = Brand.builder().id(id).name(name).build();
        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.isBrandUnique(id, name);
        Assertions.assertThat(result).isEqualTo("Duplicate");
    }

    @Test
    public void testCheckUniqueNewModeReturnOK() {
        Integer id = null;
        String name = "ACME";

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        String result = brandService.isBrandUnique(id, name);
        Assertions.assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicate() {
        Integer id = 7;
        String name = "Canon";

        Brand brand = Brand.builder().id(id).name(name).build();
        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.isBrandUnique(100, name);
        Assertions.assertThat(result).isEqualTo("Duplicate");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOK() {
        Integer id = 7;
        String name = "Canon";

        Brand brand = Brand.builder().id(id).name(name).build();
        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.isBrandUnique(id, name);
        Assertions.assertThat(result).isEqualTo("OK");
    }

    @AfterEach
    void tearDown() {
    }
}