package com.alshadidi.meet.models.dto.login;

import com.alshadidi.meet.models.enums.ERole;

import java.util.Set;
import java.util.UUID;

public record LoginResponseDTO(UUID id, String email, String token, Set<ERole> roles) {}
