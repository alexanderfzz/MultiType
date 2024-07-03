CREATE TABLE `user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci not null,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci not null,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci unique not null,
	`average` decimal(5, 2) not null default 0,
	`races` int(7) not null default 0,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci;

CREATE TABLE `quote`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `content` MEDIUMTEXT CHARACTER SET utf8 COLLATE utf8_general_ci not null,
	`length` int(3) not null,
	`source` varchar(50) default null,
	primary key (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci;

CREATE TABLE `record` (
	`id` int(100) not null auto_increment,
  `userId` int(10) NOT NULL,
  `quoteId` int(10) NOT NULL,
	`time` decimal(10, 3) not null,
	`wpm` decimal(5 ,2) not null,
	`date` datetime not null,
	primary key (`Id`)
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci;



insert into `user` (`username`, `password`, `email`) values
('user1', 'password1', 'email1@domain.com'),
('user2', 'password2', 'email2@domain.com'),



insert into `quote` (`content`, `length`, `source`) values
('Google En Passsant. Holy hell!', 29, 'AnarchyChess'),
('They don\'t know that we know they know we know.', 47, 'Friends'),
('If you have to ask, you will never know. If you know, you need only ask.', 72, 'Harry Potter and the Deathly Hallows'),
('You can\'t use the fire exit because you\'re not made of fire.', 60, 'Undertale'),
('So let me clue you in. I\'m not in danger, Skyler. I am the danger. A guy opens his door and gets shot. You think that of me? No, I am the one who knocks.', 153, 'Breaking Bad'),
('All your base are belong to us.', 31, 'Zero Wing'),
('Yelling incoherently solves everyone\'s problem. But if that doesn\'t work, you can always just pour milk on ducks.', 113, 'alexanderfzz'),
('Me when I have sex with a dead male fish.', 41, 'jtangkilla'),
('The ships hung in the sky in much the same way that bricks don\'t.', 65,'The Hitchhiker\'s Guide to the Galaxy');
('Link... you may not yet be at a point where you have fully recovered your power or all of your memories... But courage need not be remembered... for it is never forgotten.', 171, 'Breath of the Wild'),
('My dearest wish, is that you\'ll know, these tender thoughts that only seem to grow.', 83, 'The Edge of Dawn'),
('I\'ll tell you a riddle. You\'re waiting for a train, a train that will take you far away. You know where you hope this train will take you, but you don\'t know for sure. But it doesn\'t matter. How can it not matter to you where that train will take you?', 251, 'Inception'),
('There\'s this mountain of pure diamond. It takes an hour to climb it, and an hour to go around it. Every hundred years, a little bird comes. It sharpens its beak on the diamond mountain. And when the entire mountain is chiseled away, the first second of eternity will have passed.', 279, 'Doctor Who'),
('There are more things in Heaven and Earth, Horatio, than are dreamt of in your philosophy.', 90, 'Macbeth'),
('"Did you put your name into the Goblet of Fire, Harry?" Dumbledore asked calmly.', 80, 'Harry Potter and the Goblet of Fire'),
('I do not like green eggs and ham. I do not like them, Sam-I-Am.', 63, 'Green Eggs and Ham'),
('You don\'t need to know. The burden is... mine alone to bear.', 60, 'Future Redeemed'),
('You are simply deceiving yourselves, wishing to divine some meaning from something that is devoid of it.', 104, 'Xenoblade Chronicles 3'),
('What a horrible night to have a curse.', 38, 'Castlevania: Simon\'s Quest'),
('Delicious and vicious, while maliciously nutritious.', 52, 'Cuphead'),
('Look at that subtle off-white coloring, the tasteful thickness of it. Oh my God. It even has a watermark.', 105, 'American Psycho'),
('I have no idea to this day what those two Italian ladies were singing about. Truth is, I don\'t want to know. Some things are best left unsaid. I\'d like to think they were singing about something so beautiful, it can\'t be expressed in words, and makes your heart ache because of it.', 281, 'The Shawshank Redemption'),
('I\'ve seen things you people wouldn\'t believe. Attack ships on fire off the shoulder of Orion. I watched C-beams glitter in the dark near the Tannhauser gate. All those moments will be lost in time, like tears in rain.', 217, 'Blade Runner'),
('Proximity to power deludes some into thinking they wield it.', 60, 'House of Cards');

