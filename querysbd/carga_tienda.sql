INSERT INTO productes(nombre, precio, stock, fecha_inicial_catalogo, fecha_final_catalogo) values
    ('Salchichas', 3, 100, '28/03/2022', '28/12/2022'),
    ('Manzana', 0.99,  20, '28/03/2022', '28/11/2022'),
    ('Sal',     6.99,  30, '28/03/2022', '28/03/2028'),
    ('Agua',    0.59, 150, '28/03/2022', '28/10/2022'),
    ('Queso',      5,  20, '28/03/2022', '28/05/2022'),
    ('Tomate',  0.95,  75, '28/03/2022', '28/03/2022'),
    ('Pan',        1,  50, '28/03/2022', '28/06/2022'),
    ('Pastel',    10,   5, '28/03/2022', '28/08/2022'),
    ('Platano', 3.99,  20, '28/03/2022', '28/08/2022'),
    ('Melon',   7.50,  10, '28/03/2022', '28/06/2022');

INSERT INTO pack(nombre, precio, stock, fecha_inicial_catalogo, fecha_final_catalogo, dto) values 
    ('Salchi_pan',        3, 30, '28/03/2022', '28/12/2022', 1),
    ('Pastel_manzana',   15,  2, '28/03/2022', '28/11/2022', 3),
    ('Macedonia',         8,  5, '28/03/2022', '28/06/2022', 1),
    ('Queso_tomate',   7.50, 20, '28/03/2022', '28/11/2022', 5),
    ('Salchi_tomate',     5, 10, '28/03/2022', '28/07/2022', 2),
    ('Pan_queso',      6.30, 15, '28/03/2022', '28/06/2022', 7),
    ('Pan_melon',      7.99, 20, '28/03/2022', '28/12/2022', 9),
    ('Agua_manzana',      2, 16, '28/03/2022', '28/08/2022', 2),
    ('Agua_sal',          3, 20, '28/03/2022', '28/06/2022', 5),
    ('Pastel_platano',   13,  3, '28/03/2022', '28/04/2022', 2);

INSERT INTO productes_pack(id_pack, id_producte) values
    --Salchi_pan
    (11, 1),
    (11, 7),
    --Pastel_manzana
    (12, 8),
    (12, 2),
    --macedonia
    (13, 2),
    (13, 9),
    (13, 10),
    --Queso_tomate
    (14, 5),
    (14, 6),
    --Salchi_tomate
    (15, 1),
    (15, 6),
    --Pan_queso
    (16, 7),
    (16, 5),
    --Pan_melon
    (17, 7),
    (17, 10),
    --Agua_manzana
    (18, 4),
    (18, 2),
    --Agua_sal
    (19, 4),
    (19, 3),
    --Pastel_platano
    (20, 8),
    (20, 9);

INSERT INTO cliente (dni, nombre, apellidos, fecha_nacimiento, email, telefonos, direccion) values 
    ('456789128E', 'Ellaine', 'Spaulding', '31/03/1974', 'spaul@gmail.com', array['+34 661341185', '+34 610393302'], ROW('Sabadell', 'Barcelona', '35300', 'av. explanada')),
    ('128963978H', 'Nubia', 'Gil', '24/02/1979', 'nubihm@gmail.com', array['+34 669737846', '+34 669737456'], ROW('Madrid', 'Agost', '03698', 'Paraguay')),
    ('132714825I', 'Pablo', 'Iglesias', '22/05/1948', 'tisis@gmail.com', array['+34 733433894', '+34 733433487'], ROW('Salamanca', 'Salamanca', '45400', 'Urzáiz')),
    ('195748632K', 'Milba', 'Perez', '27/02/1978', 'mibac@gmail.com', array['+34 639177285', '+34 639159285'], ROW('Alaoir', 'Andalucia', '07730', 'Boriñaur')),
    ('357896564F', 'Geden', 'Núñez', '19/07/1966', 'gedens@gmail.com', array['+34 779091893', '+34 779091945'], ROW('Badia del valles', 'Barcelona','08214', 'mediterraneo')),
    ('915456132L', 'Meliodas', '', '01/01/0001', 'meldra@gmail.com', array['+34 643987123'], ROW('Liones', 'Bernia', '01466', 'Boar hat')),
    ('142695478B', 'Bart', 'Simpson', '23/03/1983', 'bart.s@gmail.com', array['+34 779081963', '+34 779091945'], ROW('Spriengfield', 'Spriengfield','22214', 'Evergreen Terrace')),
    ('586326245Y', 'Clark', 'ken', '29/02/1996', 'superman@gmail.com', array['+34 666091893', '+34 779091945'], ROW('Badia del valles', 'Barcelona','08214', 'mediterraneo')),
    ('659847251A', 'Peter', 'Parker', '08/04/1992', 'spiderman@gmail.com', array['+34 779091893', '+34 779091945'], ROW('Nueva York', 'Nueva York','08214', 'queens')),
    ('456326478S', 'Oscar', 'Adell', '01/06/2001', 'oade@gmail.com', array['+34 666543678'], ROW('Badia del valles', 'Barcelona','08214', 'Cantabrico'));

INSERT INTO provedor (dni, nombre, apellidos, fecha_nacimiento, email, telefonos, direccion) values 
    ('456719128E', 'Manuel', 'Garcia', '31/05/1984', 'mangar@gmail.com', array['+34 733433894', '+34 610393302'], row('Sabadell', 'Barcelona', '35300', 'av. explanada')),
    ('138963968H', 'Daniel', 'Gil', '24/02/1979', 'danigil@gmail.com', array['+34 669737846', '+34 669737456'], row('Madrid', 'Agost', '03698', 'Paraguay')),
    ('132714425I', 'Pablo', 'Motos', '22/05/1948', 'pablomo@gmail.com', array['+34 779091893', '+34 666543678'], row('Sabadell', 'Barcelona', '35300', 'av. explanada')),
    ('195748532K', 'Maria', 'Perez', '27/02/1978', 'mariape@gmail.com', array['+34 639177285', '+34 639159285'], row('Alaoir', 'Andalucia', '07730', 'Boriñaur')),
    ('357395564F', 'Juan', 'Núñez', '19/07/1966', 'juanu@gmail.com', array['+34 779091893', '+34 779091945'], row('Badia del valles', 'Barcelona','08214', 'mediterraneo')),
    ('915456232L', 'Jose', 'Ortiz', '01/01/0001', 'josort@gmail.com', array['+34 610393302'], row('Salamanca', 'Salamanca', '45400', 'Urzáiz')),
    ('142695178B', 'Marco', 'Polo', '23/03/1983', 'marcopolo@gmail.com', array['+34 779081963', '+34 779091945'], row('Spriengfield', 'Spriengfield','22214', 'Evergreen Terrace')),
    ('586326245Y', 'Elena', 'Nito', '29/02/1996', 'elenanito@gmail.com', array['+34 666091893', '+34 779091945'],row('Badia del valles', 'Barcelona','08214', 'mediterraneo')),
    ('659947251A', 'Sergio', 'Diaz', '08/04/1992', 'serdi@gmail.com', array['+34 779091893', '+34 669737846'], row('Nueva York', 'Nueva York','08214', 'queens')),
    ('456326498S', 'Oscar', 'Adell', '01/06/2001', 'oade@gmail.com', array['+34 666543678'], row('Badia del valles', 'Barcelona','08214', 'Cantabrico'));

INSERT INTO asistencia (fecha_entrada, fecha_salida) VALUES 
    ('2022-03-28 07:00:00' ,null),
    ('2022-03-28 08:59:46' ,'2022-03-28 19:00:00'),
    ('2022-03-28 19:00:00' ,null),
    ('2022-03-28 19:00:00' ,null),
    ('2022-03-28 17:00:00' ,'2022-03-28 22:00:00'),
    ('2022-03-28 00:13:42' ,'2022-03-28 07:00:00'),
    ('2022-03-28 10:00:00' ,'2022-03-28 13:00:00'),
    ('2022-03-28 10:00:00' ,null),
    ('2022-03-28 17:00:00' ,'2022-03-28 00:00:00'),
    ('2022-03-28 12:00:00' ,'2022-03-28 18:00:00');

SELECT * from persona;
SELECT * from producto;
SELECT * from pack;
SELECT * from productos_pack;
SELECT * from asistencia;