package com.trackzilla.entity;

import org.junit.Test;

public class ReleaseUnitTest {

    @Test
    public void testGetters(){
        Release release = new Release("first release", "01/01/1900");

        assert(release.getDescription()).equals("first release");
        assert(release.getDate()).equals("01/01/1900");
    }

    @Test
    public void testGettersAndSetters(){
        Long id = Long.parseLong("1");
        Release release = new Release();

        release.setId(id);
        release.setDescription("first release");
        release.setDate("01/01/1900");

        assert(release.getId()).equals(id);
        assert(release.getDescription()).equals("first release");
        assert(release.getDate()).equals("01/01/1900");
    }

}
