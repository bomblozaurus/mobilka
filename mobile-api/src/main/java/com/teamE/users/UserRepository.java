package com.teamE.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User getById(Long id);

    User getByEmail(String email);

    User getByFirstNameAndLastName(String firstName, String lastName);


}
