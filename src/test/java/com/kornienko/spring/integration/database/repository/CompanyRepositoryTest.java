package com.kornienko.spring.integration.database.repository;

import com.kornienko.spring.database.entity.Company;
import com.kornienko.spring.database.repository.CompanyRepository;
import com.kornienko.spring.integration.IntegrationTestBase;
import com.kornienko.spring.integration.annotation.IT;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class CompanyRepositoryTest extends IntegrationTestBase {

    private static final Integer APPLE_ID = 5;
    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;
    private final CompanyRepository companyRepository;

    @Test
    void checkFindByQueries() {
        System.out.println(companyRepository.findByName("google"));
        companyRepository.findAllByNameContainingIgnoreCase("a");
    }

    @Test
    @Disabled
    void delete() {
        Optional<Company> maybeCompany = companyRepository.findById(APPLE_ID);
        assertTrue(maybeCompany.isPresent());
        maybeCompany.ifPresent(companyRepository::delete);
        entityManager.flush();
        assertTrue(companyRepository.findById(APPLE_ID).isEmpty());
    }

    @Test
    void findById() {
        transactionTemplate.executeWithoutResult(tx -> {
            Company company = entityManager.find(Company.class, 1);
            assertNotNull(company);
            assertThat(company.getLocales()).hasSize(2);
        });
    }

    @Test
    void save() {
        Company company = Company.builder()
                .name("Apple")
                .locales(Map.of(
                        "ru", "Apple описание",
                        "en", "Apple description"
                ))
                .build();
        entityManager.persist(company);
        assertNotNull(company.getId());
    }
}