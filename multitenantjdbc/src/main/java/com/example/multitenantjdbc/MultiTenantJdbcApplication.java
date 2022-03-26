package com.example.multitenantjdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@SpringBootApplication
public class MultiTenantJdbcApplication {

  public static void main(String[] args) {
    SpringApplication.run(MultiTenantJdbcApplication.class, args);
  }

  @Bean
  RouterFunction<ServerResponse> routes(JdbcTemplate template) {
    return route()
        .GET("/customers", request -> {
          var results = template
              .query("select * from customer",
                  (rs, rowNum) -> new Customer(
                      rs.getInt("id"),
                      rs.getString("name")));
          return ServerResponse.ok().body(results);
        }).build();
  }
}

@Configuration
class DataSourceConfiguration {

  @Bean
  @Primary
  DataSource multitenantDataSource(Map<String, DataSource> dataSources) {
    var prefix = "ds";
    var map = dataSources
        .entrySet()
        .stream()
        .filter(e -> e.getKey().startsWith(prefix))
        .collect(Collectors.toMap(
            e -> (Object) Integer.parseInt(
                e.getKey().substring(prefix.length())),
            e -> (Object) e.getValue()
        ));
    map.forEach((tenantId, ds) -> {
      var initializer = new ResourceDatabasePopulator(
          new ClassPathResource("schema.sql"),
          new ClassPathResource(prefix + tenantId + "-data.sql"));
      initializer.execute((DataSource) ds);
      System.out.println("initialized database " + tenantId);
    });
    var mds = new MultitenantDataSource();
    mds.setTargetDataSources(map);
    return mds;
  }

  @Bean
  DataSource ds1() {
    return dataSource(1543);
  }

  @Bean
  DataSource ds2() {
    return dataSource(2543);
  }

  private static DataSource dataSource(int port) {
    var dsp = new DataSourceProperties();
    dsp.setDriverClassName("org.postgresql.Driver");
    dsp.setPassword("pw");
    dsp.setUsername("user");
    dsp.setUrl("jdbc:postgresql://localhost:" + port + "/user");
    return dsp.initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }
}

@Configuration
class SecurityConfiguration {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(auth ->
            auth.anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  UserDetailsService userDetailsService() {
    var rob = createUser("rwinch", 1543);
    var josh = createUser("jlong", 2543);
    var users = Stream.of(josh, rob)
        .collect(Collectors.toMap(User::getUsername, u -> u));
    return username -> {
      var user = users.getOrDefault(
          username, null
      );
      if (user == null) {
        throw new UsernameNotFoundException(
            "couldn't find : " + username);
      }
      return user;
    };
  }

  private static User createUser(String name, Integer tenantId) {
    Collection<GrantedAuthority> authorities =
        List.of(new SimpleGrantedAuthority("USER"));
    return new MultitenantUser(name, "pw",
        true, true,
        true, true,
        authorities, tenantId);
  }

}

class MultitenantUser extends User {

  private final Integer tenantId;

  public Integer getTenantId() {
    return tenantId;
  }

  public MultitenantUser(String username, String password,
                         boolean enabled,
                         boolean accountNonExpired,
                         boolean credentialsNonExpired,
                         boolean accountNonLocked,
                         Collection<? extends GrantedAuthority> authorities,
                         Integer tenantId) {
    super(username, password, enabled, accountNonExpired,
        credentialsNonExpired, accountNonLocked, authorities);
    this.tenantId = tenantId;
  }
}

class MultitenantDataSource extends AbstractRoutingDataSource {

  private final AtomicBoolean initialized = new AtomicBoolean();

  @Override
  protected DataSource determineTargetDataSource() {
    if (this.initialized.compareAndSet(false, true)) {
      this.afterPropertiesSet();
    }
    return super.determineTargetDataSource();
  }

  @Override
  protected Object determineCurrentLookupKey() {
    var authentication = SecurityContextHolder
        .getContext().getAuthentication();
    if (authentication != null &&
        authentication.getPrincipal() instanceof
            MultitenantUser user) {
      var tenantId = user.getTenantId();
      System.out.println("the tenant id is " + tenantId);
      return tenantId;
    }
    return null;
  }
}

record Customer(Integer id, String name) {

}