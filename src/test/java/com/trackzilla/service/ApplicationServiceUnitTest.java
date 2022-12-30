package com.trackzilla.service;

import com.trackzilla.entity.Application;
import com.trackzilla.repository.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ApplicationServiceUnitTest {
    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    public void listApplications(){

        ArrayList<Application> listOfApplications = new ArrayList<Application>();

        listOfApplications.add(new Application("application1", "John Smith", "first application"));
        listOfApplications.add(new Application("application2", "Jane Smith", "second application"));
        listOfApplications.add(new Application("application3", "Pat Smith", "third application"));

        when(applicationRepository.findAll()).thenReturn(listOfApplications);

        assertThat(applicationService.listApplications().size()).isEqualTo(3);
    }

    @Test
    public void findApplication(){
        Long appid = new Long(1);

        Optional<Application> app = Optional.of(
                new Application("new app", "John Smith", "first application")
        );

        when(applicationRepository.findById(appid)).thenReturn(app);

        assertThat(applicationService.findApplication(appid)).isNotEmpty();
    }

    @Test
    public void deleteApplication(){

        Long appid = new Long(1);

        doNothing().when(applicationRepository).deleteById(appid);

        applicationService.delete(appid);

        verify(applicationRepository, times(1)).deleteById(appid);
    }

    @Test
    public void deleteApplicationById(){

        Long appid = new Long(1);

        doNothing().when(applicationRepository).deleteById(appid);

        applicationService.deleteApplicationById(appid);

        verify(applicationRepository, times(1)).deleteById(appid);
    }

    @Test
    public void saveApplication(){
        Application app = new Application("new app", "John Smith", "first application");

        when(applicationRepository.save(app)).thenReturn(app);

        app = applicationService.save(app);
        assertThat(app).isNotNull();
    }
}
