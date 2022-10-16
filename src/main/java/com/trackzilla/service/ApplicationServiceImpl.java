package com.trackzilla.service;

import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;
import com.trackzilla.exception.ResourceNotFoundException;
import com.trackzilla.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public List<Application> listApplications() {
        return  applicationRepository.findAll();
    }

    @Override
    public Optional<Application> findApplication(long id) {
        Optional<Application> application = applicationRepository.findById(id);

        return application;
    }

    public boolean deleteApplicationById(Long id) {
        applicationRepository.deleteById(id);
        Optional<Application> app = applicationRepository.findById(id);
        if(app.isEmpty()){
            return true;
        }

        return false;
    }

    @Override
    public void delete(long id) {
        applicationRepository.deleteById(id);
    }


    @Override
    public Application save(Application app) {
        return applicationRepository.save(app);
    }

    public Optional<List<Application>> findByDescription(String description) {
        return applicationRepository.findByDescriptionContainsIgnoreCase(description);
    }


}
