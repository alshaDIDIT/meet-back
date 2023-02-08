package com.alshadidi.meet.repository;

import com.alshadidi.meet.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findAppUserByEmail(String email);
    Optional<AppUser> findAppUserById(UUID id);
    Boolean existsAppUserByEmail(String email);
}
