package com.example.customerservices;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment =
    SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CustomerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void shouldReturnAllCustomers() throws Exception {

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

        mockMvc
            .perform(get("/customers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("@.[0].id")
                .value(1L))
            .andExpect(jsonPath("@.[0].name")
                .value("Jane"))
            .andExpect(content()
                .contentType(MediaType.APPLICATION_JSON))
        ;
    }
}
