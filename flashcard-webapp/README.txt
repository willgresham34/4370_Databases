Flashi - Term Project - By: Connor Stephens, Adam Wright, Will Gresham

Contributions:

Adam Wright: ddl.sql, perf.txt, security.txt, Models, SetServices, FolderService, AccountService
Connor Stephens: prelim.pdf, db design pdf, queries.sql, FolderService, SetService 
Will Gresham: Project Init, data seeding, datasource.txt, and db init bash, DTOs, all front end and controllers

Technologies used:
Spring Boot java
Mustache
Bcrypt
Mysql
CSS
Bash (db_init  script)
Python (createCards script)

Server name: mysql-server-4370
Database name: flashcards_db
Database username: root
Database password: mysqlpass


Demo Users:

Username    | Password
--------------------------------
"aliceA12"  | "password"
"bobB34"    | "password"
"carolC45"  | "password"


Where to find files:
Most of our submission files are in the Submission_files folder however all of
the sql related files are in the sql folder such as our ddl.sql, queries.sql, and data.sql.

How to run: 
Make sure docker instance is running.
In base folder "flashcard-webapp" running the follow command in terminal
mvn spring-boot:run 
open http://localhost:8080 to view site