package com.example.springjdbc.Controller;


import com.example.springjdbc.DTO.LoginRequest;
import com.example.springjdbc.DTO.RegisterRequest;
import com.example.springjdbc.Entity.Role;
import com.example.springjdbc.Entity.User;
import com.example.springjdbc.Repository.RoleRepository;
import com.example.springjdbc.Repository.UserRepository;
import com.example.springjdbc.Service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody RegisterRequest Request) {

        Set<Role> roles = Request.getRoleNames().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

//
//        Role role = roleRepository.findByName(Request.getRoleName())
//                .orElseThrow(() -> new RuntimeException("Role not found: " + Request.getRoleName()));

        User user = new User();
        user.setUsername(Request.getUsername());
        user.setPassword(passwordEncoder.encode(Request.getPassword())); // encrypt password
        user.setEnabled(true);
        user.setRoles(roles);

        userRepository.save(user);

//        UserResponse response = new UserResponse();
//        response.setId(user.getId());
//        response.setUsername(user.getUsername());
//        response.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
//
//        return response;



        return user;
    }


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest user){
        System.out.println(user);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if(authentication.isAuthenticated()){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtService.generateToken(userDetails);
        }
        return "failure";
    }
}
