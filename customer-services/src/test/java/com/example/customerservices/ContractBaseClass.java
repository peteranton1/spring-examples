package com.example.customerservices;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest(classes = CustomerServicesApplication.class)
@RunWith(SpringRunner.class)
public class ContractBaseClass {

    @Autowired
    private CustomerRestController customerRestController;

    @MockBean
    private CustomerRepository customerRepository;

    @Before
    public void before() {
        RestAssuredMockMvc.standaloneSetup(
            customerRestController
        );
        Mockito.when(customerRepository.findAll())
            .thenReturn(Arrays.asList(
                    Customer.builder()
                        .id(1L)
                        .name("Jane")
                        .build(),
                    Customer.builder()
                        .id(2L)
                        .name("Bob")
                        .build()
                )
            );
    }
}
