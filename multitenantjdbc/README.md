Multi Tenant JDBC 
--

This project is an example of creating a spring boot application which is multi-tenant, meaning there are multiple 
databases involved. 

This project is derived from an example by Josh Long.

# Running

## Start POSTGRES instances

Run commands in db-env/scripts

    ./postgress-start.sh ds1 1543
    ./postgress-start.sh ds2 2543

if your setup requires it, you can precede the script call with sudo
e.g.

    sudo ./postgress-start.sh ds1 1543
    sudo ./postgress-start.sh ds2 2543

You can log into each running database (ports are 1543 and 2543)
e.g.

    $ PGPASSWORD=pw psql -U user -h localhost -p 1543 user
    psql (14.2 (Ubuntu 14.2-1.pgdg20.04+1))
    Type "help" for help.
    
    user=# \d
    Did not find any relations.
    user=# \q
    
    $ PGPASSWORD=pw psql -U user -h localhost -p 2543 user
    psql (14.2 (Ubuntu 14.2-1.pgdg20.04+1))
    Type "help" for help.
    
    user=# \d
    Did not find any relations.
    user=# \q

# Running the Spring Boot app

Run the spring boot application using the IDE in the main application class.

If all is setup correctly it should run without problems. 

If there are errors it will most likely be due to the database containers not running. 

Once you run the application, the databases will have the 
schema.sql and ds1-data.sql etc scripts applied, so there 
will be a customer table with some data in each database. 

    $ PGPASSWORD=pw psql -U user -h localhost -p 2543 user
    psql (14.2 (Ubuntu 14.2-1.pgdg20.04+1))
    Type "help" for help.
    
    user=# \d
    List of relations
    Schema |      Name       |   Type   | Owner
    --------+-----------------+----------+-------
    public | customer        | table    | user
    public | customer_id_seq | sequence | user
    (2 rows)
    
    user=# select * from customer;
    id |  name   
    ----+---------
    1 | Juergen
    2 | Josh
    (2 rows)
    
    user=# \q
    
    $ PGPASSWORD=pw psql -U user -h localhost -p 1543 user
    psql (14.2 (Ubuntu 14.2-1.pgdg20.04+1))
    Type "help" for help.
    
    user=# select * from customer;
    id | name  
    ----+-------
    1 | Dave
    2 | Yuxin
    3 | Rob
    (3 rows)
    
    user=# \q

## Test the application endpoints

The /customers endpoint will return the list of customers for the database for the user given. 

In this application, you supply the user in the web request. In a production grade
application you would set things up more securely, but this is just a demo.

There are two users defined : 

    jlong
    rwinch

Both have password "pw". 

Using curl, we would get back the right results like this:

    $ curl -u jlong:pw http://localhost:8080/customers
    [{"id":1,"name":"Juergen"},{"id":2,"name":"Hexen"}]
    
    $ curl -u rwinch:pw http://localhost:8080/customers
    [{"id":1,"name":"Dave"},{"id":2,"name":"Yuxin"}]

This demonstrates that the user supplied will determine which 
database is queried.

## Stop the databases

You can check the databases are running using the docker ps command
e.g.
    
    $ sudo docker ps
    [sudo] password for webuser:
    CONTAINER ID   IMAGE             COMMAND                  CREATED       STATUS       PORTS                                       NAMES
    873ac5162d8c   postgres:latest   "docker-entrypoint.s…"   2 hours ago   Up 2 hours   0.0.0.0:2543->5432/tcp, :::2543->5432/tcp   ds2-postgres
    d89575028746   postgres:latest   "docker-entrypoint.s…"   2 hours ago   Up 2 hours   0.0.0.0:1543->5432/tcp, :::1543->5432/tcp   ds1-postgres
    $ 
    
To stop them, use the docker kill command

    $ sudo docker kill 873ac5162d8c
    873ac5162d8c
    $ sudo docker kill d89575028746
    d89575028746
    $ sudo docker ps
    CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
    $ 
    
