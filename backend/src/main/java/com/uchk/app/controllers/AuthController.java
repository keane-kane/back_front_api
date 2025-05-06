package com.uchk.app.controllers;

import com.uchk.app.config.JwtTokenUtil;
import com.uchk.app.dto.LoginRequest;
import com.uchk.app.dto.LoginResponse;
import com.uchk.app.dto.UserDto;
import com.uchk.app.models.User;
import com.uchk.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        System.out.println("Incoming request: " + "jwt" + " " + ".getPrincipal()");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            System.out.println("Incoming request: " + "jwt" + " " + authentication.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
            System.out.println("Incoming request: " + jwt + " " + authentication.getPrincipal());

            User user = userService.findByUsername(loginRequest.getUsername());
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new LoginResponse(jwt, userDto));
        } catch (AuthenticationException e) {
            e.printStackTrace(); // or log
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }



    }
}
