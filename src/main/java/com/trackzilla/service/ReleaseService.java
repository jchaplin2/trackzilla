package com.trackzilla.service;


import com.trackzilla.entity.Release;

import java.util.List;

public interface ReleaseService {
    List<Release> listReleases();
    Release save(Release release);
    void delete(long id);
}


