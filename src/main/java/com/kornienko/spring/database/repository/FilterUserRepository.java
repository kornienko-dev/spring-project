package com.kornienko.spring.database.repository;

import com.kornienko.spring.database.entity.Role;
import com.kornienko.spring.database.entity.User;
import com.kornienko.spring.dto.PersonalInfo;
import com.kornienko.spring.dto.UserFilter;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilter userFilter);

    List<PersonalInfo> findAllByCompanyIdAndRole(Integer companyId, Role role);

    void updateCompanyAndRole(List<User> users);

    void updateCompanyAndRoleNamed(List<User> users);
}
