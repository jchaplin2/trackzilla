package com.trackzilla.actuator;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HealthIndicator implements org.springframework.boot.actuate.health.HealthIndicator {

    @Value("https://jsonplaceholder.typicode.com/users")
    String downStreamUrl;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public Health health() {
        try {
            ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(downStreamUrl, JsonNode.class);
            if(responseEntity.getStatusCode().is2xxSuccessful()){
                return Health.up().build();
            } else {
                return Health.down().build();
            }
        } catch(Exception e) {
            return Health.down().withException(e).build();
        }
    }

}
