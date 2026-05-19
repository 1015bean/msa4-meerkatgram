ALTER TABLE posts
ADD CONSTRAINT fk_posts_user_id
FOREIGN KEY (user_id)
REFERENCES users(id)
;
-- 열심히 해 예빈언니
