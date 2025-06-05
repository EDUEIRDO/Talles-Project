-- MySQL dump 10.13  Distrib 8.0.41, for Linux (x86_64)
--
-- Host: localhost    Database: ifood
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Address`
--

DROP TABLE IF EXISTS `Address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `street` text,
  `district` text,
  `city` text,
  `state` text,
  `houseNumber` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Address`
--

LOCK TABLES `Address` WRITE;
/*!40000 ALTER TABLE `Address` DISABLE KEYS */;
INSERT INTO `Address` VALUES (1,'Rua das Flores, 123','Centro','São Paulo','SP',123),(2,'Av. Paulista, 456','Bela Vista','São Paulo','SP',456),(3,'Rua da Consolação, 789','Consolação','São Paulo','SP',789),(4,'Rua Augusta, 101','Cerqueira César','São Paulo','SP',101),(5,'Praça da Sé, 202','Sé','São Paulo','SP',202),(6,'Rua do Sol, 50','Boa Vista','Recife','PE',50),(7,'Av. Boa Viagem, 1000','Boa Viagem','Recife','PE',1000),(8,'Rua da Aurora, 30','Santo Amaro','Recife','PE',30),(9,'Travessa São Francisco, 15','Centro','Rio de Janeiro','RJ',15),(10,'Rua Voluntários da Pátria, 200','Botafogo','Rio de Janeiro','RJ',200),(11,'Av. Atlântica, 10','Copacabana','Rio de Janeiro','RJ',10),(12,'Rua XV de Novembro, 25','Centro','Curitiba','PR',25),(13,'Rua Comendador Araújo, 300','Batel','Curitiba','PR',300),(14,'Rua das Gaivotas, 5','Jurerê Internacional','Florianópolis','SC',5),(15,'Rua da Praia, 80','Centro','Porto Alegre','RS',80);
/*!40000 ALTER TABLE `Address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Categories`
--

DROP TABLE IF EXISTS `Categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text,
  `description` text,
  `type` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Categories`
--

LOCK TABLES `Categories` WRITE;
/*!40000 ALTER TABLE `Categories` DISABLE KEYS */;
INSERT INTO `Categories` VALUES (1,'Prato Principal',NULL,NULL),(2,'Acompanhamento',NULL,NULL),(3,'Bebida',NULL,NULL),(4,'Sobremesa',NULL,NULL),(5,'Lanche',NULL,NULL),(6,'Pizza',NULL,NULL);
/*!40000 ALTER TABLE `Categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Client`
--

DROP TABLE IF EXISTS `Client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Client` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text,
  `addressId` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_client_address` (`addressId`),
  CONSTRAINT `fk_client_address` FOREIGN KEY (`addressId`) REFERENCES `Address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Client`
--

LOCK TABLES `Client` WRITE;
/*!40000 ALTER TABLE `Client` DISABLE KEYS */;
INSERT INTO `Client` VALUES (1,'João Silva',1),(2,'Maria Souza',6),(3,'Pedro Santos',9),(4,'Ana Costa',12),(5,'Carlos Lima',2),(6,'Fernanda Almeida',7),(7,'Ricardo Gomes',10),(8,'Sandra Rocha',13),(9,'Lucas Carvalho',3),(10,'Patrícia Nunes',8),(11,'Gabriel Mendes',11),(12,'Juliana Dias',14),(13,'Bruno Pereira',4),(14,'Amanda Vieira',15),(15,'Rafael Martins',5);
/*!40000 ALTER TABLE `Client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Product`
--

DROP TABLE IF EXISTS `Product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text,
  `price` double DEFAULT NULL,
  `description` text,
  `productTypeId` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_type_id` (`productTypeId`),
  CONSTRAINT `fk_product_type_id` FOREIGN KEY (`productTypeId`) REFERENCES `Categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Product`
--

LOCK TABLES `Product` WRITE;
/*!40000 ALTER TABLE `Product` DISABLE KEYS */;
INSERT INTO `Product` VALUES (1,'Feijoada Completa',35,'Arroz, farofa, couve, torresmo',1),(2,'Bife à Parmegiana',30,'Arroz, batata frita',1),(3,'Refrigerante Lata',6,'Coca-cola, Pepsi, Guaraná',3),(4,'Água Mineral',4,'Água sem gás 500ml',3),(5,'Pudim de Leite',12,'Pudim cremoso de leite condensado',4),(6,'Batata Frita Grande',15,'Porção generosa de batata frita',2),(7,'Pizza Margherita',45,'Molho de tomate, mussarela, manjericão',6),(8,'Pizza Calabresa',48,'Molho de tomate, mussarela, calabresa',6),(9,'Combo Burger Simples',28,'Hambúrguer, batata P, refri lata',5),(10,'Sushi Salmão (8 un.)',40,'Uramaki de salmão',1),(11,'Temaki Califórnia',25,'Pepino, manga, kani',1),(12,'Esfiha de Carne',7,'Tradicional esfiha aberta de carne',5),(13,'Salada Caesar',22,'Alface, croutons, frango grelhado, molho caesar',1),(14,'Suco de Laranja',10,'Suco natural de laranja',3),(15,'Bolo de Chocolate',18,'Fatia de bolo com calda de chocolate',4),(16,'Arroz',8,'Porção de arroz branco',2),(17,'Purê de Batata',10,'Porção de purê de batata',2),(18,'Cerveja Long Neck',9,'Cerveja em garrafa long neck',3),(19,'Torta de Limão',15,'Fatia de torta de limão com merengue',4),(20,'Hot Roll (4 un.)',28,'Sushi frito com recheio de salmão',1);
/*!40000 ALTER TABLE `Product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Request`
--

DROP TABLE IF EXISTS `Request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Request` (
  `id` int NOT NULL AUTO_INCREMENT,
  `totalValue` double DEFAULT NULL,
  `deliveryValue` double DEFAULT NULL,
  `clientId` int DEFAULT NULL,
  `restaurantId` int DEFAULT NULL,
  `addressDeliveryId` int DEFAULT NULL,
  `statusRequestId` int DEFAULT NULL,
  `typePaymentId` int DEFAULT NULL,
  `statusPaymentId` int DEFAULT NULL,
  `requestDate` datetime DEFAULT NULL,
  `deliveryDate` datetime DEFAULT NULL,
  `deliveryTimeInMinutes` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_client_request` (`clientId`),
  KEY `kf_restaurant_id` (`restaurantId`),
  KEY `fk_address_delivery_id` (`addressDeliveryId`),
  KEY `fk_status_request_id` (`statusRequestId`),
  KEY `fk_type_payment_id` (`typePaymentId`),
  KEY `fk_status_payment_id` (`statusPaymentId`),
  CONSTRAINT `fk_address_delivery_id` FOREIGN KEY (`addressDeliveryId`) REFERENCES `Address` (`id`),
  CONSTRAINT `fk_client_request` FOREIGN KEY (`clientId`) REFERENCES `Client` (`id`),
  CONSTRAINT `fk_status_payment_id` FOREIGN KEY (`statusPaymentId`) REFERENCES `statusPayment` (`id`),
  CONSTRAINT `fk_status_request_id` FOREIGN KEY (`statusRequestId`) REFERENCES `statusRequest` (`id`),
  CONSTRAINT `fk_type_payment_id` FOREIGN KEY (`typePaymentId`) REFERENCES `typePayment` (`id`),
  CONSTRAINT `kf_restaurant_id` FOREIGN KEY (`restaurantId`) REFERENCES `Restaurant` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Request`
--

LOCK TABLES `Request` WRITE;
/*!40000 ALTER TABLE `Request` DISABLE KEYS */;
INSERT INTO `Request` VALUES (1,45,5,1,1,1,4,1,2,'2025-01-05 19:30:00','2025-01-05 20:05:00',35),(2,54,7,5,2,2,4,2,2,'2025-01-07 20:00:00','2025-01-07 20:40:00',40),(3,70,8,9,3,3,4,4,2,'2025-01-10 21:00:00','2025-01-10 21:35:00',35),(4,38,6,1,4,1,4,1,2,'2025-01-12 19:45:00','2025-01-12 20:20:00',35),(5,40,5,6,1,6,4,3,2,'2025-01-15 13:00:00','2025-01-15 13:30:00',30),(6,60,9,2,6,6,4,4,2,'2025-01-18 19:15:00','2025-01-18 20:00:00',45),(7,58,8,7,2,10,4,1,2,'2025-01-20 20:30:00','2025-01-20 21:15:00',45),(8,65,7,10,3,8,4,2,2,'2025-01-22 21:00:00','2025-01-22 21:40:00',40),(9,35,6,3,4,9,4,4,2,'2025-01-25 19:00:00','2025-01-25 19:30:00',30),(10,25,4,11,5,11,4,1,2,'2025-01-28 09:30:00','2025-01-28 10:00:00',30),(11,50,5,1,1,1,4,1,2,'2025-01-06 19:30:00','2025-01-06 20:05:00',35),(12,60,7,5,2,2,4,2,2,'2025-01-08 20:00:00','2025-01-08 20:40:00',40),(13,75,8,9,3,3,4,4,2,'2025-01-11 21:00:00','2025-01-11 21:35:00',35),(14,40,6,1,4,1,4,1,2,'2025-01-13 19:45:00','2025-01-13 20:20:00',35),(15,42,5,6,1,6,4,3,2,'2025-01-16 13:00:00','2025-01-16 13:30:00',30),(16,65,9,2,6,6,4,4,2,'2025-01-19 19:15:00','2025-01-19 20:00:00',45),(17,62,8,7,2,10,4,1,2,'2025-01-21 20:30:00','2025-01-21 21:15:00',45),(18,68,7,10,3,8,4,2,2,'2025-01-23 21:00:00','2025-01-23 21:40:00',40),(19,37,6,3,4,9,4,4,2,'2025-01-26 19:00:00','2025-01-26 19:30:00',30),(20,27,4,11,5,11,4,1,2,'2025-01-29 09:30:00','2025-01-29 10:00:00',30),(21,48,5,1,1,1,4,1,2,'2025-01-05 18:30:00','2025-01-05 19:05:00',35),(22,58,7,5,2,2,4,2,2,'2025-01-07 19:00:00','2025-01-07 19:40:00',40),(23,72,8,9,3,3,4,4,2,'2025-01-10 20:00:00','2025-01-10 20:35:00',35),(24,39,6,1,4,1,4,1,2,'2025-01-12 18:45:00','2025-01-12 19:20:00',35),(25,41,5,6,1,6,4,3,2,'2025-01-15 12:00:00','2025-01-15 12:30:00',30),(26,85,10,2,7,7,4,2,2,'2025-02-03 20:15:00','2025-02-03 20:55:00',40),(27,120,12,8,8,13,4,1,2,'2025-02-05 13:00:00','2025-02-05 13:45:00',45),(28,60,8,12,9,14,4,4,2,'2025-02-08 20:45:00','2025-02-08 21:25:00',40),(29,95,11,4,10,12,4,3,2,'2025-02-10 19:30:00','2025-02-10 20:10:00',40),(30,30,5,13,11,4,4,1,2,'2025-02-13 12:00:00','2025-02-13 12:35:00',35),(31,40,6,14,12,15,4,2,2,'2025-02-15 16:00:00','2025-02-15 16:30:00',30),(32,25,5,15,13,5,4,4,2,'2025-02-18 20:00:00','2025-02-18 20:30:00',30),(33,55,7,1,14,1,4,1,2,'2025-02-20 20:15:00','2025-02-20 20:50:00',35),(34,32,5,5,15,2,4,3,2,'2025-02-22 10:00:00','2025-02-22 10:25:00',25),(35,48,6,9,1,3,4,4,2,'2025-02-25 19:00:00','2025-02-25 19:35:00',35),(36,90,10,2,7,7,4,2,2,'2025-02-04 20:15:00','2025-02-04 20:55:00',40),(37,125,12,8,8,13,4,1,2,'2025-02-06 13:00:00','2025-02-06 13:45:00',45),(38,65,8,12,9,14,4,4,2,'2025-02-09 20:45:00','2025-02-09 21:25:00',40),(39,100,11,4,10,12,4,3,2,'2025-02-11 19:30:00','2025-02-11 20:10:00',40),(40,33,5,13,11,4,4,1,2,'2025-02-14 12:00:00','2025-02-14 12:35:00',35),(41,43,6,14,12,15,4,2,2,'2025-02-16 16:00:00','2025-02-16 16:30:00',30),(42,27,5,15,13,5,4,4,2,'2025-02-19 20:00:00','2025-02-19 20:30:00',30),(43,58,7,1,14,1,4,1,2,'2025-02-21 20:15:00','2025-02-21 20:50:00',35),(44,35,5,5,15,2,4,3,2,'2025-02-23 10:00:00','2025-02-23 10:25:00',25),(45,50,6,9,1,3,4,4,2,'2025-02-26 19:00:00','2025-02-26 19:35:00',35),(46,88,10,2,7,7,4,2,2,'2025-02-03 21:15:00','2025-02-03 21:55:00',40),(47,122,12,8,8,13,4,1,2,'2025-02-05 14:00:00','2025-02-05 14:45:00',45),(48,63,8,12,9,14,4,4,2,'2025-02-08 21:45:00','2025-02-08 22:25:00',40),(49,98,11,4,10,12,4,3,2,'2025-02-10 20:30:00','2025-02-10 21:10:00',40),(50,31,5,13,11,4,4,1,2,'2025-02-13 13:00:00','2025-02-13 13:35:00',35),(51,42,5,3,1,9,4,1,2,'2025-03-01 19:00:00','2025-03-01 19:30:00',30),(52,50,7,4,2,12,4,2,2,'2025-03-03 20:00:00','2025-03-03 20:35:00',35),(53,68,8,1,3,1,4,4,2,'2025-03-05 21:00:00','2025-03-05 21:40:00',40),(54,36,6,5,4,2,4,1,2,'2025-03-07 19:45:00','2025-03-07 20:20:00',35),(55,28,4,10,5,8,4,3,2,'2025-03-10 09:30:00','2025-03-10 10:00:00',30),(56,55,9,11,6,11,4,4,2,'2025-03-12 19:15:00','2025-03-12 20:00:00',45),(57,80,10,13,7,4,4,1,2,'2025-03-15 20:30:00','2025-03-15 21:10:00',40),(58,115,12,14,8,15,4,2,2,'2025-03-17 13:00:00','2025-03-17 13:45:00',45),(59,58,8,15,9,5,4,4,2,'2025-03-20 20:45:00','2025-03-20 21:25:00',40),(60,90,11,6,10,7,4,3,2,'2025-03-22 19:30:00','2025-03-22 20:10:00',40),(61,45,5,3,1,9,4,1,2,'2025-03-02 19:00:00','2025-03-02 19:30:00',30),(62,53,7,4,2,12,4,2,2,'2025-03-04 20:00:00','2025-03-04 20:35:00',35),(63,70,8,1,3,1,4,4,2,'2025-03-06 21:00:00','2025-03-06 21:40:00',40),(64,38,6,5,4,2,4,1,2,'2025-03-08 19:45:00','2025-03-08 20:20:00',35),(65,30,4,10,5,8,4,3,2,'2025-03-11 09:30:00','2025-03-11 10:00:00',30),(66,58,9,11,6,11,4,4,2,'2025-03-13 19:15:00','2025-03-13 20:00:00',45),(67,83,10,13,7,4,4,1,2,'2025-03-16 20:30:00','2025-03-16 21:10:00',40),(68,118,12,14,8,15,4,2,2,'2025-03-18 13:00:00','2025-03-18 13:45:00',45),(69,60,8,15,9,5,4,4,2,'2025-03-21 20:45:00','2025-03-21 21:25:00',40),(70,93,11,6,10,7,4,3,2,'2025-03-23 19:30:00','2025-03-23 20:10:00',40),(71,40,5,3,1,9,4,1,2,'2025-03-01 18:00:00','2025-03-01 18:30:00',30),(72,48,7,4,2,12,4,2,2,'2025-03-03 19:00:00','2025-03-03 19:35:00',35),(73,65,8,1,3,1,4,4,2,'2025-03-05 20:00:00','2025-03-05 20:40:00',40),(74,34,6,5,4,2,4,1,2,'2025-03-07 18:45:00','2025-03-07 19:20:00',35),(75,26,4,10,5,8,4,3,2,'2025-03-10 08:30:00','2025-03-10 09:00:00',30),(76,38,5,7,11,10,4,2,2,'2025-04-02 11:30:00','2025-04-02 12:00:00',30),(77,48,6,1,12,1,4,1,2,'2025-04-04 15:00:00','2025-04-04 15:35:00',35),(78,30,5,5,13,2,4,4,2,'2025-04-07 20:00:00','2025-04-07 20:30:00',30),(79,60,7,9,14,3,4,3,2,'2025-04-09 20:15:00','2025-04-09 20:55:00',40),(80,35,5,2,15,6,4,1,2,'2025-04-12 10:00:00','2025-04-12 10:25:00',25),(81,55,6,8,1,13,4,2,2,'2025-04-14 19:30:00','2025-04-14 20:05:00',35),(82,65,8,12,2,14,4,4,2,'2025-04-17 20:00:00','2025-04-17 20:40:00',40),(83,75,9,4,3,12,4,1,2,'2025-04-19 21:00:00','2025-04-19 21:50:00',50),(84,40,6,13,4,4,4,3,2,'2025-04-22 19:45:00','2025-04-22 20:20:00',35),(85,30,4,14,5,15,4,4,2,'2025-04-25 09:30:00','2025-04-25 10:00:00',30),(86,40,5,7,11,10,4,2,2,'2025-04-03 11:30:00','2025-04-03 12:00:00',30),(87,50,6,1,12,1,4,1,2,'2025-04-05 15:00:00','2025-04-05 15:35:00',35),(88,32,5,5,13,2,4,4,2,'2025-04-08 20:00:00','2025-04-08 20:30:00',30),(89,62,7,9,14,3,4,3,2,'2025-04-10 20:15:00','2025-04-10 20:55:00',40),(90,37,5,2,15,6,4,1,2,'2025-04-13 10:00:00','2025-04-13 10:25:00',25),(91,57,6,8,1,13,4,2,2,'2025-04-15 19:30:00','2025-04-15 20:05:00',35),(92,68,8,12,2,14,4,4,2,'2025-04-18 20:00:00','2025-04-18 20:40:00',40),(93,78,9,4,3,12,4,1,2,'2025-04-20 21:00:00','2025-04-20 21:50:00',50),(94,42,6,13,4,4,4,3,2,'2025-04-23 19:45:00','2025-04-23 20:20:00',35),(95,32,4,14,5,15,4,4,2,'2025-04-26 09:30:00','2025-04-26 10:00:00',30),(96,39,5,7,11,10,4,2,2,'2025-04-02 12:30:00','2025-04-02 13:00:00',30),(97,49,6,1,12,1,4,1,2,'2025-04-04 16:00:00','2025-04-04 16:35:00',35),(98,31,5,5,13,2,4,4,2,'2025-04-07 21:00:00','2025-04-07 21:30:00',30),(99,61,7,9,14,3,4,3,2,'2025-04-09 21:15:00','2025-04-09 21:55:00',40),(100,36,5,2,15,6,4,1,2,'2025-04-12 11:00:00','2025-04-12 11:25:00',25),(101,48,5,15,1,5,4,1,2,'2025-05-01 19:30:00','2025-05-01 20:05:00',35),(102,58,7,14,2,15,4,2,2,'2025-05-03 20:00:00','2025-05-03 20:40:00',40),(103,72,8,13,3,4,4,4,2,'2025-05-06 21:00:00','2025-05-06 21:35:00',35),(104,39,6,12,4,14,4,1,2,'2025-05-08 19:45:00','2025-05-08 20:20:00',35),(105,29,4,11,5,11,4,3,2,'2025-05-11 09:30:00','2025-05-11 10:00:00',30),(106,60,9,10,6,8,4,4,2,'2025-05-13 19:15:00','2025-05-13 20:00:00',45),(107,88,10,8,7,13,4,1,2,'2025-05-16 20:30:00','2025-05-16 21:10:00',40),(108,125,12,7,8,10,4,2,2,'2025-05-18 13:00:00','2025-05-18 13:45:00',45),(109,63,8,6,9,7,4,4,2,'2025-05-21 20:45:00','2025-05-21 21:25:00',40),(110,98,11,5,10,2,4,3,2,'2025-05-23 19:30:00','2025-05-23 20:10:00',40),(111,50,5,15,1,5,4,1,2,'2025-05-02 19:30:00','2025-05-02 20:05:00',35),(112,60,7,14,2,15,4,2,2,'2025-05-04 20:00:00','2025-05-04 20:40:00',40),(113,75,8,13,3,4,4,4,2,'2025-05-07 21:00:00','2025-05-07 21:35:00',35),(114,41,6,12,4,14,4,1,2,'2025-05-09 19:45:00','2025-05-09 20:20:00',35),(115,31,4,11,5,11,4,3,2,'2025-05-12 09:30:00','2025-05-12 10:00:00',30),(116,62,9,10,6,8,4,4,2,'2025-05-14 19:15:00','2025-05-14 20:00:00',45),(117,90,10,8,7,13,4,1,2,'2025-05-17 20:30:00','2025-05-17 21:10:00',40),(118,128,12,7,8,10,4,2,2,'2025-05-19 13:00:00','2025-05-19 13:45:00',45),(119,65,8,6,9,7,4,4,2,'2025-05-22 20:45:00','2025-05-22 21:25:00',40),(120,100,11,5,10,2,4,3,2,'2025-05-24 19:30:00','2025-05-24 20:10:00',40),(121,46,5,15,1,5,4,1,2,'2025-05-01 18:30:00','2025-05-01 19:05:00',35),(122,56,7,14,2,15,4,2,2,'2025-05-03 19:00:00','2025-05-03 19:40:00',40),(123,70,8,13,3,4,4,4,2,'2025-05-06 20:00:00','2025-05-06 20:35:00',35),(124,37,6,12,4,14,4,1,2,'2025-05-08 18:45:00','2025-05-08 19:20:00',35),(125,27,4,11,5,11,4,3,2,'2025-05-11 08:30:00','2025-05-11 09:00:00',30),(126,35,5,1,15,1,4,2,2,'2025-06-01 10:00:00','2025-06-01 10:25:00',25),(127,58,7,2,14,6,4,1,2,'2025-06-03 20:15:00','2025-06-03 20:50:00',35),(128,32,5,3,13,9,4,4,2,'2025-06-06 20:00:00','2025-06-06 20:30:00',30),(129,45,6,4,12,12,4,3,2,'2025-06-08 16:00:00','2025-06-08 16:30:00',30),(130,32,5,5,11,2,4,1,2,'2025-06-11 12:00:00','2025-06-11 12:35:00',35),(131,105,11,6,10,7,4,2,2,'2025-06-13 19:30:00','2025-06-13 20:10:00',40),(132,70,8,7,9,10,4,4,2,'2025-06-16 20:45:00','2025-06-16 21:25:00',40),(133,130,12,8,8,13,4,1,2,'2025-06-18 13:00:00','2025-06-18 13:45:00',45),(134,95,10,9,7,3,4,3,2,'2025-06-21 20:30:00','2025-06-21 21:10:00',40),(135,65,9,10,6,8,4,4,2,'2025-06-23 19:15:00','2025-06-23 20:00:00',45),(136,38,5,1,15,1,4,2,2,'2025-06-02 10:00:00','2025-06-02 10:25:00',25),(137,60,7,2,14,6,4,1,2,'2025-06-04 20:15:00','2025-06-04 20:50:00',35),(138,35,5,3,13,9,4,4,2,'2025-06-07 20:00:00','2025-06-07 20:30:00',30),(139,48,6,4,12,12,4,3,2,'2025-06-09 16:00:00','2025-06-09 16:30:00',30),(140,35,5,5,11,2,4,1,2,'2025-06-12 12:00:00','2025-06-12 12:35:00',35),(141,108,11,6,10,7,4,2,2,'2025-06-14 19:30:00','2025-06-14 20:10:00',40),(142,73,8,7,9,10,4,4,2,'2025-06-17 20:45:00','2025-06-17 21:25:00',40),(143,133,12,8,8,13,4,1,2,'2025-06-19 13:00:00','2025-06-19 13:45:00',45),(144,98,10,9,7,3,4,3,2,'2025-06-22 20:30:00','2025-06-22 21:10:00',40),(145,68,9,10,6,8,4,4,2,'2025-06-24 19:15:00','2025-06-24 20:00:00',45),(146,36,5,1,15,1,4,2,2,'2025-06-01 11:00:00','2025-06-01 11:25:00',25),(147,59,7,2,14,6,4,1,2,'2025-06-03 21:15:00','2025-06-03 21:50:00',35),(148,33,5,3,13,9,4,4,2,'2025-06-06 21:00:00','2025-06-06 21:30:00',30),(149,46,6,4,12,12,4,3,2,'2025-06-08 17:00:00','2025-06-08 17:30:00',30),(150,33,5,5,11,2,4,1,2,'2025-06-11 13:00:00','2025-06-11 13:35:00',35);
/*!40000 ALTER TABLE `Request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RequestProduct`
--

DROP TABLE IF EXISTS `RequestProduct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RequestProduct` (
  `requestId` int DEFAULT NULL,
  `productId` int DEFAULT NULL,
  `notes` text,
  `quantity` int DEFAULT NULL,
  `itemPrice` double DEFAULT NULL,
  KEY `fk_product_id` (`productId`),
  CONSTRAINT `fk_product_id` FOREIGN KEY (`productId`) REFERENCES `Product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RequestProduct`
--

LOCK TABLES `RequestProduct` WRITE;
/*!40000 ALTER TABLE `RequestProduct` DISABLE KEYS */;
INSERT INTO `RequestProduct` VALUES (1,2,NULL,1,30),(1,6,NULL,1,15),(2,7,NULL,1,45),(2,3,NULL,1,6),(2,4,NULL,1,3),(3,10,NULL,1,40),(3,11,NULL,1,25),(3,4,NULL,1,5),(4,9,NULL,1,28),(4,6,NULL,1,10);
/*!40000 ALTER TABLE `RequestProduct` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Restaurant`
--

DROP TABLE IF EXISTS `Restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Restaurant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text,
  `description` text,
  `openingHours` text,
  `addressId` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_restaurant_address` (`addressId`),
  CONSTRAINT `fk_restaurant_address` FOREIGN KEY (`addressId`) REFERENCES `Address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Restaurant`
--

LOCK TABLES `Restaurant` WRITE;
/*!40000 ALTER TABLE `Restaurant` DISABLE KEYS */;
INSERT INTO `Restaurant` VALUES (1,'Sabor da Esquina','Comida caseira com toque especial.','08:00-22:00',1),(2,'Pizzaria Noturna','As melhores pizzas da cidade.','18:00-02:00',2),(3,'Oriental Express','Culinária japonesa e chinesa autêntica.','11:00-23:00',3),(4,'Burger Mania','Hambúrgueres artesanais e batatas crocantes.','12:00-00:00',4),(5,'Café & Pão','Confeitaria e lanches rápidos.','07:00-19:00',5),(6,'Recanto Nordestino','Pratos típicos do Nordeste brasileiro.','10:00-22:00',6),(7,'Sushi Master','Experiência única em sushi e sashimi.','17:00-01:00',7),(8,'Churrascaria Gaúcha','O melhor churrasco e rodízio.','11:30-23:30',8),(9,'La Pasta Italiana','Massas frescas e molhos caseiros.','12:00-23:00',9),(10,'Frutos do Mar Frescos','Peixes e frutos do mar direto do mar.','11:00-22:00',10),(11,'Vegetariano Feliz','Opções saudáveis e saborosas.','09:00-20:00',11),(12,'Doceria Sonho','Bolos e doces para todas as ocasiões.','09:00-21:00',12),(13,'Esfiha Express','Esfihas abertas e fechadas tradicionais.','14:00-23:00',13),(14,'Temakeria da Hora','Temakis frescos e combos variados.','18:00-00:00',14),(15,'Sucos Naturais','Sucos energéticos e lanches leves.','08:00-18:00',15);
/*!40000 ALTER TABLE `Restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statusPayment`
--

DROP TABLE IF EXISTS `statusPayment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statusPayment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statusPayment`
--

LOCK TABLES `statusPayment` WRITE;
/*!40000 ALTER TABLE `statusPayment` DISABLE KEYS */;
INSERT INTO `statusPayment` VALUES (1,'Pendente'),(2,'Pago'),(3,'Cancelado'),(4,'Estornado');
/*!40000 ALTER TABLE `statusPayment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statusRequest`
--

DROP TABLE IF EXISTS `statusRequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statusRequest` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statusRequest`
--

LOCK TABLES `statusRequest` WRITE;
/*!40000 ALTER TABLE `statusRequest` DISABLE KEYS */;
INSERT INTO `statusRequest` VALUES (1,'Aguardando Confirmação'),(2,'Em Preparação'),(3,'A Caminho'),(4,'Entregue'),(5,'Cancelado');
/*!40000 ALTER TABLE `statusRequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `typePayment`
--

DROP TABLE IF EXISTS `typePayment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `typePayment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `typePayment`
--

LOCK TABLES `typePayment` WRITE;
/*!40000 ALTER TABLE `typePayment` DISABLE KEYS */;
INSERT INTO `typePayment` VALUES (1,'Cartão de Crédito'),(2,'Cartão de Débito'),(3,'Dinheiro'),(4,'PIX'),(5,'Voucher');
/*!40000 ALTER TABLE `typePayment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-25 12:13:46
