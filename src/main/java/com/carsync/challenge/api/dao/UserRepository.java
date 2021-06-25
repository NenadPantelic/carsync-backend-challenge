package com.carsync.challenge.api.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.carsync.challenge.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
