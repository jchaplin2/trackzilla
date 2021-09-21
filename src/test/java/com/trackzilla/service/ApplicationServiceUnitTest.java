package com.trackzilla.service;

import com.trackzilla.entity.Application;
import com.trackzilla.repository.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
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

        Mockito.when(applicationService.listApplications()).thenReturn(listOfApplications);

        assertThat(applicationService.listApplications().size()).isEqualTo(3);
    }
}
