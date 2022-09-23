package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {

    SALE("SALE", "판매중"),
    SOLD_OUT("SOLD_OUT", "품절"),
    NEW("NEW", "신상품");

    private final String key;
    private final String title;

}
