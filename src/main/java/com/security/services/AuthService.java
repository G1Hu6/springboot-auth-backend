package com.security.services;

import com.security.dto.LogInDto;
import com.security.dto.LoginResponseDto;
import com.security.dto.SignUpDto;
import com.security.dto.UserDto;
import com.security.entities.UserEntity;
import com.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;

    public UserDto signUpUser(SignUpDto signUpDto){

        Optional<UserEntity> userEntity = userRepository.findByEmail(signUpDto.getEmail());

        if(userEntity.isPresent()){
            throw new BadCredentialsException("User is already exists by email : " + signUpDto.getEmail());
        }
        UserEntity toBeSavedUser = modelMapper.map(signUpDto, UserEntity.class);
        toBeSavedUser.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        UserEntity savedUser = userRepository.save(toBeSavedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public LoginResponseDto logInUser(LogInDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        return new LoginResponseDto(userEntity.getId(), accessToken, refreshToken);
    }

    public LoginResponseDto refresh(String refreshToken){
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        UserEntity user = userService.getUserById(userId);

        // Generate new access token
        String newAccessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDto(userId, newAccessToken, refreshToken);
    }
}
