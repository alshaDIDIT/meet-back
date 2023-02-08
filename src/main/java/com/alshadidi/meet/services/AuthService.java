package com.alshadidi.meet.services;

import com.alshadidi.meet.models.AppUser;
import com.alshadidi.meet.models.dto.login.LoginResponseDTO;
import com.alshadidi.meet.models.dto.VerifyUserDTO;
import com.alshadidi.meet.models.enums.ERole;
import com.alshadidi.meet.payload.response.MessageResponse;
import com.alshadidi.meet.repository.AppUserRepository;
import com.alshadidi.meet.security.jwt.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;

    public AuthService(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AppUserRepository appUserRepository
    ) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.appUserRepository = appUserRepository;
    }

    public VerifyUserDTO verifyUser(String token) { return jwtUtil.parseToken(token); }

    public ResponseEntity<?> loginUser(String email, String password) {
        AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(email);

        if (passwordEncoder.matches(password, appUser.getPassword())) {
            return ResponseEntity.ok(
                    new LoginResponseDTO(
                            appUser.getId(),
                            appUser.getUsername(),
                            jwtUtil.generateToken(appUser),
                            appUser.getRoles()
                    )
            );
        }
        return ResponseEntity.status(401).body("Username or password is incorrect");
    }

    public ResponseEntity<?> signUp(String email, String password, Set<ERole> roles) {
        if (appUserRepository.existsAppUserByEmail(email))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email is already in use"));

        AppUser user = new AppUser(
                email,
                passwordEncoder.encode(password)
        );

        Set<ERole> userRoles = new HashSet<>();

        if (roles == null) userRoles.add(ERole.USER);

        roles.forEach(role -> {
            switch (role) {
                case ADMIN -> roles.add(ERole.ADMIN);
                case MOD -> roles.add(ERole.MOD);
                default -> roles.add(ERole.USER);
            }
        });

        user.setRoles(roles);
        appUserRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User successfully registered"));
    }

}
