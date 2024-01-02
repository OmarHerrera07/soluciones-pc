-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.11.5-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para soluciones_pc
DROP DATABASE IF EXISTS `soluciones_pc`;
CREATE DATABASE IF NOT EXISTS `soluciones_pc` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci */;
USE `soluciones_pc`;


DROP USER IF EXISTS 'soluciones_pc'@'localhost';
CREATE USER soluciones_pc@localhost IDENTIFIED BY '123456';
GRANT INSERT ON soluciones_pc.* TO 'soluciones_pc'@'localhost';
GRANT EXECUTE ON soluciones_pc.* TO 'soluciones_pc'@'localhost';
GRANT UPDATE ON soluciones_pc.* TO 'soluciones_pc'@'localhost';
GRANT SELECT ON soluciones_pc.* TO 'soluciones_pc'@'localhost';
GRANT EVENT ON soluciones_pc.* TO 'soluciones_pc'@'localhost'; 
-- Volcando estructura para procedimiento soluciones_pc.actualizarPagos
DROP PROCEDURE IF EXISTS `actualizarPagos`;
DELIMITER //
CREATE PROCEDURE `actualizarPagos`(IN clienteId INT)
BEGIN
    -- Variables para almacenar la fecha actual y la nueva fecha de pago
    DECLARE fechaActual DATE;
    DECLARE nuevaFechaPago DATE;
    DECLARE paquete INT;

    -- Obtener la fecha actual
    SET fechaActual = CURDATE();

    -- Obtener la fecha de pago actual para el cliente
    SELECT fecha_pago INTO nuevaFechaPago
    FROM cliente
    WHERE id_cliente = clienteId;
    
    SELECT id_paquete INTO paquete
    FROM cliente
    WHERE id_cliente = clienteId;
    
     -- Insertar en la tabla meses_pago
    INSERT INTO meses_pago (fecha, id_cliente,id_paquete,fecha_pago)
    VALUES (nuevaFechaPago, clienteId,paquete,CURDATE());


    -- Actualizar el campo 'ultimo_pago'
    UPDATE cliente
    SET ultimo_pago = nuevaFechaPago
    WHERE id_cliente = clienteId;

    -- Aumentar un mes a la fecha de pago
    UPDATE cliente
    SET fecha_pago = DATE_ADD(nuevaFechaPago, INTERVAL 1 MONTH)
    WHERE id_cliente = clienteId;
END//
DELIMITER ;

-- Volcando estructura para tabla soluciones_pc.cliente
DROP TABLE IF EXISTS `cliente`;
CREATE TABLE IF NOT EXISTS `cliente` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(10) NOT NULL,
  `observaciones` text DEFAULT NULL,
  `fecha_pago` date NOT NULL,
  `rfc` varchar(13) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `id_paquete` int(11) NOT NULL,
  `id_colonia` int(11) NOT NULL,
  `fecha_alerta` date NOT NULL,
  `dias_atraso` tinyint(4) NOT NULL,
  `coordenadas` varchar(250) NOT NULL,
  `ultimo_pago` date DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `id_cliente_UNIQUE` (`id_cliente`),
  KEY `fk_cliente_paquete_internet1_idx` (`id_paquete`),
  KEY `fk_cliente_colonia1_idx` (`id_colonia`),
  CONSTRAINT `fk_cliente_colonia1` FOREIGN KEY (`id_colonia`) REFERENCES `colonia` (`id_colonia`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_cliente_paquete_internet1` FOREIGN KEY (`id_paquete`) REFERENCES `paquete_internet` (`id_paquete`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Volcando datos para la tabla soluciones_pc.cliente: ~5 rows (aproximadamente)
DELETE FROM `cliente`;
INSERT INTO `cliente` (`id_cliente`, `nombre`, `telefono`, `observaciones`, `fecha_pago`, `rfc`, `estado`, `id_paquete`, `id_colonia`, `fecha_alerta`, `dias_atraso`, `coordenadas`, `ultimo_pago`) VALUES
	(1, 'Omar Herrera Santos', '6161071750', 'fallosSAFJNL{SEFBJAERWRUHJKQP3RIOGKQ3R2412¿\'\'¿12¿\'?¡l:ñ"3123Ñ.1.231{23\n\n123\n\n124\n124\n12Ñ4124L\n124L{Ñ12LÑ3\n', '2026-04-14', 'HESO001007E50', 1, 1, 1, '2010-01-12', 0, 'nFMKSDFMKSDFMK', '2026-03-14'),
	(2, 'Gadiel salazar', '6161074312', 'No ha pagado >:v', '2024-08-20', 'fskn3923j', 1, 2, 1, '2023-12-15', 0, 'rtuyrtruruyryuyt', '2024-07-20'),
	(3, 'Prueba Herrera Santos', '432432', 'No ha pagado', '2024-02-15', 'fskn3923j', 1, 1, 1, '2023-12-15', 0, 'fdsfdsf', '2024-01-15'),
	(4, 'Prueba Herrera Santos', '432432', '', '2023-12-14', 'fskn3923j', 1, 1, 1, '2023-12-14', 0, 'fdsfdsf', '2023-12-14'),
	(6, 'Prueba Herrera Santos', '432432', '', '2023-12-22', 'fskn3923j', 0, 1, 1, '2023-12-22', 0, 'fdsfdsf', '2023-12-22');

-- Volcando estructura para tabla soluciones_pc.colonia
DROP TABLE IF EXISTS `colonia`;
CREATE TABLE IF NOT EXISTS `colonia` (
  `id_colonia` int(11) NOT NULL AUTO_INCREMENT,
  `colonia` varchar(45) NOT NULL,
  PRIMARY KEY (`id_colonia`),
  UNIQUE KEY `id_colonia_UNIQUE` (`id_colonia`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Volcando datos para la tabla soluciones_pc.colonia: ~0 rows (aproximadamente)
DELETE FROM `colonia`;
INSERT INTO `colonia` (`id_colonia`, `colonia`) VALUES
	(1, 'Nueva Era');

-- Volcando estructura para tabla soluciones_pc.meses_pago
DROP TABLE IF EXISTS `meses_pago`;
CREATE TABLE IF NOT EXISTS `meses_pago` (
  `id_meses_pago` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `id_paquete` int(11) DEFAULT NULL,
  `fecha_pago` date DEFAULT NULL,
  PRIMARY KEY (`id_meses_pago`),
  UNIQUE KEY `id_meses_pago_UNIQUE` (`id_meses_pago`),
  KEY `fk_meses_pago_cliente1_idx` (`id_cliente`),
  KEY `meses_pago_FK` (`id_paquete`),
  CONSTRAINT `fk_meses_pago_cliente1` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `meses_pago_FK` FOREIGN KEY (`id_paquete`) REFERENCES `paquete_internet` (`id_paquete`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Volcando datos para la tabla soluciones_pc.meses_pago: ~0 rows (aproximadamente)
DELETE FROM `meses_pago`;
INSERT INTO `meses_pago` (`id_meses_pago`, `fecha`, `id_cliente`, `id_paquete`, `fecha_pago`) VALUES
	(16, '2025-12-14', 1, 1, '2024-01-02'),
	(17, '2026-01-14', 1, 1, '2024-01-02'),
	(18, '2026-02-14', 1, 1, '2024-01-02'),
	(19, '2024-04-20', 2, 2, '2024-01-02'),
	(20, '2026-03-14', 1, 1, '2024-01-02'),
	(21, '2024-05-20', 2, 2, '2024-01-02'),
	(22, '2024-06-20', 2, 2, '2024-01-02'),
	(23, '2024-07-20', 2, 2, '2024-01-02'),
	(24, '2024-01-15', 3, 1, '2024-01-02');

-- Volcando estructura para tabla soluciones_pc.pago
DROP TABLE IF EXISTS `pago`;
CREATE TABLE IF NOT EXISTS `pago` (
  `id_pago` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) NOT NULL,
  `fecha` date DEFAULT NULL,
  `recibo` blob DEFAULT NULL,
  `id_usuario` int(11) DEFAULT NULL,
  `total` float NOT NULL,
  PRIMARY KEY (`id_pago`),
  KEY `recibo_FK` (`id_cliente`),
  KEY `recibo_FK_1` (`id_usuario`),
  CONSTRAINT `recibo_FK` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`),
  CONSTRAINT `recibo_FK_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Volcando datos para la tabla soluciones_pc.pago: ~1 rows (aproximadamente)
DELETE FROM `pago`;
INSERT INTO `pago` (`id_pago`, `id_cliente`, `fecha`, `recibo`, `id_usuario`, `total`) VALUES
	(1, 1, '2024-01-02', NULL, 19, 760),
	(2, 2, '2024-01-02', NULL, 19, 960),
	(3, 3, '2024-01-02', NULL, 19, 190);

-- Volcando estructura para tabla soluciones_pc.paquete_internet
DROP TABLE IF EXISTS `paquete_internet`;
CREATE TABLE IF NOT EXISTS `paquete_internet` (
  `id_paquete` int(11) NOT NULL AUTO_INCREMENT,
  `precio` float NOT NULL,
  PRIMARY KEY (`id_paquete`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Volcando datos para la tabla soluciones_pc.paquete_internet: ~2 rows (aproximadamente)
DELETE FROM `paquete_internet`;
INSERT INTO `paquete_internet` (`id_paquete`, `precio`) VALUES
	(1, 190),
	(2, 240);

-- Volcando estructura para tabla soluciones_pc.rol
DROP TABLE IF EXISTS `rol`;
CREATE TABLE IF NOT EXISTS `rol` (
  `id_rol` tinyint(4) NOT NULL AUTO_INCREMENT,
  `rol` varchar(45) NOT NULL,
  PRIMARY KEY (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Volcando datos para la tabla soluciones_pc.rol: ~2 rows (aproximadamente)
DELETE FROM `rol`;
INSERT INTO `rol` (`id_rol`, `rol`) VALUES
	(1, 'Administrador'),
	(2, 'Caja');

-- Volcando estructura para tabla soluciones_pc.usuario
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(200) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `id_rol` tinyint(4) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `id_usuario_UNIQUE` (`id_usuario`),
  KEY `fk_usuario_rol_idx` (`id_rol`),
  CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Volcando datos para la tabla soluciones_pc.usuario: ~11 rows (aproximadamente)
DELETE FROM `usuario`;
INSERT INTO `usuario` (`id_usuario`, `username`, `password`, `nombre`, `id_rol`) VALUES
	(1, 'gadi61253', '$argon2id$v=19$m=4096,t=3,p=1$kdwYK5UqJFweix8WsuI+AK6ZUC4p3ozNK3ut8enIGq0$r3OPHMgdeXcJnzpwzy2cLgdt/oAMjRwA5tyFGQRqXEI', 'Gadiel Salazar ', 1),
	(2, 'omar', '$argon2id$v=19$m=4096,t=3,p=1$vx+uezpsCDseuFURERJH2YYs+CSOE1R8aVmf1ZKfyig$kuwGOQa3clM+UajpTipZ0GbNcjx3GsrrbG0mbvUHeww', 'Herrera', 1),
	(3, 'Gadiel', '$argon2id$v=19$m=4096,t=3,p=1$iZCR3zugaPH9v7JcQ/Wt42LiwV6yBaHEd60eepUluHw$p1SBJS5xtyuqmmR9/WzlOtzv2K7Jh+/g6el4PhXr/38', 'Salazar', 1),
	(17, 'Joshua', '$argon2id$v=19$m=4096,t=3,p=1$yHbHhwHrosVp/iEP9r5GKEKkEjowDnYcJ3abfJDmDiw$qS2Bb+gTYP5JgLW3sqfueHsHeHyygfxxpyaJZyRTHTA', 'Salazar', 1),
	(18, 'Joshua2', '$argon2id$v=19$m=4096,t=3,p=1$K7WTDNKNrz4fNcp/zz0j94Z/Eh/ehr6MEeKQiGkUNsc$at4w/3+QpFVvmf5AdZaU6rYx1KDqvBYUHVOjUulbbVw', 'Salazar', 1),
	(19, 'walls', '$argon2id$v=19$m=4096,t=3,p=1$Ohs+eQlFCYEXqhgGMn+6Y5f2XbxMSRukoXalKn0i04I$No/sdAIstm+avlu791+WFh/dQAddKTQQYK6pyQOp8cM', 'Omar Herrera ', 1),
	(20, 'Fausto2', 'dasdsa', 'Faustoc', 1),
	(21, 'Fausto2', 'dasfds', 'Fausto sfdsf', 1),
	(22, 'AnaMariala', '$argon2id$v=19$m=4096,t=3,p=1$9qDBPMQEipwDWJwYukLd4cx3W2A1ydNRWDryOuHviQg$s1Oe8MKt4xNTgYM4WXkkF+tAM2BFISNco+Pcwd30Z3w', 'Ana Mariela ', 2),
	(23, 'Angel12', '$argon2id$v=19$m=4096,t=3,p=1$qjQWWGVN5aMmb4hrtKO5SyrJH+FrTbQD+hARAquHEG8$AeCpJi8D6yCNdMv803TidLnePlyJIcikXzRD1jjB1Yw', 'Angel Herrera Santos', 2),
	(24, 'gadiel123', '$argon2id$v=19$m=4096,t=3,p=1$PmAY1CD9xBObu+4YSe1ZbyBOPx4IqdxfeQ4yWCuM/hs$kJY75neAcfHt8AoUAfygCHu59z2lVT7Lzobs7GhgOQ4', 'gadiel salazar ', 2);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
