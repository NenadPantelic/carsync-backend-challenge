package com.carsync.challenge.api.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.carsync.challenge.api.model.TwoFAToken;

@Repository
public interface TwoFARepository extends CrudRepository<TwoFAToken, Long> {

}
