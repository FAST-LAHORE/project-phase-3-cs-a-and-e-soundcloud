CREATE TABLE Account 
(
    id int NOT NULL primary key GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    name varchar(50),
    email varchar(40),
    mobile_number varchar(20),
    password varchar(40),
    blocked_until varchar(15)
);
CREATE TABLE Admin 
(
    AdminID int REFERENCES Account(id),
    PRIMARY KEY(AdminID)
);
CREATE TABLE SubscriptionManager 
(
    SubManID int PRIMARY KEY REFERENCES ACCOUNT(id)
);
CREATE TABLE Users
(	
    UserID int primary key GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
	Account_ID int REFERENCES ACCOUNT(id)
);
CREATE TABLE SubscriptionPackages 
(
    id int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    package_name varchar(10) NOT NULL,
    price double NOT NULL
);
CREATE TABLE CustomerService 
(	
    CSID int PRIMARY KEY REFERENCES ACCOUNT(id)
);
CREATE TABLE Feedback 
(
    feedbackID int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    feedback_details varchar(255) NOT NULL,
    feedback_date varchar(15) NOT NULL, 
    UserID int REFERENCES Users(UserID)
);
CREATE TABLE ResolvedFeedback 
(
    resolvedID int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    feedback_details varchar(255) NOT NULL,
    resolve_date varchar(15) NOT NULL, 
    AdminID int REFERENCES Admin(AdminID)
);
CREATE TABLE SongManager 
(	
    SMID int PRIMARY KEY REFERENCES ACCOUNT(id)
);
CREATE TABLE Songs 
(
	song_id int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
	singer varchar(255) NOT NULL,
	title varchar(255) NOT NULL,
	added_by varchar(255) NOT NULL,
	premium int NOT NULL,	
	path_to_file varchar(255) NOT NULL
);
CREATE TABLE Playlist 
(
    playlist_id int NOT NULL,
	song_id int NOT NULL references Songs(song_id)
);
CREATE TABLE UserPlaylist 
(
    playlist_name varchar(50) NOT NULL,	
	playlist_id int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
	user_id int NOT NULL references USERS(USERID)
);

