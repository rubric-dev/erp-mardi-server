package mardi.erp_mini.core.entity.option;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepletionRepository extends JpaRepository<DepletionLevel, Long> {

    List<DepletionLevel> findAllByOrderBySeq();
}
