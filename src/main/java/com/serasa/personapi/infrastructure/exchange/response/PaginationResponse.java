package com.serasa.personapi.infrastructure.exchange.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PaginationResponse {
    private Integer currentPage;
    private Integer itemsPerPage;
    private Integer numberOfPages;
    private Long totalNumberOfItems;
}
