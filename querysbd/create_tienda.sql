drop table if exists productes_pack;
drop table if exists pack;
drop table if exists productes;
drop table if exists cliente;
drop table if exists proveedor;
drop table if exists persona;
drop type if exists direccion;
drop table if exists asistencia;

CREATE TABLE productes(
	id SERIAL PRIMARY KEY,
	nombre VARCHAR(20) NOT NULL,
	precio DECIMAL(5,2) NOT NULL,
    stock INTEGER NOT NULL,
    fecha_inicial_catalogo DATE NOT NULL,
    fecha_final_catalogo DATE NOT NULL,

    constraint chk_precio CHECK(precio>0),
	constraint chk_stock CHECK(stock>=0),
	constraint chk_catalogo CHECK(fecha_final_catalogo>=fecha_inicial_catalogo)
);

CREATE TABLE pack(
    dto INTEGER NOT NULL,
    unique (id),
	constraint chk_dto CHECK(dto>=0 and dto<=100)
)INHERITS (productes);

CREATE TABLE productes_pack(
    id_pack INTEGER,
    id_producte INTEGER,
    PRIMARY KEY(id_pack,id_producte),
    CONSTRAINT fk_id_pack  FOREIGN KEY(id_pack) REFERENCES pack(id),
    CONSTRAINT fk_id_producte  FOREIGN KEY(id_producte) REFERENCES productes(id)
);

CREATE TYPE tipo_direccion AS(
	localidad VARCHAR(30),
    provincia VARCHAR(30),
    cod_postal VARCHAR(5),
    domicilio VARCHAR(30)
);

CREATE TABLE persona(
    id SERIAL PRIMARY KEY,
	dni VARCHAR(10) NOT NULL,
	nombre VARCHAR(20) NOT NULL,
	apellidos VARCHAR(40) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    email VARCHAR(50) NOT NULL,
    telefonos VARCHAR(15) ARRAY NULL,
	direccion tipo_direccion NULL
);

CREATE TABLE provedor(
)INHERITS (persona);

CREATE TABLE cliente(
)INHERITS (persona);

CREATE TABLE asistencia(
    id SERIAL PRIMARY KEY,
    fecha_entrada TIMESTAMP NOT NULL,
    fecha_salida TIMESTAMP NULL
);
