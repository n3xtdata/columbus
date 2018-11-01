# Columbus

## Overview 
Columbus is an open source monitoring tool.<br>
Columbus ist able to execute queries across various jdbc compatible database systems and compares those results.

For example:<br>
We want to check if an [Oracle database] table has the same amount of rows like a [MySQL database] table.


## Getting Started

### Installation 

Download Columbus

``` shell 
curl -L https://github.com/n3xtdata/columbus/raw/artifacts/columbus-0.0.1-SNAPSHOT.jar -o columbus.jar
```

Setup Environment

``` shell 
export COLUMBUS_HOME=/Users/horst/columbus
export COLUMBUS_MAIL_HOST=example.com
export COLUMBUS_MAIL_USERNAME=name@example.com
export COLUMBUS_MAIL_PASSWORD=password
export COLUMBUS_MAIL_PORT=465
export COLUMBUS_MAIL_SENDER=name@example.com
```

Setup the Columbus Home Directory

``` shell 
mkdir $COLUMBUS_HOME/checks 
mkdir $COLUMBUS_HOME/connections
mkdir $COLUMBUS_HOME/connections/jdbc
mkdir $COLUMBUS_HOME/notifications
```

### Directory Structure

In order to run checks on databases, you need to store the necessary jdbc-connection files as shown below.
Create your own checks and store those .yml files in the previously set $COLUMBUS_HOME/checks directory.

```
users/horst/columbus
            │
            └───connections
            │   │   
            │   └───jdbc
            │       │   mysql.yml
            │       │   oracle.yml
            │       │   ...
            │
            └───checks
            │   │   check1.yml
            │   │   check2.yml
            │   │
            │   └───project-1
            │   │   │   firstCheck.yml
            │   │   │   ...
            │   │
            │   └───project-2
            │    ...
            │
            └───notifications
                │   emailGroups.yml
                │   ...
            
```

### Setup Connections 

Create a new Yaml File "mysql.yml" in $COLUMBUS_HOME/connections/jdbc:

``` yaml 
label: jdbc-mysql
username: admin
password: password
url: jdbc:mysql://localhost:3306/YourDBName
driverClass: com.mysql.jdbc.Driver
driverPath: /Users/horst/drivers/mysql-connector-java-5.1.12.jar
```

Setup a second connection in $COLUMBUS_HOME/connections/jdbc/oracle.yml

``` yaml 
label: jdbc-oracle
username: admin
password: password
url: jdbc:oracle:thin:@myhost:1521:orc
driverClass: oracle.jdbc.OracleDriver
driverPath: /Users/horst/drivers/ojdbc6.jar
```

### Setup Notifications

Create a new Yaml File in $COLUMBUS_HOME/notifications:

``` yaml 
label: groupA
members:
  - a1@example.com
  - a2@example.com
---
label: groupB
members:
  - b1@example.com
```

### First Check

Create your first check in Columbus:

##### example-check.yml
```yaml
label: exampleRowCountComparison
description: this check compares a count of an oracle db table with a count on a mysql db table.
components:
  - label: sourceDb
    type: JDBC
    params:
      connection: jdbc-mysql
      sqlQuery: SELECT COUNT(*) AS cnt FROM customers
  - label: targetDb
    type: JDBC
    params:
      connection: jdbc-oracle
      sqlQuery: SELECT COUNT(*) AS cnt FROM customers
evaluation:
  type: CUSTOM
  params:
    rules:
      - "{sourceDb.cnt} / {targetDb.cnt} > 1 -> ERROR"
      - "{sourceDb.cnt} / {targetDb.cnt} < 0.95 -> ERROR"
      - "{sourceDb.cnt} / {targetDb.cnt} > 0.98 -> SUCCESS"
      - "{sourceDb.cnt} / {targetDb.cnt} > 0.95 -> WARNING"
schedules:
  - type: SIMPLE
    value: 10
notifications:
  ERROR:
    - groupA
    - groupB
  WARNING:
    - groupB
```
## Start Columbus
```
$ java -jar columbus.jar
```



## Support
If you need support, please feel free to contact us!

[Oracle database]: https://www.oracle.com/database/technologies/index.html
[MySQL database]: https://www.mysql.com/de/
