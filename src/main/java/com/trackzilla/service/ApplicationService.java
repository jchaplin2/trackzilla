package com.trackzilla.service;

import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    List<Application> listApplications();
    Optional<Application> findApplication(long id);
    Optional<List<Application>> findByDescription(String desc);
    void delete(long id);
    Application save(Application app);
}
