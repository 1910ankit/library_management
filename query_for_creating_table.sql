drop table library.Borrower ;

-- Create table for Borrower
CREATE TABLE IF NOT EXISTS Borrower (
    id int AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Create table for Book
drop table Book;
CREATE TABLE Book (
    id int AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT false,
    borrower_id int,
    FOREIGN KEY (borrower_id) REFERENCES Borrower(id)
);
select * from library.Borrower;

select * from library.Book;
