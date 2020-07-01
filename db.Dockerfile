FROM mysql:latest

# To use this initialisation mechanism, datadir (/var/lib/mysql) must not have
# 'mysql' directory.
# Simple way to achive it is to mount volume to datadir.
COPY src/main/resources/sql/mysql/ddl.sql /docker-entrypoint-initdb.d/

# When datadir is empty, mysql asks to set password option.
ENV MYSQL_ALLOW_EMPTY_PASSWORD 1
