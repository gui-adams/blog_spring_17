ALTER TABLE post_category
DROP CONSTRAINT fk_post_category_category;

ALTER TABLE post_category
ADD CONSTRAINT fk_post_category_category
FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE;
