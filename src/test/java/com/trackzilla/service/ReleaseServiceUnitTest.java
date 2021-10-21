package com.trackzilla.service;

import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;
import com.trackzilla.repository.ApplicationRepository;
import com.trackzilla.repository.ReleaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ReleaseServiceUnitTest {
    @Mock
    private ReleaseRepository releaseRepository;

    @InjectMocks
    private ReleaseServiceImpl releaseService;

    @Test
    public void listReleases(){
        ArrayList<Release> listOfReleases = new ArrayList<Release>();

        listOfReleases.add(new Release( "John Smith", "first application"));
        listOfReleases.add(new Release( "Jane Smith", "second application"));
        listOfReleases.add(new Release( "Pat Smith", "third application"));

        Mockito.when(releaseService.listReleases()).thenReturn(listOfReleases);

        assertThat(releaseService.listReleases().size()).isEqualTo(3);
    }
}
