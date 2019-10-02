DROP SCHEMA IF EXISTS projet_ae CASCADE ;
CREATE SCHEMA projet_ae;

CREATE TABLE projet_ae.users(
	user_id SERIAL PRIMARY KEY,
	pseudo VARCHAR(30) NOT NULL,
	last_name VARCHAR(30) NOT NULL,
	first_name VARCHAR(30) NOT NULL,
	email VARCHAR(50) NOT NULL,
	password VARCHAR(60) NOT NULL,
	role VARCHAR(30) NOT NULL,
	record_date TIMESTAMP NOT NULL,
	version_number INTEGER
);



CREATE TABLE projet_ae.programs(
program_id SERIAL PRIMARY KEY,
program_name VARCHAR(30)
);


CREATE TABLE projet_ae.countries(
country_id SERIAL PRIMARY KEY,
country_code CHAR(2),
country_name VARCHAR(50),
program_id INTEGER REFERENCES projet_ae.programs(program_id)
);

CREATE TABLE projet_ae.students(
student_id SERIAL PRIMARY KEY,
title VARCHAR(30) NULL,
birth_date TIMESTAMP NULL,
nationality INTEGER REFERENCES projet_ae.countries(country_id) NULL,
street VARCHAR(50) NULL,
street_number INTEGER NULL,
box VARCHAR(30) NULL,
zip_code INTEGER NULL,
commune VARCHAR(30) NULL,
city VARCHAR(30) NULL,
tel_number VARCHAR(20) NULL,
sex CHAR(1) NULL,
number_high_school_years_succeded INTEGER NULL,
bank_card_number VARCHAR(20) NULL,
account_owner VARCHAR(30) NULL,
bank_name VARCHAR(30) NULL,
BIC_code VARCHAR(11) NULL,
user_id INTEGER REFERENCES projet_ae.users (user_id),
version_number INTEGER
);



CREATE TABLE  projet_ae.partners(
partner_id SERIAL PRIMARY KEY,
legal_name VARCHAR(100) NOT NULL,
business_name VARCHAR(100) NOT NULL,
full_name VARCHAR(100) NOT NULL,
departement VARCHAR(50),
organisation_type CHAR(50),
employee_number INTEGER,
adress VARCHAR(60) NOT NULL,
country INTEGER REFERENCES projet_ae.countries (country_id) NOT NULL,
area VARCHAR(50),
zip_code VARCHAR(50) NOT NULL,
city VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
web_site VARCHAR(50),
tel_number VARCHAR(50) NOT NULL,
mobility_type VARCHAR(50) NOT NULL
);



CREATE TABLE projet_ae.mobilities(
mobility_id SERIAL PRIMARY KEY,
application_number INTEGER,
preference_order_number INTEGER,
mobility_code VARCHAR(4),
semester INTEGER NULL,
partner INTEGER REFERENCES projet_ae.partners (partner_id) NULL,
introduction_date TIMESTAMP NULL,
stay_certificat BOOLEAN NULL,
transcripts BOOLEAN NULL,
stage_certificat BOOLEAN NULL,
final_report VARCHAR(255) NULL,
proof_passing_lang_tests_go BOOLEAN NULL,
school_scholarship_contract BOOLEAN NULL,
internship_study_agreement BOOLEAN NULL,
student_chart BOOLEAN NULL,
proof_passing_lang_tests_back BOOLEAN NULL,
commitment_doc BOOLEAN NULL,
cancellation_reason VARCHAR(255) NULL,
first_payment_request BOOLEAN NULL,
second_payment_request BOOLEAN NULL,
state VARCHAR(30),
confirmed_mobility BOOLEAN NULL,
pro_eco BOOLEAN NULL,
mobility_tool BOOLEAN NULL,
mobi BOOLEAN NULL,
cancelled_by_teacher BOOLEAN NULL,
student_id INTEGER REFERENCES projet_ae.students (student_id),
country_id INTEGER REFERENCES projet_ae.countries (country_id) NULL,
program_id INTEGER REFERENCES projet_ae.programs (program_id),
version_number INTEGER
);

-- insert programs

INSERT INTO projet_ae.programs VALUES(1,'FAME');
INSERT INTO projet_ae.programs VALUES(2,'Erasmus+');
INSERT INTO projet_ae.programs VALUES(3,'Erabel');
INSERT INTO projet_ae.programs VALUES(4,'Suisse');


--Insert countries
INSERT INTO projet_ae.countries VALUES(1,'AF','Afghanistan',1);					
INSERT INTO projet_ae.countries VALUES(2,'ZA','Afrique du Sud',1);					
INSERT INTO projet_ae.countries VALUES(3,'AL','Albanie',1);					
INSERT INTO projet_ae.countries VALUES(4,'DZ','Algerie',1);					
INSERT INTO projet_ae.countries VALUES(5,'DE','Allemagne',2);					
INSERT INTO projet_ae.countries VALUES(6,'AD','Andorre',1);					
INSERT INTO projet_ae.countries VALUES(7,'AO','Angola',1);					
INSERT INTO projet_ae.countries VALUES(8,'AI','Anguilla',1);					
INSERT INTO projet_ae.countries VALUES(9,'AQ','Antarctique',1);					
INSERT INTO projet_ae.countries VALUES(10,'AG','Antigua et Barbuda',1);					
INSERT INTO projet_ae.countries VALUES(11,'AN','Antilles Néerlandaises',1);					
INSERT INTO projet_ae.countries VALUES(12,'SA','Arabie Saoudite',1);					
INSERT INTO projet_ae.countries VALUES(13,'AR','Argentine',1);					
INSERT INTO projet_ae.countries VALUES(14,'AM','Arménie',1);					
INSERT INTO projet_ae.countries VALUES(15,'AW','Aruba',1);					
INSERT INTO projet_ae.countries VALUES(16,'AU','Australie',1);					
INSERT INTO projet_ae.countries VALUES(17,'AT','Autriche',2);					
INSERT INTO projet_ae.countries VALUES(18,'AZ','Azerbaidjan',1);					
INSERT INTO projet_ae.countries VALUES(19,'BS','Bahamas',1);					
INSERT INTO projet_ae.countries VALUES(20,'BH','Bahrein',1);					
INSERT INTO projet_ae.countries VALUES(21,'BD','Bangladesh',1);					
INSERT INTO projet_ae.countries VALUES(22,'BB','Barbade',1);					
INSERT INTO projet_ae.countries VALUES(23,'BY','Bélarus (Biélorussie)',1);					
INSERT INTO projet_ae.countries VALUES(24,'BE','Belgique',3);					
INSERT INTO projet_ae.countries VALUES(25,'BZ','Belize',1);					
INSERT INTO projet_ae.countries VALUES(26,'BJ','Bénin',1);					
INSERT INTO projet_ae.countries VALUES(27,'BM','Bermudes',1);					
INSERT INTO projet_ae.countries VALUES(28,'BT','Bhoutan',1);					
INSERT INTO projet_ae.countries VALUES(29,'BO','Bolivie',1);					
INSERT INTO projet_ae.countries VALUES(30,'BA','Bosnie et Herzégovine',1);					
INSERT INTO projet_ae.countries VALUES(31,'BW','Botswana',1);					
INSERT INTO projet_ae.countries VALUES(32,'BV','Bouvet Island',1);					
INSERT INTO projet_ae.countries VALUES(33,'BR','Brésil',1);					
INSERT INTO projet_ae.countries VALUES(34,'BN','Brunei',1);					
INSERT INTO projet_ae.countries VALUES(35,'BG','Bulgarie',2);					
INSERT INTO projet_ae.countries VALUES(36,'BF','Burkina Faso',1);					
INSERT INTO projet_ae.countries VALUES(37,'BI','Burundi',1);					
INSERT INTO projet_ae.countries VALUES(38,'KH','Cambodge',1);					
INSERT INTO projet_ae.countries VALUES(39,'CM','Cameroun',1);					
INSERT INTO projet_ae.countries VALUES(40,'CA','Canada',1);					
INSERT INTO projet_ae.countries VALUES(41,'CV','Cap Vert',1);					
INSERT INTO projet_ae.countries VALUES(42,'CL','Chili',1);					
INSERT INTO projet_ae.countries VALUES(43,'CN','Chine',1);					
INSERT INTO projet_ae.countries VALUES(44,'CY','Chypre',2);					
INSERT INTO projet_ae.countries VALUES(45,'VA','Cité du Vatican (Holy See)',1);					
INSERT INTO projet_ae.countries VALUES(46,'CO','Colombie',1);					
INSERT INTO projet_ae.countries VALUES(47,'KM','Comores',1);					
INSERT INTO projet_ae.countries VALUES(48,'CG','Congo, République',1);					
INSERT INTO projet_ae.countries VALUES(49,'CD','Congo, République Démocratique du',1);					
INSERT INTO projet_ae.countries VALUES(50,'KP','Corée du Nord',1);					
INSERT INTO projet_ae.countries VALUES(51,'KR','Corée du Sud',1);					
INSERT INTO projet_ae.countries VALUES(52,'CR','Costa Rica',1);					
INSERT INTO projet_ae.countries VALUES(53,'CI','Côte d Ivoire',1);					
INSERT INTO projet_ae.countries VALUES(54,'HR','Croatie',2);					
INSERT INTO projet_ae.countries VALUES(55,'CU','Cuba',1);					
INSERT INTO projet_ae.countries VALUES(56,'CW','Curacao',1);					
INSERT INTO projet_ae.countries VALUES(57,'DK','Danemark',2);					
INSERT INTO projet_ae.countries VALUES(58,'DJ','Djibouti',1);					
INSERT INTO projet_ae.countries VALUES(59,'DM','Dominique',1);					
INSERT INTO projet_ae.countries VALUES(60,'EG','Egypte',1);					
INSERT INTO projet_ae.countries VALUES(61,'AE','Emirats Arabes Unis',1);					
INSERT INTO projet_ae.countries VALUES(62,'EC','Equateur',1);				
INSERT INTO projet_ae.countries VALUES(63,'ER','Erythrée',1);					
INSERT INTO projet_ae.countries VALUES(64,'ES','Espagne',2);					
INSERT INTO projet_ae.countries VALUES(65,'EE','Estonie',2);					
INSERT INTO projet_ae.countries VALUES(66,'US','Etats-Unis',1);					
INSERT INTO projet_ae.countries VALUES(67,'ET','Ethiopie',1);					
INSERT INTO projet_ae.countries VALUES(68,'FJ','Fidji',1);					
INSERT INTO projet_ae.countries VALUES(69,'FI','Finlande',2);					
INSERT INTO projet_ae.countries VALUES(70,'FR','France',2);					
INSERT INTO projet_ae.countries VALUES(71,'FX','France, Métropolitaine',1);					
INSERT INTO projet_ae.countries VALUES(72,'GA','Gabon',1);					
INSERT INTO projet_ae.countries VALUES(73,'GM','Gambie',1);					
INSERT INTO projet_ae.countries VALUES(74,'PS','Gaza',1);					
INSERT INTO projet_ae.countries VALUES(75,'GE','Géorgie',1);					
INSERT INTO projet_ae.countries VALUES(76,'GS','Géorgie du Sud et les îles Sandwich du Sud',1);					
INSERT INTO projet_ae.countries VALUES(77,'GH','Ghana',1);					
INSERT INTO projet_ae.countries VALUES(78,'GI','Gibraltar',1);					
INSERT INTO projet_ae.countries VALUES(79,'GR','Grèce',2);					
INSERT INTO projet_ae.countries VALUES(80,'GD','Grenade',1);					
INSERT INTO projet_ae.countries VALUES(81,'GL','Greoenland',1);					
INSERT INTO projet_ae.countries VALUES(82,'GP','Guadeloupe',1);					
INSERT INTO projet_ae.countries VALUES(83,'GU','Guam',1);					
INSERT INTO projet_ae.countries VALUES(84,'GT','Guatemala',1);					
INSERT INTO projet_ae.countries VALUES(85,'GN','Guinée',1);					
INSERT INTO projet_ae.countries VALUES(86,'GW','Guinée Bissau',1);					
INSERT INTO projet_ae.countries VALUES(87,'GQ','Guinée Equatoriale',1);					
INSERT INTO projet_ae.countries VALUES(88,'GY','Guyane',1);					
INSERT INTO projet_ae.countries VALUES(89,'GF','Guyane Française',1);					
INSERT INTO projet_ae.countries VALUES(90,'HT','Haïti',1);					
INSERT INTO projet_ae.countries VALUES(91,'HN','Honduras',1);					
INSERT INTO projet_ae.countries VALUES(92,'HK','Hong Kong',1);					
INSERT INTO projet_ae.countries VALUES(93,'HU','Hongrie',2);					
INSERT INTO projet_ae.countries VALUES(94,'IM','Ile de Man',1);					
INSERT INTO projet_ae.countries VALUES(95,'KY','Iles Caïman',1);					
INSERT INTO projet_ae.countries VALUES(96,'CX','Iles Christmas',1);					
INSERT INTO projet_ae.countries VALUES(97,'CC','Iles Cocos',1);					
INSERT INTO projet_ae.countries VALUES(98,'CK','Iles Cook',1);					
INSERT INTO projet_ae.countries VALUES(99,'FO','Iles Féroé',1);					
INSERT INTO projet_ae.countries VALUES(100,'GG','Iles Guernesey',1);					
INSERT INTO projet_ae.countries VALUES(101,'HM','Iles Heardet McDonald',1);					
INSERT INTO projet_ae.countries VALUES(102,'FK','Iles Malouines',1);					
INSERT INTO projet_ae.countries VALUES(103,'MP','Iles Mariannes du Nord',1);					
INSERT INTO projet_ae.countries VALUES(104,'MH','Iles Marshall',1);					
INSERT INTO projet_ae.countries VALUES(105,'MU','Iles Maurice',1);					
INSERT INTO projet_ae.countries VALUES(106,'UM','Iles mineures éloignées des Etats-Unis',1);					
INSERT INTO projet_ae.countries VALUES(107,'NF','Iles Norfolk',1);					
INSERT INTO projet_ae.countries VALUES(108,'SB','Iles Salomon',1);					
INSERT INTO projet_ae.countries VALUES(109,'TC','Iles Turques et Caïque',1);					
INSERT INTO projet_ae.countries VALUES(110,'VI','Iles Vierges des Etats-Unis',1);					
INSERT INTO projet_ae.countries VALUES(111,'VG','Iles Vierges du Royaume-Uni',1);					
INSERT INTO projet_ae.countries VALUES(112,'IN','Inde',1);					
INSERT INTO projet_ae.countries VALUES(113,'ID','Indonésie',1);					
INSERT INTO projet_ae.countries VALUES(114,'IR','Iran',1);					
INSERT INTO projet_ae.countries VALUES(115,'IQ','Iraq',1);					
INSERT INTO projet_ae.countries VALUES(116,'IE','Irlande',2);					
INSERT INTO projet_ae.countries VALUES(117,'IS','Islande',2);					
INSERT INTO projet_ae.countries VALUES(118,'IL','Israël',1);					
INSERT INTO projet_ae.countries VALUES(119,'IT','Italie',2);					
INSERT INTO projet_ae.countries VALUES(120,'JM','Jamaique',1);					
INSERT INTO projet_ae.countries VALUES(121,'JP','Japon',1);					
INSERT INTO projet_ae.countries VALUES(122,'JE','Jersey',1);					
INSERT INTO projet_ae.countries VALUES(123,'JO','Jordanie',1);					
INSERT INTO projet_ae.countries VALUES(124,'KZ','Kazakhstan',1);					
INSERT INTO projet_ae.countries VALUES(125,'KE','Kenya',1);					
INSERT INTO projet_ae.countries VALUES(126,'KG','Kirghizistan',1);					
INSERT INTO projet_ae.countries VALUES(127,'KI','Kiribati',1);					
INSERT INTO projet_ae.countries VALUES(128,'XK','Kosovo',1);					
INSERT INTO projet_ae.countries VALUES(129,'KW','Koweit',1);					
INSERT INTO projet_ae.countries VALUES(130,'LA','Laos',1);					
INSERT INTO projet_ae.countries VALUES(131,'LS','Lesotho',1);					
INSERT INTO projet_ae.countries VALUES(132,'LV','Lettonie',2);					
INSERT INTO projet_ae.countries VALUES(133,'LB','Liban',1);					
INSERT INTO projet_ae.countries VALUES(134,'LR','Libéria',1);					
INSERT INTO projet_ae.countries VALUES(135,'LY','Libye',1);					
INSERT INTO projet_ae.countries VALUES(136,'LI','Liechtenstein',2);					
INSERT INTO projet_ae.countries VALUES(137,'LT','Lituanie',2);					
INSERT INTO projet_ae.countries VALUES(138,'LU','Luxembourg',2);					
INSERT INTO projet_ae.countries VALUES(139,'MO','Macao',1);					
INSERT INTO projet_ae.countries VALUES(140,'MK','Macédoine',2);					
INSERT INTO projet_ae.countries VALUES(141,'MG','Madagascar',1);					
INSERT INTO projet_ae.countries VALUES(142,'MY','Malaisie',1);					
INSERT INTO projet_ae.countries VALUES(143,'MW','Malawi',1);					
INSERT INTO projet_ae.countries VALUES(144,'MV','Maldives',1);					
INSERT INTO projet_ae.countries VALUES(145,'ML','Mali',1);					
INSERT INTO projet_ae.countries VALUES(146,'MT','Malte',2);					
INSERT INTO projet_ae.countries VALUES(147,'MA','Maroc',1);					
INSERT INTO projet_ae.countries VALUES(148,'MQ','Martinique',1);					
INSERT INTO projet_ae.countries VALUES(149,'MR','Mauritanie',1);					
INSERT INTO projet_ae.countries VALUES(150,'YT','Mayotte',1);					
INSERT INTO projet_ae.countries VALUES(151,'MX','Mexique',1);					
INSERT INTO projet_ae.countries VALUES(152,'FM','Micronésie',1);					
INSERT INTO projet_ae.countries VALUES(153,'MD','Moldavie',1);					
INSERT INTO projet_ae.countries VALUES(154,'MC','Monaco',1);					
INSERT INTO projet_ae.countries VALUES(155,'MN','Mongolie',1);					
INSERT INTO projet_ae.countries VALUES(156,'ME','Monténégro',1);					
INSERT INTO projet_ae.countries VALUES(157,'MS','Montserrat',1);					
INSERT INTO projet_ae.countries VALUES(158,'MZ','Mozambique',1);					
INSERT INTO projet_ae.countries VALUES(159,'MM','Myanmar (Birmanie)',1);					
INSERT INTO projet_ae.countries VALUES(160,'NA','Namibie',1);					
INSERT INTO projet_ae.countries VALUES(161,'NR','Nauru',1);					
INSERT INTO projet_ae.countries VALUES(162,'NP','Népal',1);					
INSERT INTO projet_ae.countries VALUES(163,'NI','Nicaragua',1);					
INSERT INTO projet_ae.countries VALUES(164,'NE','Niger',1);					
INSERT INTO projet_ae.countries VALUES(165,'NG','Nigeria',1);					
INSERT INTO projet_ae.countries VALUES(166,'NU','Niue',1);					
INSERT INTO projet_ae.countries VALUES(167,'NO','Norvège',2);					
INSERT INTO projet_ae.countries VALUES(168,'NC','Nouvelle Calédonie',1);					
INSERT INTO projet_ae.countries VALUES(169,'NZ','Nouvelle Zélande',1);					
INSERT INTO projet_ae.countries VALUES(170,'OM','Oman',1);					
INSERT INTO projet_ae.countries VALUES(171,'UG','Ouganda',1);					
INSERT INTO projet_ae.countries VALUES(172,'UZ','Ouzbékistan',1);					
INSERT INTO projet_ae.countries VALUES(173,'PK','Pakistan',1);					
INSERT INTO projet_ae.countries VALUES(174,'PW','Palau',1);					
INSERT INTO projet_ae.countries VALUES(175,'PA','Panama',1);					
INSERT INTO projet_ae.countries VALUES(176,'PG','Papouasie Nouvelle Guinée',1);					
INSERT INTO projet_ae.countries VALUES(177,'PY','Paraguay',1);					
INSERT INTO projet_ae.countries VALUES(178,'NL','Pays-Bas',2);					
INSERT INTO projet_ae.countries VALUES(179,'PE','Pérou',1);					
INSERT INTO projet_ae.countries VALUES(180,'PH','Philippines',1);					
INSERT INTO projet_ae.countries VALUES(181,'PN','Pitcairn',1);					
INSERT INTO projet_ae.countries VALUES(182,'PL','Pologne',2);					
INSERT INTO projet_ae.countries VALUES(183,'PF','Polynésie Française',1);					
INSERT INTO projet_ae.countries VALUES(184,'PR','Porto Rico',1);					
INSERT INTO projet_ae.countries VALUES(185,'PT','Portugal',2);					
INSERT INTO projet_ae.countries VALUES(186,'QA','Qatar',1);					
INSERT INTO projet_ae.countries VALUES(187,'CF','République Centraficaine',1);					
INSERT INTO projet_ae.countries VALUES(188,'DO','République Dominicaine',1);					
INSERT INTO projet_ae.countries VALUES(189,'CZ','République Tchèque',2);					
INSERT INTO projet_ae.countries VALUES(190,'RE','Réunion',1);					
INSERT INTO projet_ae.countries VALUES(191,'RO','Roumanie',2);					
INSERT INTO projet_ae.countries VALUES(192,'GB','Royaume Uni',1);					
INSERT INTO projet_ae.countries VALUES(193,'RU','Russie',1);					
INSERT INTO projet_ae.countries VALUES(194,'RW','Rwanda',1);					
INSERT INTO projet_ae.countries VALUES(195,'EH','Sahara Occidental',1);					
INSERT INTO projet_ae.countries VALUES(196,'BL','Saint Barthelemy',1);					
INSERT INTO projet_ae.countries VALUES(197,'SH','Saint Hélène',1);					
INSERT INTO projet_ae.countries VALUES(198,'KN','Saint Kitts et Nevis',1);					
INSERT INTO projet_ae.countries VALUES(199,'MF','Saint Martin',1);					
INSERT INTO projet_ae.countries VALUES(200,'SX','Saint Martin',1);					
INSERT INTO projet_ae.countries VALUES(201,'PM','Saint Pierre et Miquelon',1);					
INSERT INTO projet_ae.countries VALUES(202,'VC','Saint Vincent et les Grenadines',1);					
INSERT INTO projet_ae.countries VALUES(203,'LC','Sainte Lucie',1);					
INSERT INTO projet_ae.countries VALUES(204,'SV','Salvador',1);					
INSERT INTO projet_ae.countries VALUES(205,'AS','Samoa Americaines',1);					
INSERT INTO projet_ae.countries VALUES(206,'WS','Samoa Occidentales',1);					
INSERT INTO projet_ae.countries VALUES(207,'SM','San Marin',1);					
INSERT INTO projet_ae.countries VALUES(208,'ST','Sao Tomé et Principe',1);					
INSERT INTO projet_ae.countries VALUES(209,'SN','Sénégal',1);					
INSERT INTO projet_ae.countries VALUES(210,'RS','Serbie',1);					
INSERT INTO projet_ae.countries VALUES(211,'SC','Seychelles',1);					
INSERT INTO projet_ae.countries VALUES(212,'SL','Sierra Léone',1);					
INSERT INTO projet_ae.countries VALUES(213,'SG','Singapour',1);					
INSERT INTO projet_ae.countries VALUES(214,'SK','Slovaquie',2);					
INSERT INTO projet_ae.countries VALUES(215,'SI','Slovénie',2);					
INSERT INTO projet_ae.countries VALUES(216,'SO','Somalie',1);					
INSERT INTO projet_ae.countries VALUES(217,'SD','Soudan',1);					
INSERT INTO projet_ae.countries VALUES(218,'LK','Sri Lanka',1);					
INSERT INTO projet_ae.countries VALUES(219,'SS','Sud Soudan',1);					
INSERT INTO projet_ae.countries VALUES(220,'SE','Suède',2);					
INSERT INTO projet_ae.countries VALUES(221,'CH','Suisse',4);					
INSERT INTO projet_ae.countries VALUES(222,'SR','Surinam',1);					
INSERT INTO projet_ae.countries VALUES(223,'SJ','Svalbard et Jan Mayen',1);					
INSERT INTO projet_ae.countries VALUES(224,'SZ','Swaziland',1);					
INSERT INTO projet_ae.countries VALUES(225,'SY','Syrie',1);					
INSERT INTO projet_ae.countries VALUES(226,'TJ','Tadjikistan',1);					
INSERT INTO projet_ae.countries VALUES(227,'TW','Taiwan',1);					
INSERT INTO projet_ae.countries VALUES(228,'TZ','Tanzanie',1);					
INSERT INTO projet_ae.countries VALUES(229,'TD','Tchad',1);					
INSERT INTO projet_ae.countries VALUES(230,'TF','Terres Australes et Antarctique Françaises',1);					
INSERT INTO projet_ae.countries VALUES(231,'IO','Térritoire Britannique de l Océan Indien',1);					
INSERT INTO projet_ae.countries VALUES(232,'PS','Territoires Palestiniens occupés (Gaza)',1);					
INSERT INTO projet_ae.countries VALUES(233,'TH','Thaïlande',1);					
INSERT INTO projet_ae.countries VALUES(234,'TL','Timor-Leste',1);					
INSERT INTO projet_ae.countries VALUES(235,'TG','Togo',1);					
INSERT INTO projet_ae.countries VALUES(236,'TK','Tokelau',1);					
INSERT INTO projet_ae.countries VALUES(237,'TO','Tonga',1);					
INSERT INTO projet_ae.countries VALUES(238,'TT','Trinité et Tobago',1);					
INSERT INTO projet_ae.countries VALUES(239,'TN','Tunisie',1);					
INSERT INTO projet_ae.countries VALUES(240,'TM','Turkmenistan',1);					
INSERT INTO projet_ae.countries VALUES(241,'TR','Turquie',2);					
INSERT INTO projet_ae.countries VALUES(242,'TV','Tuvalu',1);					
INSERT INTO projet_ae.countries VALUES(243,'UA','Ukraine',1);					
INSERT INTO projet_ae.countries VALUES(244,'UY','Uruguay',1);					
INSERT INTO projet_ae.countries VALUES(245,'VU','Vanuatu',1);					
INSERT INTO projet_ae.countries VALUES(246,'VE','Venezuela',1);					
INSERT INTO projet_ae.countries VALUES(247,'VN','Vietnam',1);					
INSERT INTO projet_ae.countries VALUES(248,'WF','Wallis et Futuna',1);					
INSERT INTO projet_ae.countries VALUES(249,'YE','Yemen',1);					
INSERT INTO projet_ae.countries VALUES(250,'ZM','Zambie',1);					
INSERT INTO projet_ae.countries VALUES(251,'ZW','Zimbabwe',1);					

--INSERT PARTNERS ON DB

INSERT INTO projet_ae.partners VALUES(DEFAULT,'UC Leuven-Limburg (Leuven campus)','UC Leuven-Limburg (Leuven campus)','UC Leuven-Limburg (Leuven campus)','Computer Sciences',null,null,'Abdij van Park 9',24,'','3001','Heverlee','international@ucll.be','https://www.ucll.be/','+32 (0)16 375 735','SMS');
INSERT INTO projet_ae.partners VALUES(DEFAULT,'Technological University Dublin ','Technological University Dublin ','Technological University Dublin ','Computing',null,null,'40- 45 Mount Joy Square',116,'','Dublin 1','Dublin','erasmus@dit.ie','www.dit.ie/computing/','+35314023404','SMS');
INSERT INTO projet_ae.partners VALUES(DEFAULT,'Université du Luxembourg','Université du Luxembourg','Université du Luxembourg','Computing',null,null,'7, avenue des Hauts-Fourneaux',138,'','L-4362','Esch-sur-Alzette','erasmus@uni.lu','https://wwwfr.uni.lu/','(+352) 46 66 44 4000','SMS');
INSERT INTO projet_ae.partners VALUES(DEFAULT,'Wölfel Engineering GmbH','Wölfel Engineering GmbH','Wölfel Engineering GmbH','Data processing and analytics',null,null,'Max-Planck-Str. 15',5,'','97204','Höchberg','tr@woelfel.de ','https://www.woelfel.de/home.html','+49 (931) 49708-168','SMP');
INSERT INTO projet_ae.partners VALUES(DEFAULT,'HES-SO Haute école spécialisée de Suisse occidentale ','HES-SO Haute école spécialisée de Suisse occidentale (Haute école de gestion de Genève (HEG GE))','HES-SO Haute école spécialisée de Suisse occidentale (Haute école de gestion de Genève (HEG GE))','Business Information Systems',null,null,'17, Rue de la Tambourine ',221,'','1227','Carouge – Genève','international@hes-so.ch','','0041 22 388 17 00','SMS');
INSERT INTO projet_ae.partners VALUES(DEFAULT,'cégep Edouard Montpetit','cégep Edouard Montpetit','cégep Edouard Montpetit','Techniques de l’informatique',null,null,'945 chemin de Chambly',40,'Québec','J4H  3M6','Longueuil','mobilites@cegepmontpetit.ca ','http://www.cegepmontpetit.ca/','450 679-2631','SMS');
INSERT INTO projet_ae.partners VALUES(DEFAULT,'Collège de Maisonneuve','Collège de Maisonneuve','Collège de Maisonneuve','Techniques de l’informatique',null,null,'3 800, rue Sherbrooke Est ',40,'Québec','H1X 2A2','Montréal','international@cmaisonneuve.qc.ca ','https://www.cmaisonneuve.qc.ca/ ','514 254-7131','SMS');
INSERT INTO projet_ae.partners VALUES(DEFAULT,'Cégep de Chicoutimi','Cégep de Chicoutimi','Cégep de Chicoutimi','Techniques de l’informatique',null,null,'534 Jacques-Cartier Est',40,'Québec','G7H 1Z6','Chicoutimi','mobilitesetudiantes@cchic.ca ','https://cchic.ca','418 549.9520  | Poste 1144','SMS');


--Insert USER
INSERT INTO projet_ae.users VALUES(DEFAULT,'bri','Lehmann','Brigitte','brigitte.lehmann@vinci.be','$2a$10$.SCsET3SsHhH4bBiQ5qnl.ZMQNOcgGzQjWFWUwrBLHUnlHIA2pp6a','resp_teacher',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'lau','Leleux','Laurent','laurent.leleux@vinci.be','$2a$10$yNF7JgrMLdaJPkvByQpyiOeHfvPFOWc/7P9Yi2D/fGaB5xB/7MB7G','teacher',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'chri','Damas','Christophe','christophe.damas@vinci.be','$2a$10$ah8FVRqJi.XRzQLODIsFke7Jw4eP/yCzlObwrXAEkyXAmFn3N7Gya','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'achil','Ile','Achille','ach.ile@student.vinci.be ','$2a$10$TN2wKnwGhIWeWQxdq.f2t.PqbspY8b2p2Z2i7NbCOlq2.hPhplnGW','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'bazz','Ile','Basile','bas.ile@student.vinci.be','$2a$10$IfqJtGThcPSnGu4bjMkuiu10NdPhG8MJwo.lglpbNU.02NX2Ehqzi','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'caro','Line','Caroline','car.line@student.vinci.be','$2a$10$JBdWMTL3vGgLwWbTXhisl.3CTfkXptzcw0gVhurE/ag7GD1.0FJvi','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'laurence','Lasage','Laurence','lau.sage@student.vinci.be','$2a$10$L4rrIWZ1hMtY2LwH6GsGhOT/jGHF3bJBNSKudO.ztoy76goyHOjY6','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'pir','Kiroule','Pierre','pir.pas@student.vinci.be','$2a$10$565GXNgzLT07IVMzjrk4euoyMdtVnjWYysCdDW9pzdqxyVIuooMfG','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'mousse','Namassepas','Mousse','nam.mas@student.vinci.be','$2a$10$oqKrPDb17dQfzZJV1ZijXuT5Mw8fSMZdsESyZ8gB2j3AHMlCrd/hi','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'the','Tatilotetatou','Tonthe','ton.the@student.vinci.be','$2a$10$niLaw3/xPDjzsiG2KPBqweOxxAZVWvUQ1M4.PmVdiG4El6FJoWKQS','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'albert','Frair','Albert','al.fr@student.vinci.be','$2a$10$f.hqJ6RMFI1w/Jta0pt1c.rYDmJGo3cI0UU23PKUTkK4I9BN4gN5K','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'alph','Albert','Alpha','al.pha@student.vinci.be','$2a$10$ZnO71m.T7iF1XV7CAjuDJ.bdXsOSiPH6lVSAhjBD.DB0RJ1oAmdoO','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'louis','Albert','Louis','lo.ouis@student.vinci.be','$2a$10$68dOHchxhGDrU0Jc62VW7upTOioV9gKa70EqHNCd3YdBmzCO0HHYm','student',NOW(),1);																				
INSERT INTO projet_ae.users VALUES(DEFAULT,'theo','Legrand','Théophile','theo.phile@student.vinci.be','$2a$10$fROthyB0nPFk.zRw5C6KIuca5TSiaZUx0A7WMbkTR/RsJ1Aq3XXlG','student',NOW(),1);																				

--insert students
INSERT INTO projet_ae.students VALUES(DEFAULT,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,3,1);
INSERT INTO projet_ae.students VALUES(DEFAULT,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1);	
INSERT INTO projet_ae.students VALUES(DEFAULT,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,1);																						
INSERT INTO projet_ae.students VALUES(DEFAULT,'Mlle','1996-05-13 14:30:26.840915',24,'rue des coteaux',27,NULL,1300,'Limal','Limal','0476354921','F',2,'BE31 0000 0000 5555','Caroline','Banque de la poste','BPOTBEB1',6,1);	
INSERT INTO projet_ae.students VALUES(DEFAULT,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,1);							
INSERT INTO projet_ae.students VALUES(DEFAULT,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,1);							
INSERT INTO projet_ae.students VALUES(DEFAULT,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,9,1);							
INSERT INTO projet_ae.students VALUES(DEFAULT,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,10,1);							
INSERT INTO projet_ae.students VALUES(DEFAULT,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,11,1);	
INSERT INTO projet_ae.students VALUES(DEFAULT,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,12,1);				
INSERT INTO projet_ae.students VALUES(DEFAULT,'M.','1995-09-12 14:30:26.840915',24,'rue des soucis',27,NULL,6150,'Anderlues','Anderlues','0475895623','M',2,'BE73 0000 0000 6060','Louis','Banque de la poste','BPOTBEB1',13,1);	
INSERT INTO projet_ae.students VALUES(DEFAULT,'M.','1995-12-10 14:30:26.840915',70,'rue du lac',25,'B2',1348,'Louvain-la-Neuve','Louvain-la-Neuve',NULL,'M',3,'BE71 2100 6676 6869','Theophile','BNP PARIBAS FORTIS','GEBABEBB',14,1);																			
																			

-- INSERT demande de mobilities (2017-2018)
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,1,2,'SMS',1,3,'2017-11-13 14:30:26.840915',FALSE,FALSE,FALSE,NULL,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,NULL,FALSE,FALSE,'none',FALSE,FALSE,FALSE,FALSE,FALSE,4,138,2,1); -- caro 
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,1,3,'SMP',2,NULL,'2017-10-05 14:30:26.840915',FALSE,FALSE,FALSE,NULL,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,NULL,FALSE,FALSE,'none',FALSE,FALSE,FALSE,FALSE,FALSE,4,NULL,1,1); -- Caro 
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,2,1,'SMP',2,NULL,'2017-12-10 14:30:26.840915',FALSE,FALSE,FALSE,NULL,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,NULL,FALSE,FALSE,'none',FALSE,FALSE,FALSE,FALSE,FALSE,2,NULL,1,1);-- Achille 

-- INSERT demande mobilities (2018-2019)
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,3,1,'SMS',1,NULL,'2018-12-10 14:30:26.840915',FALSE,FALSE,FALSE,NULL,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,NULL,FALSE,FALSE,'none',FALSE,FALSE,FALSE,FALSE,FALSE,12,169,1,1);-- Theophile 
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,4,1,'SMS',1,5,'2018-11-11 14:30:26.840915',FALSE,FALSE,FALSE,NULL,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,NULL,FALSE,FALSE,'none',FALSE,FALSE,FALSE,FALSE,FALSE,7,221,4,1);-- Mousse 
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,5,1,'SMS',1,1,'2018-12-11 14:30:26.840915',FALSE,FALSE,FALSE,NULL,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,NULL,FALSE,FALSE,'none',FALSE,FALSE,FALSE,FALSE,FALSE,8,24,3,1);-- Tonthe
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,5,2,'SMP',2,NULL,'2018-12-05 14:30:26.840915',FALSE,FALSE,FALSE,NULL,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,NULL,FALSE,FALSE,'none',FALSE,FALSE,FALSE,FALSE,FALSE,8,NULL,2,1);-- Tonthe
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,6,3,'SMS',1,7,'2018-11-21 14:30:26.840915',FALSE,FALSE,FALSE,NULL,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,NULL,FALSE,FALSE,'none',FALSE,FALSE,FALSE,FALSE,FALSE,11,40,1,1);-- Louis

--INSERT mobilitées déjà validées par un prof
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,1,1,'SMS',1,2,NOW(),FALSE,FALSE,FALSE,NULL,TRUE,TRUE,TRUE,TRUE,FALSE,TRUE,NULL,TRUE,FALSE,'first_demand',TRUE,FALSE,FALSE,FALSE,FALSE,4,116,2,1);-- Caroline
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,3,2,'SMS',1,2,NOW(),FALSE,FALSE,FALSE,NULL,FALSE,FALSE,TRUE,FALSE,FALSE,FALSE,NULL,FALSE,FALSE,'in_preparation',TRUE,FALSE,FALSE,FALSE,FALSE,12,116,2,1);-- Theophile
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,6,1,'SMS',1,3,NOW(),FALSE,FALSE,FALSE,NULL,TRUE,TRUE,TRUE,TRUE,FALSE,TRUE,NULL,FALSE,FALSE,'to_pay',TRUE,FALSE,FALSE,FALSE,FALSE,11,138,2,1);-- Louis
INSERT INTO projet_ae.mobilities VALUES(DEFAULT,6,2,'SMS',2,6,NOW(),FALSE,FALSE,FALSE,NULL,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,NULL,FALSE,FALSE,'created',TRUE,FALSE,FALSE,FALSE,FALSE,11,40,1,1);-- Louis



GRANT CONNECT ON DATABASE dbbaudouin_martelee TO marcin_wilk;
GRANT CONNECT ON DATABASE dbbaudouin_martelee TO francois_petit;
GRANT CONNECT ON DATABASE dbbaudouin_martelee TO hedi_mzoughi;
GRANT CONNECT ON DATABASE dbbaudouin_martelee TO dorian_gruselin;
GRANT USAGE ON SCHEMA projet_ae TO marcin_wilk;
GRANT USAGE ON SCHEMA projet_ae TO francois_petit;
GRANT USAGE ON SCHEMA projet_ae TO hedi_mzoughi;
GRANT USAGE ON SCHEMA projet_ae TO dorian_gruselin;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA projet_ae TO marcin_wilk;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA projet_ae TO francois_petit;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA projet_ae TO hedi_mzoughi;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA projet_ae TO dorian_gruselin;

GRANT SELECT ON ALL TABLES IN SCHEMA projet_ae TO marcin_wilk;
GRANT SELECT ON ALL TABLES IN SCHEMA projet_ae TO francois_petit;
GRANT SELECT ON ALL TABLES IN SCHEMA projet_ae TO hedi_mzoughi;
GRANT SELECT ON ALL TABLES IN SCHEMA projet_ae TO dorian_gruselin;

GRANT INSERT ON TABLE projet_ae.users TO marcin_wilk;
GRANT INSERT ON TABLE projet_ae.users TO francois_petit;
GRANT INSERT ON TABLE projet_ae.users TO hedi_mzoughi;
GRANT INSERT ON TABLE projet_ae.users TO dorian_gruselin;
GRANT SELECT ON TABLE projet_ae.users TO marcin_wilk;
GRANT SELECT ON TABLE projet_ae.users TO francois_petit;
GRANT SELECT ON TABLE projet_ae.users TO hedi_mzoughi;
GRANT SELECT ON TABLE projet_ae.users TO dorian_gruselin;
GRANT USAGE,SELECT ON SEQUENCE projet_ae.users_user_id_seq TO marcin_wilk;
GRANT USAGE,SELECT ON SEQUENCE projet_ae.users_user_id_seq TO francois_petit;
GRANT USAGE,SELECT ON SEQUENCE projet_ae.users_user_id_seq TO hedi_mzoughi;
GRANT USAGE,SELECT ON SEQUENCE projet_ae.users_user_id_seq TO dorian_gruselin;
