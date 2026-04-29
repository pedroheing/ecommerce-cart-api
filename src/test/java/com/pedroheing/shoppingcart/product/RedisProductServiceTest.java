package com.pedroheing.shoppingcart.product;

import com.pedroheing.shoppingcart.product.dto.CreateProductInput;
import com.pedroheing.shoppingcart.product.dto.UpdateProductInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisProductServiceTest {

    @Mock ProductService databaseProductService;
    @Mock RedisTemplate<String, Object> redisTemplate;
    @Mock ProductCacheProperties cacheProperties;
    @Mock ValueOperations<String, Object> valueOps;

    RedisProductService service;

    @BeforeEach
    void setUp() {
        service = new RedisProductService(databaseProductService, redisTemplate, cacheProperties);
        when(cacheProperties.keyPrefix()).thenReturn("product:");
    }

    private Product product() {
        return Product.builder().name("Wireless Headphones").stock(10).price(new BigDecimal("99.99")).build();
    }

    @Test
    void findById_cacheHit_returnsCachedProductWithoutQueryingDatabase() {
        var productId = "id-1";
        var cached = product();
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(service.buildKey(productId))).thenReturn(cached);

        var result = service.findById(productId);

        assertThat(result).isEqualTo(cached);
        verify(databaseProductService, never()).findById(any());
    }

    @Test
    void findById_cacheMiss_queriesDatabaseAndPopulatesCache() {
        var productId = "id-1";
        var fromDb = product();
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(service.buildKey(productId))).thenReturn(null);
        when(databaseProductService.findById(productId)).thenReturn(fromDb);
        when(cacheProperties.ttl()).thenReturn(Duration.ofHours(1));

        var result = service.findById(productId);

        assertThat(result).isEqualTo(fromDb);
        verify(valueOps).set(eq(service.buildKey(productId)), eq(fromDb), any(Duration.class));
    }

    @Test
    void delete_deletesFromDatabaseAndInvalidatesCache() {
        var productId = "id-1";

        service.delete(productId);

        verify(databaseProductService).delete(productId);
        verify(redisTemplate).delete(service.buildKey(productId));
    }

    @Test
    void decrementStock_decrementsInDatabaseAndInvalidatesCache() {
        var productId = "id-1";
        var amount = 3;

        service.decrementStock(productId, amount);

        verify(databaseProductService).decrementStock(productId, amount);
        verify(redisTemplate).delete(service.buildKey(productId));
    }

    @Test
    void create_savesToDatabaseAndPopulatesCache() {
        var input = new CreateProductInput("Wireless Headphones", new BigDecimal("99.99"), 10);
        var created = product();
        when(databaseProductService.create(input)).thenReturn(created);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(cacheProperties.ttl()).thenReturn(Duration.ofHours(1));

        var result = service.create(input);

        assertThat(result).isEqualTo(created);
        verify(valueOps).set(any(), eq(created), any(Duration.class));
    }

    @Test
    void update_updatesInDatabaseAndRefreshesCache() {
        var productId = "id-1";
        var input = new UpdateProductInput(Optional.of("Pro Headphones"), Optional.empty(), Optional.empty());
        var updated = product();
        when(databaseProductService.update(productId, input)).thenReturn(updated);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(cacheProperties.ttl()).thenReturn(Duration.ofHours(1));

        service.update(productId, input);

        verify(valueOps).set(any(), eq(updated), any(Duration.class));
    }
}
