package com.trackzilla.repository;

import com.trackzilla.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

    List<Release> findByDate(String releaseDate);
}