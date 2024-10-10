package com.kornienko.spring.dto;

import lombok.Value;

import java.time.LocalDate;

public record PersonalInfo(String firstname, String lastname, LocalDate birthDate) {

}
