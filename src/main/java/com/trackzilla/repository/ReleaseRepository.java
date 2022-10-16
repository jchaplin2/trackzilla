package com.trackzilla.repository;

import com.trackzilla.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

    Optional<List<Release>> findByDate(String releaseDate);
    Optional<List<Release>> findByDescriptionContainsIgnoreCase(String description);
}