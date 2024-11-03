CREATE TABLE post_category (
    post_id INTEGER,
    category_id INTEGER,
    CONSTRAINT fk_post_category_post FOREIGN KEY (post_id) REFERENCES post(id),
    CONSTRAINT fk_post_category_category FOREIGN KEY (category_id) REFERENCES category(id),
    PRIMARY KEY (post_id, category_id)
);
