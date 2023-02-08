package com.alshadidi.meet.models.dto.signup;

import com.alshadidi.meet.models.enums.ERole;

import java.util.Set;

public record SignUpRequestDTO(String email, String password, Set<ERole> roles) {}
