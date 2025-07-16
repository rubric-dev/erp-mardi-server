package mardi.erp_mini.core.entity.reorder;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ReorderRequest;
import mardi.erp_mini.core.response.ReorderResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReorderDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<ReorderResponse.ListRes> searchList(ReorderRequest.SearchParam searchParam) {
        return List.of();
    }
}
