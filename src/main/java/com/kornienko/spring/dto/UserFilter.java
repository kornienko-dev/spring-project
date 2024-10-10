package com.kornienko.spring.dto;

import java.time.LocalDate;

public record UserFilter(String firstname, String lastname, LocalDate birthDate) {

}
