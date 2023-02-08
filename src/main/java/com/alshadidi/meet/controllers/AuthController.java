package com.alshadidi.meet.controllers;

import com.alshadidi.meet.models.dto.login.LoginRequestDTO;
import com.alshadidi.meet.models.dto.VerifyUserDTO;
import com.alshadidi.meet.models.dto.signup.SignUpRequestDTO;
import com.alshadidi.meet.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000"}, methods = {
        RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET
})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("verify")
    public VerifyUserDTO verifyUser(@RequestParam String token) { return authService.verifyUser(token); }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.loginUser(
                loginRequestDTO.email(),
                loginRequestDTO.password()
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        return authService.signUp(
                signUpRequestDTO.email(),
                signUpRequestDTO.password(),
                signUpRequestDTO.roles()
        );
    }


}
