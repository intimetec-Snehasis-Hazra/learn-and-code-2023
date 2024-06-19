Create Database cafeteria;

Use cafeteria;

CREATE TABLE Users (
    employeeId VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    role VARCHAR(50)
);

CREATE TABLE MenuItems (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(255),
    price FLOAT,
    availability BIT
);

CREATE TABLE Feedbacks (
    id INT IDENTITY(1,1) PRIMARY KEY,
    comment TEXT,
    rating INT,
    feedbackDate DATE,
    itemId INT,
    userId VARCHAR(255),
    FOREIGN KEY (itemId) REFERENCES MenuItems(id),
    FOREIGN KEY (userId) REFERENCES Users(employeeId)
);

CREATE TABLE Notifications (
    id INT IDENTITY(1,1) PRIMARY KEY,
    message TEXT,
    date DATE,
    userId VARCHAR(255),
    FOREIGN KEY (userId) REFERENCES Users(employeeId)
);

CREATE TABLE FoodRecommendations (
    id INT IDENTITY(1,1) PRIMARY KEY,
    recommendationDate DATE
);
