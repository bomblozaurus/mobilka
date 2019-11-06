package com.example.teamE.users;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User getById(Long id);

    User getByFirstNameAndLastName(String firstName, String lastName);
}
