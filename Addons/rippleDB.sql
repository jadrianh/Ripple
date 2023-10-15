-- Crear la base de datos si esta no existe
CREATE DATABASE IF NOT EXISTS rippleDB

-- Usar la base de datos anteriormente creada
USE rippleDB

-- Crear la tabla User
CREATE TABLE UserProfile (
    idUser INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    middleName VARCHAR(50),
    lastName VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    registerDate TIMESTAMP NOT NULL
);

-- Crear la tabla Contacts
CREATE TABLE Contacts (
    idContacts INT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    nickName VARCHAR(50),
    company VARCHAR(100),
    address VARCHAR(255),
    birthday DATE,
    notes TEXT,
    profilePicture VARCHAR(255)
);

CREATE TABLE Email (
    idEmail INT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    emailType VARCHAR(50)
);

-- Crear la tabla SocialMedia
CREATE TABLE SocialMedia (
    idSocial INT PRIMARY KEY,
    socialNetwork VARCHAR(50),
    socialUsername VARCHAR(100)
);

-- Crear la tabla Tags
CREATE TABLE Tags (
    idTags INT PRIMARY KEY,
    tagName VARCHAR(50) NOT NULL,
    tagColor VARCHAR(10)
);

-- Crear la tabla Phone
CREATE TABLE Phone (
    idPhone INT PRIMARY KEY,
    phoneNumber VARCHAR(20) NOT NULL,
    phoneSuffix VARCHAR(10) NOT NULL,
    phoneType VARCHAR(50) NOT NULL
);

-- Crear la tabla User-Contacts
CREATE TABLE User_Contacts (
    idUser INT,
    idContacts INT,
    FOREIGN KEY (idUser) REFERENCES UserProfile(idUser),
    FOREIGN KEY (idContacts) REFERENCES Contacts(idContacts)
);

-- Crear la tabla Social-Contacts
CREATE TABLE Social_Contacts (
    idSocial INT,
    idContacts INT,
    FOREIGN KEY (idSocial) REFERENCES SocialMedia(idSocial),
    FOREIGN KEY (idContacts) REFERENCES Contacts(idContacts)
);

-- Crear la tabla Tag-Contacts
CREATE TABLE Tag_Contacts (
    idTags INT,
    idContacts INT,
    FOREIGN KEY (idTags) REFERENCES Tags(idTags),
    FOREIGN KEY (idContacts) REFERENCES Contacts(idContacts)
);

-- Agregar una relaci�n con la tabla "User"
CREATE TABLE User_Email (
    idUser INT,
    idEmail INT,
    FOREIGN KEY (idUser) REFERENCES UserProfile(idUser),
    FOREIGN KEY (idEmail) REFERENCES Email(idEmail)
);

-- Agregar una relaci�n con la tabla "Contacts"
CREATE TABLE Contacts_Email (
    idContacts INT,
    idEmail INT,
    FOREIGN KEY (idContacts) REFERENCES Contacts(idContacts),
    FOREIGN KEY (idEmail) REFERENCES Email(idEmail)
);

-- Crear la tabla Phone-Contacts
CREATE TABLE Phone_Contacts (
    idPhone INT,
    idContacts INT,
    FOREIGN KEY (idPhone) REFERENCES Phone(idPhone),
    FOREIGN KEY (idContacts) REFERENCES Contacts(idContacts)
);
