io-netty-http
=============
Current application is represented as a server, what implemented on netty framework and using mySQL server as the database. 
The application provides: an http request receiving on specified port, query processing and transmission of http page to the client.
Application depending on the user's URI: writes << hello >> (somedomain/hello); redirects to the specified page (somedomain/redirect?uri=<page_address>);
returns http page with following information - the total number of requests , the number of unique URI, URI table and the number of requests for it, a table with IPs addresses and number of requests and date,
information on the last 16 requests (src_ip, URI, timestamp,  sent_bytes, received_bytes, speed (bytes/sec)).
 
Establishing of HTTP Server
1. Http server Requires mySQL server to save information about requests;
1.1 Install mySQL server and create user with username and password;
1.2 Create database ( for example "NettyBase");
1.3 Create table (for example Table), enter in mySql console CREATE TABLE <Table_Name>(id_request INT(10) PRIMARY KEY AUTO_INCREMENT, ip_add TINYTEXT, uri TINYTEXT, time_stamp DATE, send_bytes INT(10), received_bytes INT(10), speed INT(10), state BOOLEAN);
1.4 Configure config.xml. Write in <user> username, <password> password, <database> name of created database, <table> name of created table;
2. Set as a classpath external libraries /mysql-connector-java-5.1.22-bin.jar and /netty-all-4.0.10.Final.jar
3. Start with java class ServerHTTP: 8080, where 8080 port, or *.jar. Also you can run with IDE.
