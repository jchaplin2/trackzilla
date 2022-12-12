package com.trackzilla.service;

import com.trackzilla.repository.UserRepository;
import com.trackzilla.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findByUsername(username);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}

		return UserDetailsImpl.build(user.get());
	}

}
