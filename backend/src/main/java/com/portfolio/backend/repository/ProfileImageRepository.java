package com.portfolio.backend.repository;

import com.portfolio.backend.model.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findFirstByActiveTrueOrderByUploadedAtDesc();

    @Modifying
    @Query("UPDATE ProfileImage p SET p.active = false WHERE p.active = true")
    void deactivateAll();
}
