CREATE DATABASE file_repo;
CREATE USER file_repo WITH PASSWORD 'file_repo';
GRANT ALL PRIVILEGES ON DATABASE file_repo to file_repo;

CREATE DATABASE file_repo_test;
CREATE USER file_repo_test WITH PASSWORD 'file_repo_test';
GRANT ALL PRIVILEGES ON DATABASE file_repo_test to file_repo_test;