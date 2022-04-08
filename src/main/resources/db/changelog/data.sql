-- liquibase formatted sql
-- changeset mydb:data

DROP TABLE IF EXISTS mydb.pets;

CREATE TABLE mydb.pets (
   id INT AUTO_INCREMENT  PRIMARY KEY,
   name VARCHAR(250) NOT NULL,
   description VARCHAR(250) NOT NULL,
   breed VARCHAR(250) NOT NULL,
   type VARCHAR(250) NOT NULL,
   price VARCHAR(250) NOT NULL
);

INSERT INTO pets (name, description, breed, type, price) VALUES
 ('Nemo2', 'fish', 'piranah', 'a fricking fich', '$100')

-- rollback DROP TABLE IF EXISTS mydb.pets