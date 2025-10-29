INSERT INTO categories (id, name, is_deleted) VALUES
(1, 'Fiction', false);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUES
(100, 'Book A', 'Author A', 'ISBN-AAA', 10.00, 'Desc A', 'a.jpg', false),
(200, 'Book B', 'Author B', 'ISBN-BBB', 12.00, 'Desc B', 'b.jpg', false);

INSERT INTO books_categories (book_id, category_id) VALUES
(100, 1),
(200, 1);
