package com.kornienko.spring.integration.service;

import com.kornienko.spring.config.DatabaseProperties;
import com.kornienko.spring.dto.CompanyReadDto;
import com.kornienko.spring.integration.annotation.IT;
import com.kornienko.spring.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = ApplicationRunner.class, initializers = ConfigDataApplicationContextInitializer.class)
@IT
@RequiredArgsConstructor
class CompanyServiceIT {

    private static final Integer COMPANY_ID = 1;

    private final CompanyService companyService;
    private final DatabaseProperties databaseProperties;

    @Test
    void findById() {
        Optional<CompanyReadDto> actualResult = companyService.findById(COMPANY_ID);

        assertTrue(actualResult.isPresent());
        CompanyReadDto expectedResult = new CompanyReadDto(COMPANY_ID, null);
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }
}
