package com.example.jpa;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.stream.Stream;

@Log4j2
@Component
public class JpaApplicationWriter implements ApplicationRunner {

    private final EntityManager em;
    private final TransactionTemplate transactionTemplate;
    private final CustomerRepository customerRepository;

    public JpaApplicationWriter(EntityManager em,
                                TransactionTemplate transactionTemplate, CustomerRepository customerRepository) {
        this.em = em;
        this.transactionTemplate = transactionTemplate;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        customerRepository.deleteAll();

        transactionTemplate.execute(status -> {

            // create some test data
            Stream.of("Dave,Syer;Phil,Webb;Mark,Fisher"
                            .split(";"))
                    .map(name -> name.split(","))
                    .forEach(tpl -> this.em.persist(new Customer(
                            null, tpl[0], tpl[1], new HashSet<>()
                    )));

            // create a query
            TypedQuery<Customer> customers = this.em
                    .createQuery(
                            "select c from Customer c ",
                            Customer.class
                    );

            // Display list of customers
            customers.getResultList()
                    .forEach(customer -> log.info(
                            "typed query result: " +
                                    ToStringBuilder
                                            .reflectionToString(customer)
                    ));

            return null;
        });

        transactionTemplate.execute(x -> {
            customerRepository.findAll().forEach(customer -> {

                int countOfOrders = (int) (Math.random() * 5) + 1;

                for (int i = 0; i < countOfOrders; i++) {
                    customer.getOrders()
                            .add(new Order(null, "sku_" + i));
                    customerRepository.save(customer);
                }

            });
            return null;
        });

        transactionTemplate.execute(x -> {
            log.info("------------------------");
            customerRepository.findByFirstAndLast("Dave", "Syer")
                    .forEach(dave -> log.info(ToStringBuilder
                            .reflectionToString(dave)));

            log.info("------------------------");
            customerRepository.byFullName("Phil", "Webb")
                    .forEach(dave -> log.info(ToStringBuilder
                            .reflectionToString(dave)));

            log.info("------------------------");
            customerRepository.orderSummary().forEach(summary ->
                    log.info(summary.getSku() + " has " +
                            summary.getCount() + " instances."));

            return null;
        });

        customerRepository.byFullName("Dave", "Syer")
                .forEach(dave -> {
                    dave.setFirst("David");
                    customerRepository.save(dave);
                });

        customerRepository.byFullName("David", "Syer")
                .forEach(david -> {
                    Long id = david.getId();
                    customerRepository.findRevisions(id)
                            .forEach(revision ->
                                    log.info("revision " +
                                            ToStringBuilder.reflectionToString(
                                                    revision.getMetadata())
                                            + " for entity " +
                                            ToStringBuilder.reflectionToString(
                                                    revision.getEntity())
                                    )
                            );
                });
    }
}
