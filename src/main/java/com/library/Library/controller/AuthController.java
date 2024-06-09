package com.library.Library.controller;


import com.library.Library.dto.*;
import com.library.Library.entity.Role;
import com.library.Library.exception.UserNotFoundException;
import com.library.Library.repository.RoleRepository;
import com.library.Library.repository.UserRepository;
import com.library.Library.security.JwtGenerator;
import com.library.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.library.Library.entity.LibraryUser;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private JwtGenerator jwtGenerator;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtGenerator jwtGenerator,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return new ResponseEntity<>(ResponseDTO.builder().message("Username is already taken!").build(), HttpStatus.BAD_REQUEST);
        } else {
            LibraryUser user = new LibraryUser();
            user.setUsername(registerDTO.getUsername());
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            Role role = roleRepository.findByName("ADMIN").get();
            user.setRoles(Collections.singletonList(role));

            userRepository.save(user);
            return new ResponseEntity<>(ResponseDTO.builder().message("User registered successfully!").build(), HttpStatus.OK);
        }
    }


    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("HELLO");
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(authentication.isAuthenticated());
            String token = jwtGenerator.generateToken(authentication);
            Long userId = userService.getUserIdByUsername(loginDTO.getUsername());
            return new ResponseEntity<>(AuthResponseDTO.builder().tokenType("Bearer ").accessToken(token).message("Success").userId(userId).build(), HttpStatus.OK);

        }catch (AuthenticationException e){
            return new ResponseEntity<>(AuthResponseDTO.builder().message("Bad credentials").build(), HttpStatus.BAD_REQUEST);
        }
    }

}
