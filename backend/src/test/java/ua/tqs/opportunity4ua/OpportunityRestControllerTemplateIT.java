package ua.tqs.opportunity4ua;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ua.tqs.opportunity4ua.entity.Opportunity;
import ua.tqs.opportunity4ua.enums.OpportunityStatus;
import ua.tqs.opportunity4ua.repository.OpportunityRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class OpportunityRestControllerTemplateIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OpportunityRepository repository;

    @AfterEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Disabled
    @Test
    void whenCreateOpportunity_thenItIsPersisted() {
        // given
        Opportunity op = new Opportunity();
        op.setTitle("Volunteering Event");
        op.setDescription("Help at conference");

        // when
        ResponseEntity<Opportunity> response =
                restTemplate.postForEntity("/api/opportunity", op, Opportunity.class);

        // then (HTTP)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // then (DB)
        List<Opportunity> found = repository.findAll();
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("Volunteering Event");
    }
    
    @Disabled
    @Test
    void givenOpportunities_whenGetAll_thenStatus200AndListReturned() {
        // given
        repository.save(new Opportunity(null, "Op1", "Desc1", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 30, 0, 10, OpportunityStatus.OPEN, null, null));
        repository.save(new Opportunity(null, "Op2", "Desc2", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 30, 0, 10, OpportunityStatus.OPEN, null, null));

    
        // when
        ResponseEntity<List> response =
                restTemplate.exchange(
                        "/api/opportunity",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        List.class
                );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }
}
