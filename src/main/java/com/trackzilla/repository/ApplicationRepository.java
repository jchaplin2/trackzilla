package com.trackzilla.repository;

import com.trackzilla.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    //TODO : https://attacomsian.com/blog/spring-data-jpa-query-annotation
    //TODO : https://www.netsurfingzone.com/jpa/spring-data-jpa-named-parameters/

    @Query(value = "select app from Application app where app.owner = :name")
    List<Application> findByOwner(@Param("name") String name);

    //need @Column annotation to do this.
    List<Application> findByDescription(String description);

}
