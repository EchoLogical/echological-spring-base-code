package com.github.echological.akint.global.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedBaseResponse<T> extends BaseResponse<T> {

    PageInfo pageInfo;

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PageInfo {
        Long pagePos;
        Long pageSize;
        Long totalData;
        Long totalPage;
    }

}
