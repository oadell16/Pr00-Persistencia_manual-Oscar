--=======================
--UPDATE
--=======================
UPDATE producto set stock = 10 WHERE nombre like 'Pastel';
UPDATE pack set dto = 5 WHERE nombre like 'Pastel_platano';
UPDATE asistencia set fecha_salida = '2022-03-28 17:00:00' WHERE id = 1;
UPDATE cliente SET telefonos = array_append(telefonos,'+34 610393302') where nombre like 'Meliodas';
UPDATE provedor SET direccion = ROW('Badia del valles', 'Barcelona','08215','mediterraneo') where (direccion).localidad like 'Badia del valles';
UPDATE productos_pack set id_producto = 8 where id_pack = 17 AND id_producto = 7;
UPDATE cliente SET apellidos = 'Sin of Wrath' where nombre like 'Meliodas';

--=======================
--SELECT
--=======================
SELECT dni,nombre,apellidos FROM cliente WHERE (direccion).localidad like 'Badia del valles';
SELECT * FROM producto WHERE to_char(fecha_fin, 'month') like 'december';
SELECT * FROM pack WHERE nombre like 'Pastel%';
SELECT * FROM asistencia WHERE fecha_salida IS NULL;
SELECT * FROM provedor WHERE dni like '%45%';
SELECT * FROM persona WHERE to_char(fecha_nacimiento, 'month') like 'march';
--=======================
--JOIN
--=======================
SELECT pack.id, pack.nombre FROM productos_pack pp, pack WHERE pack.id = pp.id_pack AND pp.id_producto=4;
SELECT pack.id, pack.nombre, prod.id, prod.nombre FROM productos_pack pp, pack, producto prod WHERE pack.id = pp.id_pack AND pp.id_producto=prod.id order by pack.id;
SELECT prod.id, prod.nombre FROM productos_pack pp, producto prod WHERE prod.id = pp.id_producto AND pp.id_pack=11;
--=======================
--Subconsulta
--=======================
select id,nombre from pack where id in (select id_pack from productos_pack where id_producto=4);
select id,nombre from producto where id in (select id_producto from productos_pack where id_pack in(select id from pack where nombre like'Agua%'));
select id,nombre from pack where id in (select id_pack from productos_pack where id_producto in(select id from producto where nombre like'Pastel'));

--=======================
--DELETE
--=======================
DELETE FROM pack WHERE dto < 5;
DELETE FROM producto WHERE to_char(fecha_final_catalogo, 'month') like 'march';
DELETE FROM asistencia WHERE fecha_salida is null;
DELETE FROM productos_pack where id_pack = 15;
DELETE FROM cliente where dni like '%45%';
DELETE FROM provedor where (direccion).cod_postal like '08215';
DELETE FROM persona where (direccion).localidad like 'Nueva York';