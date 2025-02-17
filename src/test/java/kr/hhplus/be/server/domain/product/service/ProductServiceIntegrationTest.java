package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.exception.ProductException;
import kr.hhplus.be.server.infra.product.entity.ProductInventory;
import kr.hhplus.be.server.infra.product.repository.ProductInventoryJpaRepository;
import kr.hhplus.be.server.infra.product.repository.ProductJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Sql(
        scripts = {
                "/test-product-data.sql",
                "/test-product-inventory-data.sql"
        },
        config = @SqlConfig(encoding = "UTF-8")
)
@SpringBootTest
@Testcontainers
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductInventoryJpaRepository productInventoryJpaRepository;

    @Test
    @DisplayName("재고 부족 테스트")
    @Transactional
    void insufficientStock() {
        // given
        final Long PRODUCT_INVENTORY_ID = 1L;
        final Long REDUCE_QUANTITY = 11L;

        // when
        ProductException productException = assertThrows(ProductException.class, () -> {
            productService.reduceProductStock(PRODUCT_INVENTORY_ID, REDUCE_QUANTITY);
        });

        // then
        assertEquals(BAD_REQUEST, productException.getStatus());
        assertEquals("재고가 부족 합니다.", productException.getMessage());
    }

    @Test
    @DisplayName("재고 감소 테스트")
    @Transactional
    void reduceProductStock() {
        // given
        final Long REDUCE_QUANTITY = 10L;
        List<Long> idList = List.of(1L, 2L, 3L, 4L, 5L);

        // when
        for(Long productInventoryId : idList) {
            productService.reduceProductStock(productInventoryId, REDUCE_QUANTITY);
        }

        long result = productInventoryJpaRepository.findByIdIn(idList)
                                                   .orElseThrow()
                                                   .stream()
                                                   .mapToLong(ProductInventory::getStock)
                                                   .sum();

        // then
        assertEquals(0, result);
    }

    @Test
    @DisplayName("재고 감소 동시성 테스트")
    void reduceProductStockWithConcurrency() throws InterruptedException {
        // given
        final Long PRODUCT_INVENTORY_ID = 1L;
        final ExecutorService executor = Executors.newFixedThreadPool(30);
        final CountDownLatch successLatch = new CountDownLatch(10);
        final CountDownLatch failLatch = new CountDownLatch(20);

        // when
        for(int i = 0; i < 30; i++) {
            executor.submit(() -> {
                try {
                    productService.reduceProductStock(PRODUCT_INVENTORY_ID, 1L);
                    successLatch.countDown();
                } catch(Exception e) {
                    failLatch.countDown();
                }
            });
        }

        successLatch.await();
        failLatch.await();

        // then
        assertEquals(0, successLatch.getCount());
        assertEquals(0, failLatch.getCount());
    }
}