package com.trackzilla.service;

import com.trackzilla.entity.Application;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    List<Application> listApplications();
    Optional<Application> findApplication(long id);
    void delete(long id);
    Application save(Application app);
}
