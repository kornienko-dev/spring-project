package com.kornienko.spring.database.repository;

import com.kornienko.spring.database.entity.Role;
import com.kornienko.spring.database.entity.User;
import com.kornienko.spring.dto.PersonalInfo2;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public interface UserRepository extends
        JpaRepository<User, Long>,
        FilterUserRepository,
        RevisionRepository<User, Long, Integer>,
        QuerydslPredicateExecutor<User> {

    @Query("select u from User u where u.firstname like %:firstname% and u.lastname like %:lastname%")
    List<User> findAllBy(String firstname, String lastname);

    @Query(value = "SELECT u.* FROM users u WHERE u.username LIKE %:username%",
            nativeQuery = true)
    List<User> findAllByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.role = :role where u.id in (:ids)")
    int updateRole(Role role, Long... ids);

    Optional<User> findTopByOrderByIdDesc();

    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "50"))
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<User> findTop3ByBirthDateBefore(LocalDate birthDate, Sort sort);

    // Collection, Stream
    // Streamable, Slice, Page
    //    @EntityGraph("User.company")
    @Query(value = "select u from User u", countQuery = "select count(distinct u.firstname) from User u")
    @EntityGraph(attributePaths = {"company", "company.locales"})
    Page<User> findAllBy(Pageable pageable);

    //    @Transactional(readOnly = true)
    @Query(value = "SELECT firstname, " +
                   "lastname, " +
                   "birth_date birthDate " +
                   "FROM users " +
                   "WHERE company_id = :companyId",
            nativeQuery = true)
    List<PersonalInfo2> findAllByCompanyId(Integer companyId);

    Optional<User> findByUsername(String username);

    //    List<PersonalInfo> findAllByCompanyId(Integer companyId);
}
