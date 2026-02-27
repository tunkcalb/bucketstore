package com.bucketstore.api.product.repository;
import com.bucketstore.api.global.config.QuerydslConfig;
import com.bucketstore.api.product.entity.Product;
import com.bucketstore.api.product.entity.ProductItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class) // 직접 만든 설정 파일 로드
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 db 사용
class ProductItemRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductItemRepository productItemRepository;

    @Test
    @DisplayName("비관적 락을 적용한 상품 조회 쿼리가 정상 작동한다")
    void findForUpdateTest() {
        // 1. 테스트 데이터 저장
        Product product = productRepository.save(new Product("P001"));
        productItemRepository.save(new ProductItem(product, "RED", "M", 100));

        // 2. 락 걸고 조회
        Optional<ProductItem> result = productItemRepository
                .findProductItemForUpdate("P001", "RED", "M");

        // 3. 검증
        assertThat(result).isPresent();
        assertThat(result.get().getStock()).isEqualTo(100);
    }
}