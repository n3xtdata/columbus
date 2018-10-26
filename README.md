# Columbus


## Overview 
Columbus is an open source monitoring tool.<br>
Columbus ist able to execute queries across various jdbc compatible database systems and compares those results.

For example:<br>
We want to check if an [Oracle database] table has the same amount of rows like a [SQLite database] table:


## Getting Started

### Installation 

``` shell 
curl -L https://github.com/n3xtdata/columbus/tree/artifacts/columbus-0.0.1-SNAPSHOT.jar

```

### Setup Connections 

### First Check

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
  - mail@example.com
```
## To start using Columbus
##### Please wait for the first official release:
```
$ yet to come!
```

After installation, the columbus-home variable and directory have to be created and set:
```
$ mkdir $HOME/columbus
$ export COLUMBUS_HOME $HOME/columbus
```
In order to run checks on databases, you need to store the necessary jdbc-connection files as shown below.
Create your own checks and store those .yml files in the previously set $COLUMBUS_HOME/checks directory.
##### Columbus-Home filesystem hierarchy:
```
$HOME/columbus
      │
      └──-connections
      │   │   
      │   └───jdbc
      │   │   │   oracleDbs.yml
      │   │   │   hiveDbs.yml
      │   │   │   ...
      │   │
      │   └───ssh
      │       │   server-a.yml
      │       │   server-b.yml
      │       │   ...
      │   
      └───checks
          │   check1.yml
          │   check2.yml
          │
          └───project-1
          │   │   firstCheck.yml
          │   │   ...
          │
          └───project-2
          ...
```

## Support
If you need support, please feel free to contact us!

[Oracle database]: https://www.oracle.com/database/technologies/index.html
[SQLite database]: https://www.sqlite.org/