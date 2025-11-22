INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (1, 'test@example.com', 'password', 'Test', 'User', false);

INSERT INTO shopping_carts (user_id, is_deleted) VALUES (1, 0);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (100, 'Test Book', 'Author Test', 'ISBN-TEST-1', 9.99, 'Description', 'cover.jpg', false);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity) VALUES (10, 1, 100, 1);
