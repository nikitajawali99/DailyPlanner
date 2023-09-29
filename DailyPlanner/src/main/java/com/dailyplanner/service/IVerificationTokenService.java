package com.dailyplanner.service;

import java.util.Optional;

import com.dailyplanner.entity.User;
import com.dailyplanner.entity.VerificationToken;


public interface IVerificationTokenService {
	
String validateToken(String token);
	
	void saveVerificationTokenForUser(User user,String token);
	
	Optional<VerificationToken> findByToken(String token);


}
