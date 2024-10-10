package com.kornienko.spring.mapper;

import com.kornienko.spring.database.entity.Company;
import com.kornienko.spring.dto.CompanyReadDto;
import org.springframework.stereotype.Component;

@Component
public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {

    @Override
    public CompanyReadDto map(Company object) {
        return new CompanyReadDto(object.getId(), object.getName());
    }
}
