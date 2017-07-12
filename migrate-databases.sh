#!/bin/bash

set -x

mvn flyway:migrate -Dflyway.configFile=etc/flyway/properties/cvdb-dev.properties
mvn flyway:migrate -Dflyway.configFile=etc/flyway/properties/cvdb-test.properties