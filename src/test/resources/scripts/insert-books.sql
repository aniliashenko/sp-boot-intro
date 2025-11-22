INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUES
(1, 'Book One',   'Author A', 'ISBN-001', 19.99, 'Description One', 'cover1.jpg', false),
(2, 'Book Two',   'Author B', 'ISBN-002', 25.99, 'Description Two', 'cover2.jpg', false),
(3, 'Book Three', 'Author C', 'ISBN-003', 15.99, 'Description Three', 'cover3.jpg', false);

INSERT INTO categories (id, name, is_deleted) VALUES
(1, 'Fiction', false);

INSERT INTO books_categories (book_id, category_id) VALUES
(1, 1);
