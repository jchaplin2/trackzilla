package com.trackzilla.service;


import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;

import java.util.List;
import java.util.Optional;

public interface ReleaseService {
    List<Release> listReleases();
    Optional<Release> findRelease(long id);
    Release save(Release release);
    void delete(long id);
}


