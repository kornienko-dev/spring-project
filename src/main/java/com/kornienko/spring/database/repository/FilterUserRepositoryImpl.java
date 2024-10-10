package com.kornienko.spring.database.repository;

import com.kornienko.spring.database.entity.Role;
import com.kornienko.spring.database.entity.User;
import com.kornienko.spring.database.querydsl.QPredicates;
import com.kornienko.spring.dto.PersonalInfo;
import com.kornienko.spring.dto.UserFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.kornienko.spring.database.entity.QUser.user;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private static final String FIND_BY_COMPANY_AND_ROLE = """
            SELECT firstname, lastname, birth_date
            FROM users
            WHERE company_id = ? AND role = ?
            """;

    private static final String UPDATE_COMPANY_AND_ROLE = """
            UPDATE users
            SET company_id = ?, role = ?
            WHERE id = ?
            """;

    private static final String UPDATE_COMPANY_AND_ROLE_NAMED = """
            UPDATE users
            SET company_id = :companyId, role = :role
            WHERE id = :id
            """;

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public List<User> findAllByFilter(UserFilter filter) {
        Predicate predicate = QPredicates.builder()
                .add(filter.firstname(), user.firstname::containsIgnoreCase)
                .add(filter.lastname(), user.lastname::containsIgnoreCase)
                .add(filter.birthDate(), user.birthDate::before)
                .build();

        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .fetch();
    }

    @Override
    public List<PersonalInfo> findAllByCompanyIdAndRole(Integer companyId, Role role) {
        return jdbcTemplate.query(FIND_BY_COMPANY_AND_ROLE,
                (rs, rowNum) -> new PersonalInfo(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getDate("birth_date").toLocalDate()
                ), companyId, role.toString());
    }

    @Override
    public void updateCompanyAndRole(List<User> users) {
        List<Object[]> args = users.stream()
                .map(user -> new Object[] {user.getCompany().getId(), user.getRole().toString(), user.getId()})
                .toList();
        jdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE, args);
    }

    @Override
    public void updateCompanyAndRoleNamed(List<User> users) {
        MapSqlParameterSource[] args = users.stream()
                .map(user -> Map.of(
                        "id", user.getId(),
                        "companyId", user.getCompany().getId(),
                        "role", user.getRole().toString()
                        ))
                .map(MapSqlParameterSource::new)
                .toArray(MapSqlParameterSource[]::new);

        namedJdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE_NAMED, args);
    }
}
