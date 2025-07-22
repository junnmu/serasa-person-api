package com.serasa.personapi.infrastructure.exchange.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaginationResponse {
    private Integer currentPage;
    private Integer itemsPerPage;
    private Integer numberOfPages;
    private Long totalNumberOfItems;
}
