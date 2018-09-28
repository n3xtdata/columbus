# Columbus
Columbus is an open source tool for monitoring.<br>
Our monitoring solution ist able to execute queries across various jdbc compatible database systems and compares those results.

For example:<br>
We want to check if an [Oracle database] table has the same amount of rows like a [SQLite database] table:
##### example-check.yml
```yaml
label: exampleRowCountComparison
description: this check compares a count of an oracle db table with a count on a mysql db table.
components:
  - label: first
    connectionType: jdbc
    connectionLabel: jdbc-sqllite
    command: SELECT COUNT(*) as cnt FROM table
  - label: second
    connectionType: jdbc
    connectionLabel: jdbc-oracle
    command: SELECT COUNT(*) as cnt FROM table
evaluationType: COMPARE
```
## To start using Columbus
##### Please wait for the first official release:
```
$ yet to come!
```

After installation, the columbus-home variable and directory have to be created and set:
```
$ mkdir $HOME/columbus
$ export COLUMBUS $HOME/columbus
```
In order to run checks on databases, you need to store the necessary jdbc-connection files as shown below.
Create your own checks and store those .yml files in the previously set $COLUMBUS/checks directory.
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
## To start developing Columbus
```
$ git clone https://github.com/n3xtdata/columbus
$ cd columbus
$ mvn clean package
```
## Support
If you need support, please feel free to contact us!

[Oracle database]: https://www.oracle.com/database/technologies/index.html
[SQLite database]: https://www.sqlite.org/