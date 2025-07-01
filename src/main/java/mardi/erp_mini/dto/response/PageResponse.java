package mardi.erp_mini.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

    private final PageValue pageValue;
    private final List<T> contents;

    public PageResponse(int totalPage, int pageSize, int number, long totalElements,
                        List<T> contents) {
        this.pageValue = new PageValue(totalPage, pageSize, number, totalElements);
        this.contents = contents;
    }

    public PageResponse(Page<T> page) {
        this(page.getTotalPages(), page.getSize(), page.getNumber(), page.getTotalElements(), page.getContent());
    }
}
