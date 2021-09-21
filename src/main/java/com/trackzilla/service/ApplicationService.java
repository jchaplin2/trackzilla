package com.trackzilla.service;

import com.trackzilla.entity.Application;

import java.util.List;

public interface ApplicationService {
    List<Application> listApplications();
    Application findApplication(long id);
    void delete(long id);
    Application save(Application app);
}
