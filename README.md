# QMTfypBackEnd

Final Year Project - Qur'an Memorisation Tool Java Dropwizard REST-API Application

How to set up the database locally
---
1. Download MySQLWorkbench
2. Create a basic connection
3. From, the route directory go to the `resources` folder and run all of the SQL Scripts. They should be run in order of the Create Database, then the create table scripts, then the create trigger scripts and finally the insert scripts.
4. Then create the necessary environment variables to access the database with the names: `DB_USERNAME`, `DB_USERNAME`, `DB_PASSWORD`, `DB_NAME` (`DB_NAME` = QMT)
5. Then have the MySQL server active on your machine.
---

How to start the QMTfypBackEnd application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/QMTfypBackend-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
