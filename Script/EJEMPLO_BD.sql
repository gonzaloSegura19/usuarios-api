
CREATE DATABASE IF NOT EXISTS usuarios_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE usuarios_db;


CREATE TABLE IF NOT EXISTS direcciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_postal VARCHAR(10) NOT NULL,
    asentamiento VARCHAR(255) NOT NULL,
    tipo_asentamiento VARCHAR(100) NOT NULL,
    municipio VARCHAR(255) NOT NULL,
    estado VARCHAR(255) NOT NULL,
    ciudad VARCHAR(255),
    zona VARCHAR(100)
);


CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100),
    correo VARCHAR(255) NOT NULL UNIQUE,
    direccion_id BIGINT,
    CONSTRAINT fk_direccion
        FOREIGN KEY (direccion_id) REFERENCES direcciones(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);


INSERT INTO direcciones (codigo_postal, asentamiento, tipo_asentamiento, municipio, estado, ciudad, zona) VALUES
('03100', 'Del Valle', 'Colonia', 'Benito Juárez', 'Ciudad de México', 'Ciudad de México', 'Centro');

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, direccion_id) VALUES
('Juan', 'Perez', 'Lopez', 'juan.perez@example.com', 1);



INSERT INTO direcciones (codigo_postal, asentamiento, tipo_asentamiento, municipio, estado, ciudad, zona) VALUES
('04000', 'Coyoacán', 'Colonia', 'Coyoacán', 'Ciudad de México', 'Ciudad de México', 'Sur');

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, direccion_id) VALUES
('Maria', 'Garcia', 'Martinez', 'maria.garcia@example.com', LAST_INSERT_ID())