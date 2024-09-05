package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static com.compass.ecommerce_spring.common.ProductConstants.BATTERIES;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class ProductRepositoryTest extends BaseTest {

    @Autowired
    private ProductStockRepository repository;

    @Test
    public void getProduct_ByExistingName_ReturnsProduct() {
        var sut = repository.findByName(BATTERIES.getName()).orElse(null);

        assertThat(sut).isNotNull();
        assertThat(sut).isEqualTo(BATTERIES);
    }

    @Test
    public void getProduct_ByNonexistentName_ReturnsEmpty() {
        var sut = repository.findByName("NonExistingName").orElse(null);

        assertThat(sut).isNull();
    }

    @Test
    public void existsProduct_ByExistingName_ReturnsTrue() {
        var sut = repository.existsByName(BATTERIES.getName());

        assertThat(sut).isTrue();
    }

    @Test
    public void existsProduct_ByNonexistentName_ReturnsFalse() {
        var sut = repository.existsByName("NonExistingName");

        assertThat(sut).isFalse();
    }
}
