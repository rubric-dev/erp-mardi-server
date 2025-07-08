package mardi.erp_mini.core.entity.user;

import jakarta.annotation.Nonnull;
import mardi.erp_mini.core.entity.auth.UserAuth;
import mardi.erp_mini.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByAuth(UserAuth userAuth);

    Optional<User> findByEmail(String email);

    List<User> findByEmailIn(Collection<String> emails);

    @Query(nativeQuery = true, value = """
                SELECT u.*
                FROM users u
                JOIN company_user cu ON cu.user_id = u.id
                WHERE cu.company_id = :companyId
                  AND u.email IN (:emails)
            """)
    List<User> findByCompanyIdAndEmailIn(Long companyId, Collection<String> emails);

    @Query(nativeQuery = true, value = """
                SELECT u.*
                FROM users u
                JOIN company_user cu ON cu.user_id = u.id
                WHERE cu.company_id = :companyId
            """)
    List<User> findByCompanyId(Long companyId);


    @Modifying
    @Query(nativeQuery = true, value = "update users set rank_id = null where users.rank_id =:rankId")
    void initRank(Long rankId);

    @Modifying
    @Query(nativeQuery = true, value = "update users set position_id = null where users.position_id =:positionId")
    void initPosition(Long positionId);

    @Nonnull
    default User findOneById(@Nonnull Long id) {
        return this.findById(id).orElseThrow(() -> new NotFoundException("user not found. id : " + id));
    }
}
