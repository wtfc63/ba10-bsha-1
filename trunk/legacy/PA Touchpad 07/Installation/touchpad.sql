-- phpMyAdmin SQL Dump
-- version 2.10.1
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Erstellungszeit: 20. Juni 2007 um 12:00
-- Server Version: 5.0.41
-- PHP-Version: 5.2.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Datenbank: `touchpad`
-- 

-- --------------------------------------------------------

-- 
-- Tabellenstruktur für Tabelle `microgeste`
-- 

CREATE TABLE `microgeste` (
  `id` int(11) NOT NULL auto_increment,
  `sourceid` int(11) NOT NULL,
  `destinationid` int(11) NOT NULL,
  `probability` double NOT NULL,
  `type` int(11) NOT NULL,
  `length` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=141 ;

-- 
-- Daten für Tabelle `microgeste`
-- 

INSERT INTO `microgeste` VALUES (1, 1, 2, 0.1, 2, -1);
INSERT INTO `microgeste` VALUES (2, 1, 3, 0.2, 3, -1);
INSERT INTO `microgeste` VALUES (3, 1, 4, 0.2, 1, -1);
INSERT INTO `microgeste` VALUES (4, 1, 5, 0.4, 3, -1);
INSERT INTO `microgeste` VALUES (5, 1, 6, 0.1, 2, -1);
INSERT INTO `microgeste` VALUES (6, 2, 18, 0.5, 5, -1);
INSERT INTO `microgeste` VALUES (7, 2, 18, 0.5, 4, -1);
INSERT INTO `microgeste` VALUES (8, 3, 7, 0.7, 4, -1);
INSERT INTO `microgeste` VALUES (9, 3, 29, 0.3, 4, 1);
INSERT INTO `microgeste` VALUES (10, 4, 8, 0.5, 5, -1);
INSERT INTO `microgeste` VALUES (11, 4, 8, 0.5, 4, -1);
INSERT INTO `microgeste` VALUES (12, 5, 9, 0.125, 5, -1);
INSERT INTO `microgeste` VALUES (13, 5, 10, 0.875, 4, -1);
INSERT INTO `microgeste` VALUES (14, 6, 10, 1, 4, -1);
INSERT INTO `microgeste` VALUES (15, 7, 11, 0.4, 3, -1);
INSERT INTO `microgeste` VALUES (16, 7, 29, 0.4, 3, 1);
INSERT INTO `microgeste` VALUES (17, 7, 29, 0.2, 2, -1);
INSERT INTO `microgeste` VALUES (18, 8, 12, 1, 3, -1);
INSERT INTO `microgeste` VALUES (19, 9, 13, 0.5, 3, -1);
INSERT INTO `microgeste` VALUES (20, 9, 29, 0.5, 3, 1);
INSERT INTO `microgeste` VALUES (21, 10, 14, 0.888, 3, -1);
INSERT INTO `microgeste` VALUES (22, 10, 15, 0.111, 2, -1);
INSERT INTO `microgeste` VALUES (23, 11, 16, 0.4, 4, -1);
INSERT INTO `microgeste` VALUES (24, 11, 17, 0.1, 5, -1);
INSERT INTO `microgeste` VALUES (25, 11, 29, 0.4, 4, 1);
INSERT INTO `microgeste` VALUES (26, 12, 18, 1, 4, -1);
INSERT INTO `microgeste` VALUES (27, 13, 19, 1, 2, -1);
INSERT INTO `microgeste` VALUES (28, 14, 19, 0.25, 2, -1);
INSERT INTO `microgeste` VALUES (29, 15, 19, 1, 3, -1);
INSERT INTO `microgeste` VALUES (30, 14, 20, 0.625, 4, -1);
INSERT INTO `microgeste` VALUES (31, 16, 21, 0.5, 3, -1);
INSERT INTO `microgeste` VALUES (32, 17, 22, 0.8, 3, -1);
INSERT INTO `microgeste` VALUES (33, 17, 29, 0.2, 3, 1);
INSERT INTO `microgeste` VALUES (34, 19, 23, 0.75, 5, -1);
INSERT INTO `microgeste` VALUES (35, 14, 23, 0.125, 5, -1);
INSERT INTO `microgeste` VALUES (36, 20, 24, 0.8, 3, -1);
INSERT INTO `microgeste` VALUES (37, 21, 25, 1, 5, -1);
INSERT INTO `microgeste` VALUES (38, 11, 25, 0.1, 5, -1);
INSERT INTO `microgeste` VALUES (39, 23, 26, 1, 2, -1);
INSERT INTO `microgeste` VALUES (40, 19, 26, 0.25, 5, -1);
INSERT INTO `microgeste` VALUES (41, 20, 26, 0.2, 5, -1);
INSERT INTO `microgeste` VALUES (42, 24, 26, 1, 5, -1);
INSERT INTO `microgeste` VALUES (43, 25, 27, 1, 2, -1);
INSERT INTO `microgeste` VALUES (44, 18, 27, 1, 3, -1);
INSERT INTO `microgeste` VALUES (45, 27, 28, 0.3, 5, -1);
INSERT INTO `microgeste` VALUES (46, 27, 28, 0.3, 4, -1);
INSERT INTO `microgeste` VALUES (47, 27, 29, 0.4, 4, 1);
INSERT INTO `microgeste` VALUES (48, 16, 29, 0.5, 3, 1);
INSERT INTO `microgeste` VALUES (49, 28, 29, 0.5, 3, 1);
INSERT INTO `microgeste` VALUES (50, 28, 29, 0.5, 4, 1);
INSERT INTO `microgeste` VALUES (51, 22, 29, 1, 4, 1);
INSERT INTO `microgeste` VALUES (52, 30, 31, 0.3, 3, -1);
INSERT INTO `microgeste` VALUES (53, 30, 32, 0.5, 3, -1);
INSERT INTO `microgeste` VALUES (54, 31, 33, 0.167, 2, -1);
INSERT INTO `microgeste` VALUES (55, 30, 33, 0.1, 1, -1);
INSERT INTO `microgeste` VALUES (56, 30, 33, 0.1, 2, -1);
INSERT INTO `microgeste` VALUES (57, 32, 34, 1, 4, -1);
INSERT INTO `microgeste` VALUES (58, 33, 35, 0.8, 5, -1);
INSERT INTO `microgeste` VALUES (59, 33, 35, 0.2, 4, -1);
INSERT INTO `microgeste` VALUES (60, 34, 36, 0.9, 3, -1);
INSERT INTO `microgeste` VALUES (61, 35, 37, 1, 3, -1);
INSERT INTO `microgeste` VALUES (62, 36, 38, 0.444, 5, -1);
INSERT INTO `microgeste` VALUES (63, 36, 38, 0.444, 1, -1);
INSERT INTO `microgeste` VALUES (64, 36, 38, 0.111, 4, -1);
INSERT INTO `microgeste` VALUES (65, 34, 38, 0.1, 2, -1);
INSERT INTO `microgeste` VALUES (66, 37, 39, 1, 5, -1);
INSERT INTO `microgeste` VALUES (67, 31, 39, 0.5, 4, -1);
INSERT INTO `microgeste` VALUES (68, 31, 39, 0.333, 5, -1);
INSERT INTO `microgeste` VALUES (69, 39, 40, 1, 3, 1);
INSERT INTO `microgeste` VALUES (70, 41, 42, 0.1, 2, -1);
INSERT INTO `microgeste` VALUES (71, 41, 43, 0.1, 4, -1);
INSERT INTO `microgeste` VALUES (72, 41, 44, 0.15, 1, -1);
INSERT INTO `microgeste` VALUES (73, 41, 45, 0.1, 2, -1);
INSERT INTO `microgeste` VALUES (74, 41, 45, 0.2, 3, -1);
INSERT INTO `microgeste` VALUES (75, 41, 46, 0.05, 1, -1);
INSERT INTO `microgeste` VALUES (76, 41, 62, 0.15, 4, 1);
INSERT INTO `microgeste` VALUES (77, 41, 62, 0.15, 3, 1);
INSERT INTO `microgeste` VALUES (78, 44, 43, 0.4, 4, -1);
INSERT INTO `microgeste` VALUES (79, 44, 43, 0.6, 5, -1);
INSERT INTO `microgeste` VALUES (80, 43, 47, 0.6, 3, -1);
INSERT INTO `microgeste` VALUES (81, 43, 62, 0.4, 3, 1);
INSERT INTO `microgeste` VALUES (82, 45, 48, 0.35, 4, -1);
INSERT INTO `microgeste` VALUES (83, 45, 62, 0.35, 4, 1);
INSERT INTO `microgeste` VALUES (84, 45, 62, 0.1, 5, 1);
INSERT INTO `microgeste` VALUES (85, 45, 49, 0.2, 5, -1);
INSERT INTO `microgeste` VALUES (86, 46, 50, 1, 3, -1);
INSERT INTO `microgeste` VALUES (87, 47, 51, 0.75, 4, -1);
INSERT INTO `microgeste` VALUES (88, 48, 52, 0.3, 3, -1);
INSERT INTO `microgeste` VALUES (89, 48, 62, 0.6, 3, 1);
INSERT INTO `microgeste` VALUES (90, 49, 53, 1, 3, -1);
INSERT INTO `microgeste` VALUES (91, 50, 54, 1, 5, -1);
INSERT INTO `microgeste` VALUES (92, 51, 55, 0.833, 3, -1);
INSERT INTO `microgeste` VALUES (93, 52, 56, 0.6, 4, -1);
INSERT INTO `microgeste` VALUES (94, 53, 57, 0.667, 4, -1);
INSERT INTO `microgeste` VALUES (95, 53, 57, 0.333, 5, -1);
INSERT INTO `microgeste` VALUES (96, 54, 58, 1, 3, -1);
INSERT INTO `microgeste` VALUES (97, 55, 59, 0.8, 4, -1);
INSERT INTO `microgeste` VALUES (98, 42, 59, 0.5, 4, -1);
INSERT INTO `microgeste` VALUES (99, 56, 60, 0.25, 3, -1);
INSERT INTO `microgeste` VALUES (100, 56, 62, 0.25, 3, 1);
INSERT INTO `microgeste` VALUES (101, 58, 61, 1, 1, -1);
INSERT INTO `microgeste` VALUES (102, 59, 62, 1, 3, 1);
INSERT INTO `microgeste` VALUES (103, 47, 62, 0.125, 5, 1);
INSERT INTO `microgeste` VALUES (104, 55, 62, 0.2, 4, 1);
INSERT INTO `microgeste` VALUES (105, 51, 62, 0.167, 3, 1);
INSERT INTO `microgeste` VALUES (106, 47, 62, 0.125, 4, 1);
INSERT INTO `microgeste` VALUES (107, 42, 62, 0.5, 4, 1);
INSERT INTO `microgeste` VALUES (108, 60, 63, 1, 5, -1);
INSERT INTO `microgeste` VALUES (109, 56, 63, 0.5, 5, -1);
INSERT INTO `microgeste` VALUES (110, 52, 63, 0.4, 4, -1);
INSERT INTO `microgeste` VALUES (111, 48, 63, 0.1, 3, -1);
INSERT INTO `microgeste` VALUES (112, 57, 63, 1, 0, -1);
INSERT INTO `microgeste` VALUES (113, 61, 65, 1, 4, -1);
INSERT INTO `microgeste` VALUES (114, 65, 64, 1, 3, -1);
INSERT INTO `microgeste` VALUES (115, 64, 63, 1, 0, -1);
INSERT INTO `microgeste` VALUES (116, 66, 67, 0.25, 1, -1);
INSERT INTO `microgeste` VALUES (117, 66, 68, 0.15, 2, -1);
INSERT INTO `microgeste` VALUES (118, 66, 68, 0.35, 3, -1);
INSERT INTO `microgeste` VALUES (119, 67, 69, 0.8, 5, -1);
INSERT INTO `microgeste` VALUES (120, 66, 69, 0.25, 3, -1);
INSERT INTO `microgeste` VALUES (121, 67, 70, 0.2, 4, -1);
INSERT INTO `microgeste` VALUES (122, 68, 71, 0.9, 4, -1);
INSERT INTO `microgeste` VALUES (123, 68, 71, 0.1, 5, -1);
INSERT INTO `microgeste` VALUES (124, 69, 72, 0.444, 4, -1);
INSERT INTO `microgeste` VALUES (125, 70, 73, 1, 3, -1);
INSERT INTO `microgeste` VALUES (126, 71, 74, 0.5, 3, -1);
INSERT INTO `microgeste` VALUES (127, 72, 75, 0.5, 3, -1);
INSERT INTO `microgeste` VALUES (128, 69, 75, 0.333, 3, -1);
INSERT INTO `microgeste` VALUES (129, 73, 76, 1, 4, -1);
INSERT INTO `microgeste` VALUES (130, 74, 77, 0.8, 5, -1);
INSERT INTO `microgeste` VALUES (131, 74, 77, 0.2, 1, -1);
INSERT INTO `microgeste` VALUES (132, 71, 77, 0.5, 3, -1);
INSERT INTO `microgeste` VALUES (133, 75, 78, 0.8, 5, -1);
INSERT INTO `microgeste` VALUES (134, 69, 78, 0.222, 5, 1);
INSERT INTO `microgeste` VALUES (135, 76, 79, 1, 3, -1);
INSERT INTO `microgeste` VALUES (136, 78, 80, 1, 3, 1);
INSERT INTO `microgeste` VALUES (137, 75, 80, 0.2, 4, 1);
INSERT INTO `microgeste` VALUES (138, 72, 80, 0.25, 5, 1);
INSERT INTO `microgeste` VALUES (139, 72, 80, 0.25, 3, 1);
INSERT INTO `microgeste` VALUES (140, 79, 80, 1, 4, 1);

-- --------------------------------------------------------

-- 
-- Tabellenstruktur für Tabelle `sign`
-- 

CREATE TABLE `sign` (
  `id` int(11) NOT NULL auto_increment,
  `meaning` varchar(50) collate latin1_general_ci NOT NULL,
  `ascender` tinyint(1) NOT NULL,
  `descender` tinyint(1) NOT NULL,
  `quadrant` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=81 ;

-- 
-- Daten für Tabelle `sign`
-- 

INSERT INTO `sign` VALUES (1, 'root', 0, 0, 1);
INSERT INTO `sign` VALUES (2, '', 0, 0, 1);
INSERT INTO `sign` VALUES (3, '', 0, 0, 1);
INSERT INTO `sign` VALUES (4, '', 0, 0, 1);
INSERT INTO `sign` VALUES (5, '', 0, 0, 1);
INSERT INTO `sign` VALUES (6, '', 0, 0, 1);
INSERT INTO `sign` VALUES (7, '', 0, 0, 1);
INSERT INTO `sign` VALUES (8, '', 0, 0, 1);
INSERT INTO `sign` VALUES (9, '', 0, 0, 1);
INSERT INTO `sign` VALUES (10, '', 0, 0, 1);
INSERT INTO `sign` VALUES (11, '', 0, 0, 1);
INSERT INTO `sign` VALUES (12, '', 0, 0, 1);
INSERT INTO `sign` VALUES (13, '', 0, 0, 1);
INSERT INTO `sign` VALUES (14, '', 0, 0, 1);
INSERT INTO `sign` VALUES (15, '', 0, 0, 1);
INSERT INTO `sign` VALUES (16, '', 0, 0, 1);
INSERT INTO `sign` VALUES (17, '', 0, 0, 1);
INSERT INTO `sign` VALUES (18, '', 0, 0, 1);
INSERT INTO `sign` VALUES (19, '', 0, 0, 1);
INSERT INTO `sign` VALUES (20, '', 0, 0, 1);
INSERT INTO `sign` VALUES (21, '', 0, 0, 1);
INSERT INTO `sign` VALUES (22, '', 0, 0, 1);
INSERT INTO `sign` VALUES (23, '', 0, 0, 1);
INSERT INTO `sign` VALUES (24, '', 0, 0, 1);
INSERT INTO `sign` VALUES (25, '', 0, 0, 1);
INSERT INTO `sign` VALUES (26, 'b', 1, 0, 1);
INSERT INTO `sign` VALUES (27, '', 0, 0, 1);
INSERT INTO `sign` VALUES (28, '', 0, 0, 1);
INSERT INTO `sign` VALUES (29, 'a', 0, 0, 1);
INSERT INTO `sign` VALUES (30, 'root', 0, 0, 2);
INSERT INTO `sign` VALUES (31, '', 0, 0, 2);
INSERT INTO `sign` VALUES (32, '', 0, 0, 2);
INSERT INTO `sign` VALUES (33, '', 0, 0, 2);
INSERT INTO `sign` VALUES (34, '', 0, 0, 2);
INSERT INTO `sign` VALUES (35, '', 0, 0, 2);
INSERT INTO `sign` VALUES (36, '', 0, 0, 2);
INSERT INTO `sign` VALUES (37, '', 0, 0, 2);
INSERT INTO `sign` VALUES (38, 'b', 1, 0, 2);
INSERT INTO `sign` VALUES (39, '', 0, 0, 2);
INSERT INTO `sign` VALUES (40, 'a', 0, 0, 2);
INSERT INTO `sign` VALUES (41, 'root', 0, 0, 3);
INSERT INTO `sign` VALUES (42, '', 0, 0, 3);
INSERT INTO `sign` VALUES (43, '', 0, 0, 3);
INSERT INTO `sign` VALUES (44, '', 0, 0, 3);
INSERT INTO `sign` VALUES (45, '', 0, 0, 3);
INSERT INTO `sign` VALUES (46, '', 0, 0, 3);
INSERT INTO `sign` VALUES (47, '', 0, 0, 3);
INSERT INTO `sign` VALUES (48, '', 0, 0, 3);
INSERT INTO `sign` VALUES (49, '', 0, 0, 3);
INSERT INTO `sign` VALUES (50, '', 0, 0, 3);
INSERT INTO `sign` VALUES (51, '', 0, 0, 3);
INSERT INTO `sign` VALUES (52, '', 0, 0, 3);
INSERT INTO `sign` VALUES (53, '', 0, 0, 3);
INSERT INTO `sign` VALUES (54, '', 0, 0, 3);
INSERT INTO `sign` VALUES (55, '', 0, 0, 3);
INSERT INTO `sign` VALUES (56, '', 0, 0, 3);
INSERT INTO `sign` VALUES (57, '', 0, 0, 3);
INSERT INTO `sign` VALUES (58, '', 0, 0, 3);
INSERT INTO `sign` VALUES (59, '', 0, 0, 3);
INSERT INTO `sign` VALUES (60, '', 0, 0, 3);
INSERT INTO `sign` VALUES (61, '', 0, 0, 3);
INSERT INTO `sign` VALUES (62, 'a', 0, 0, 3);
INSERT INTO `sign` VALUES (63, 'b', 1, 0, 3);
INSERT INTO `sign` VALUES (64, '', 0, 0, 3);
INSERT INTO `sign` VALUES (65, '', 0, 0, 3);
INSERT INTO `sign` VALUES (66, 'root', 0, 0, 4);
INSERT INTO `sign` VALUES (67, '', 0, 0, 4);
INSERT INTO `sign` VALUES (68, '', 0, 0, 4);
INSERT INTO `sign` VALUES (69, '', 0, 0, 4);
INSERT INTO `sign` VALUES (70, '', 0, 0, 4);
INSERT INTO `sign` VALUES (71, '', 0, 0, 4);
INSERT INTO `sign` VALUES (72, '', 0, 0, 4);
INSERT INTO `sign` VALUES (73, '', 0, 0, 4);
INSERT INTO `sign` VALUES (74, '', 0, 0, 4);
INSERT INTO `sign` VALUES (75, '', 0, 0, 4);
INSERT INTO `sign` VALUES (76, '', 0, 0, 4);
INSERT INTO `sign` VALUES (77, 'b', 1, 0, 4);
INSERT INTO `sign` VALUES (78, '', 0, 0, 4);
INSERT INTO `sign` VALUES (79, '', 0, 0, 4);
INSERT INTO `sign` VALUES (80, 'a', 0, 0, 4);
