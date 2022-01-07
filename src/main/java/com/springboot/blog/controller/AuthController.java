package com.springboot.blog.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.model.Role;
import com.springboot.blog.model.User;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.vo.JWTAuthResponse;
import com.springboot.blog.vo.LoginDto;
import com.springboot.blog.vo.SignUpDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Auth controller exposes signin and signup rest api")
@RestController
@RequestMapping({ "/api/v1/auth" })
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider tokenProvider;

  @ApiOperation(value = "REST API to Register or Signup user to Blog app")
  @PostMapping("/signin")
  public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameorEmail(), loginDto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    // get token form tokenProvider
    String token = tokenProvider.generateToken(authentication);

    return ResponseEntity.ok(new JWTAuthResponse(token));

  }

  @ApiOperation(value = "REST API to Signin or Login user to Blog app")
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {

    // add check for username exists in a DB
    if (userRepository.existsByUsername(signUpDto.getUsername())) {
      return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
    }

    // add check for email exists in DB
    if (userRepository.existsByEmail(signUpDto.getEmail())) {
      return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
    }

    // create user object
    User user = User.builder().name(signUpDto.getName()).username(signUpDto.getUsername()).email(signUpDto.getEmail())
        .password(passwordEncoder.encode(signUpDto.getPassword())).build();

    Role roles = roleRepository.findByName("ROLE_ADMIN").get();
    user.setRoles(Collections.singleton(roles));

    userRepository.save(user);

    return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

  }
}
