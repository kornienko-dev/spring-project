package com.kornienko.spring.validation.impl;

import com.kornienko.spring.database.repository.CompanyRepository;
import com.kornienko.spring.dto.AbstractDto;
import com.kornienko.spring.validation.UserInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
public class UserInfoValidator implements ConstraintValidator<UserInfo, AbstractDto> {

    private final CompanyRepository companyRepository;

    @Override
    public boolean isValid(AbstractDto value, ConstraintValidatorContext context) {
        return hasText(value.getFirstname()) || hasText(value.getLastname());
    }
}
