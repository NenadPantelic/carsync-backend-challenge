package com.carsync.challenge.api.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.carsync.challenge.api.model.VerificationToken;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

	Optional<VerificationToken> findBy_token(String token);

	void deleteBy_email(String email);

}
