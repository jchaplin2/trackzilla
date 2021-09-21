package com.trackzilla.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ApplicationUnitTest {

    @Test
    public void testGetters(){
        Application application = new Application("application1", "Jim Smith", "first application");

        assert(application.getName()).equals("application1");
        assert(application.getOwner()).equals("Jim Smith");
        assert(application.getDescription()).equals("first application");
    }

    @Test
    public void testGettersAndSetters(){
        Application application = new Application();

        application.setName("application2");
        application.setOwner("Jane Smith");
        application.setDescription("second application");

        assert(application.getName()).equals("application2");
        assert(application.getOwner()).equals("Jane Smith");
        assert(application.getDescription()).equals("second application");
    }

}
