package com.trackzilla.repository;

import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ReleaseRepositoryUnitTest {

    @Autowired
    private ReleaseRepository releaseRepository;

    @BeforeEach
    public void setup(){
        Release release = new Release("first release", "01/01/1900");
        releaseRepository.save(release);
    }

    @Test
    public void findByIdTest(){
        Long id = new Long("1");
        assertThat(releaseRepository.findById(id)).isNotNull();
    }

    @Test
    public void testFindByReleaseDate(){
        List<Release> releases = releaseRepository.findByReleaseDate("01/01/1900");
        assertThat(releases).isNotNull();
        assertThat(Integer.valueOf(releases.size())).isEqualTo(1);
    }

}
