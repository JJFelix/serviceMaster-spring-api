package com.felix.serviceMaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felix.serviceMaster.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByEmail(String email);

    // Boolean presentByEmail(String email);

}
