package com.bucketstore.api.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelDto {

    @NotBlank(message = "상품 코드는 필수입니다.")
    private String productCode;

    @NotBlank(message = "색상은 필수입니다.")
    private String color;

    @NotBlank(message = "사이즈는 필수입니다.")
    private String size;

    @Min(value = 1, message = "취소 수량은 최소 1개 이상이어야 합니다.")
    private int cancelCount;
}
