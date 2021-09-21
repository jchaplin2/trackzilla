package com.trackzilla.service;

import com.trackzilla.entity.Application;
import com.trackzilla.exception.ApplicationNotFoundException;
import com.trackzilla.repository.ApplicationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    // TODO: https://www.javaguides.net/2019/07/spring-data-jpa-save-findbyid-findall-deletebyid-example.html

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public List<Application> listApplications() {
        return  applicationRepository.findAll();
    }

    @Override
    public Application findApplication(long id) {
        Optional<Application> optionalApplication = applicationRepository.findById(id);

        if(optionalApplication.isPresent())
            return optionalApplication.get();
        else
            throw new ApplicationNotFoundException("Application Not Found");
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




}
