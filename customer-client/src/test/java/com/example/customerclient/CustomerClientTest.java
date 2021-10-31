package com.example.customerclient;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerClientApplication.class)
//@AutoConfigureWireMock(port = 8081)
@AutoConfigureStubRunner(
    ids = "com.example:customer-service:+:8081"
)
public class CustomerClientTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerClient client;

    @Test
    public void clientShouldReturnAllCustomers() throws Exception {

/*
        WireMock.stubFor(
            get(urlEqualTo("/customers"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                    )
                    .withBody(
                        jsonForCustomer(
                            new Customer(1L, "Jane"),
                            new Customer(2L, "Bob")
                        )
                    )
                )
        );
*/

        Collection<Customer> customers = client.getAllCustomers();
        BDDAssertions
            .then(customers)
            .size()
            .isEqualTo(2);
        BDDAssertions
            .then(customers.iterator().next().getId())
            .isEqualTo(1L);
        BDDAssertions
            .then(customers.iterator().next().getName())
            .isEqualTo("Jane");
    }

    private String jsonForCustomer(
        Customer... customers) throws Exception {
        List<Customer> customerList = Arrays.asList(customers);
        return objectMapper.writeValueAsString(customerList);
    }
}
