package com.kornienko.spring.integration.database.repository;

import com.kornienko.spring.database.entity.Role;
import com.kornienko.spring.database.entity.User;
import com.kornienko.spring.database.repository.UserRepository;
import com.kornienko.spring.dto.PersonalInfo;
import com.kornienko.spring.dto.PersonalInfo2;
import com.kornienko.spring.dto.UserFilter;
import com.kornienko.spring.integration.IntegrationTestBase;
import com.kornienko.spring.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class UserRepositoryTest extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void checkBatch() {
        List<User> users = userRepository.findAll();
        userRepository.updateCompanyAndRole(users);
        System.out.println();
    }

    @Test
    void checkJdbcTemplate() {
        List<PersonalInfo> users = userRepository.findAllByCompanyIdAndRole(1, Role.USER);
        assertThat(users).hasSize(1);
    }

    @Test
    void checkAuditing() {
        User ivan = userRepository.findById(1L).orElseThrow();
        ivan.setBirthDate(ivan.getBirthDate().plusYears(1L));
        userRepository.flush();
        System.out.println();
    }

    @Test
    void checkCustomImplementation() {
        UserFilter filter = new UserFilter(null, "ov", LocalDate.now());
        List<User> users = userRepository.findAllByFilter(filter);
        assertThat(users).hasSize(4);
    }

    @Test
    void checkProjections() {
        List<PersonalInfo2> users = userRepository.findAllByCompanyId(1);
        assertThat(users).hasSize(2);
    }

    @Test
    void checkPageable() {
        PageRequest pageable = PageRequest.of(0, 2, Sort.by("id"));
        Page<User> page = userRepository.findAllBy(pageable);
        page.forEach(user -> System.out.println(user.getCompany().getName()));

        while (page.hasNext()) {
            page = userRepository.findAllBy(page.nextPageable());
            page.forEach(user -> System.out.println(user.getCompany().getName()));
        }
    }

    @Test
    void checkSort() {
        Sort.TypedSort<User> sortBy = Sort.sort(User.class);
        Sort sort = sortBy.by(User::getFirstname)
                .and(sortBy.by(User::getLastname));

        List<User> allUsers = userRepository.findTop3ByBirthDateBefore(LocalDate.now(), sort);
        assertThat(allUsers).hasSize(3);
    }

    @Test
    void checkFirstTop() {
        Optional<User> topUser = userRepository.findTopByOrderByIdDesc();
        assertTrue(topUser.isPresent());
        topUser.ifPresent(user -> assertEquals(5L, user.getId()));
    }

    @Test
    void checkUpdate() {
        User ivan = userRepository.getReferenceById(1L);
        assertSame(Role.ADMIN, ivan.getRole());
        ivan.setBirthDate(LocalDate.now());

        int resultCount = userRepository.updateRole(Role.USER, 1L, 5L);
        assertEquals(2, resultCount);

        User theSameIvan = userRepository.getReferenceById(1L);
        assertSame(Role.USER, theSameIvan.getRole());
    }

    @Test
    void checkQueries() {
        List<User> users = userRepository.findAllBy("a", "ov");
        assertThat(users).hasSize(3);
    }
}