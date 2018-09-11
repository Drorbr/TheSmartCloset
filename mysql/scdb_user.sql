-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: scdb
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `token` varchar(4096) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'m.mich92@gmail.com','michal','Female','man','123'),(2,'drorbrosh@gmail.com','dror','Male','brosh','123a'),(3,'mor@gmail.com','mor','Male','peles','123b'),(5,'m.mich92@gmail.com','michal','Female','man','123c'),(6,'m.mich92@gmail.com','michal','Female','man','123d'),(7,'rotem@gmail.com','rotem','Female','harush','123e'),(8,'ba-li-lamot@gmail.com','mor','Male','hazain','123f'),(9,'ba-li-lamot@gmail.com','mor1','Male','hazain1','123g'),(10,'ba-li-lamot@gmail.com','mor2','Male','hazain2','123h'),(11,'ba-li-lamot@gmail.com','mor12','Male','peled12','123'),(12,'ba-li-lamot@gmail.com','mor12','Male','peled12','123123'),(15,'mppeled@gmail.com','mor',NULL,'peled','eyJhbGciOiJSUzI1NiIsImtpZCI6ImQ5NjQ4ZTAzMmNhYzU4NDI0ZTBkMWE3YzAzMGEzMTk4ZDNmNDZhZGIifQ.eyJhenAiOiI2Mjc1MDUxNTIwODktb3Y2bnJxYzU3MzMydG0yMWloazR2cnBjMmEyMTg3bm4uYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2Mjc1MDUxNTIwODktdmRxMzJpcHJoOGdmamZpbGpucG0zZ2RobGRqOTF1aGIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQ3MjAzNTgwMzc5MzM3NDMxNTMiLCJlbWFpbCI6Im1wcGVsZWRAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImV4cCI6MTUzNjY4NzgwMywiaXNzIjoiaHR0cHM6Ly9hY2NvdW50cy5nb29nbGUuY29tIiwiaWF0IjoxNTM2Njg0MjAzLCJuYW1lIjoibW9yIHBlbGVkIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS8tZS0tbzJsMFdoaEkvQUFBQUFBQUFBQUkvQUFBQUFBQUFBQUEvQVBVSUZhUEVPZEpJRXFHQ3ZJcGVPYllRZVlJWHlrZllZZy9zOTYtYy9waG90by5qcGciLCJnaXZlbl9uYW1lIjoibW9yIiwiZmFtaWx5X25hbWUiOiJwZWxlZCIsImxvY2FsZSI6ImhlIn0.Ik7FOGSjCuA345cdNHxjyE2OHb7KfkSsRO8pGLnAZoVIAmTZC60wgedZp_msdJod5r_aEAoJIwwEjIbt8_eCutjrZz0_Osfz2sp_qy2Jrr1bUfW2q9ESE1LEhfNYkpQHFTFtDxaXpI0Tv2M_7JNiMf9b3tLEXE42b_MHLJic2LY02WXo-2NOtQ0KMpsmB_viysrjD99DPsG1fFJTVlNl-Knj8SeaoQeijhcWFAsRGcwMGHPxdgl7F3AkJEp8zsQs6Yfbj8BJqbr_SZTzDGqlmQt1sbPc_lnPoQjnBT44zeNDTb0k7UWlNY8Q662JmOHm-z4oBvF3ZiEiUMKDKTF0jg');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-11 20:59:11
