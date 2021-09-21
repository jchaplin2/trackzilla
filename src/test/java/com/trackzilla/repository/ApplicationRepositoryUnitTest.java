package com.trackzilla.repository;

import com.trackzilla.entity.Application;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ApplicationRepositoryUnitTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @BeforeEach
    public void setup(){
        Application myApp = new Application("application1", "Jane Smith", "first application");
        applicationRepository.save(myApp);
    }

    @Test
    public void findByIdTest(){
        Long id = new Long("1");
        assertThat(applicationRepository.findById(id)).isNotNull();
    }

    @Test
    public void testFindByOwner(){
        List<Application> apps = applicationRepository.findByOwner("Jane Smith");
        System.out.println(apps.size());
        assertThat(apps).isNotNull();
        assertThat(Integer.valueOf(apps.size())).isEqualTo(1);
    }

    @Test
    public void testFindByDescription(){
        List<Application> apps = applicationRepository.findByDescription("first application");
        System.out.println(apps.size());
        assertThat(apps).isNotNull();
        assertThat(Integer.valueOf(apps.size())).isEqualTo(1);
    }
}
