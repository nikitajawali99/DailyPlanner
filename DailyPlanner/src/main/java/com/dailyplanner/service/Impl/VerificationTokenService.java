package com.dailyplanner.service.Impl;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dailyplanner.entity.User;
import com.dailyplanner.entity.VerificationToken;
import com.dailyplanner.repository.UserRepository;
import com.dailyplanner.repository.VerificationTokenRepository;
import com.dailyplanner.service.IVerificationTokenService;


@Service
public class VerificationTokenService implements IVerificationTokenService{
	
	private final VerificationTokenRepository tokenRepository;
	private final UserRepository userRepository;

	public VerificationTokenService(VerificationTokenRepository tokenRepository,UserRepository userRepository) {
		super();
		this.tokenRepository = tokenRepository;
		this.userRepository=userRepository;
	}

	@Override
	public String validateToken(String token) {
		
		Optional<VerificationToken> theToken = tokenRepository.findByToken(token);
		
		if(theToken.isEmpty()) {
			return "Invalid verification token";
		}
		User user = theToken.get().getUser();
		Calendar calendar = Calendar.getInstance();
		if(theToken.get().getExpirationTime().getTime()-calendar.getTime().getTime()<=0) {
			
			return "expired";
			
		}
		user.setEnabled(true);
		userRepository.save(user);
		
		return "valid";
	}

	@Override
	public void saveVerificationTokenForUser(User user, String token) {
		
		var verificationToken = new VerificationToken(token, user);
		tokenRepository.save(verificationToken);
		
	}

	@Override
	public Optional<VerificationToken> findByToken(String token) {
		
		return tokenRepository.findByToken(token);
	}

}
