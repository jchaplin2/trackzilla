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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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

    @Test
    public void findApplication(){
        Long appid = new Long(1);

        Optional<Release> app = Optional.of(
                new Release("new app", "01/01/1900")
        );

        when(releaseRepository.findById(appid)).thenReturn(app);

        assertThat(releaseService.findRelease(appid)).isNotEmpty();
    }

    @Test
    public void deleteApplication(){

        Long appid = new Long(1);

        doNothing().when(releaseRepository).deleteById(appid);

        releaseService.delete(appid);

        verify(releaseRepository, times(1)).deleteById(appid);
    }


    @Test
    public void saveApplication(){
        Release release = new Release("new app", "01/01/1900");

        when(releaseRepository.save(release)).thenReturn(release);

        release = releaseService.save(release);
        assertThat(release).isNotNull();
    }

}
