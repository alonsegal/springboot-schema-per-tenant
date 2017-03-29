# springboot-schema-per-tenant
Seed project for achieving multi-tenancy (single pooled schema-per-tenant) using SpringBoot and Hibernate as proposed in [this article](https://dzone.com/articles/spring-boot-hibernate-multitenancy-implementation). 

This project assumes a dedicated MySql DB is reachable (can be configured in application.properties), which has a default schema named `default_schema` and at least one additional schema with some tenant name you choose.

The default schema has a single table called `user_tenant_relation` and has the following structure:

```
CREATE TABLE `user_tenant_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `tenant` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_tenent_uq` (`username`,`tenant`)
);
```

The JWT boiler-plate code is removed from to keep it clear and as simple as possible.