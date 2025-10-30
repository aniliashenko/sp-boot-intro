DELETE FROM books_categories;
DELETE FROM books;
DELETE FROM categories;

ALTER TABLE books AUTO_INCREMENT = 1;
ALTER TABLE categories AUTO_INCREMENT = 1;
