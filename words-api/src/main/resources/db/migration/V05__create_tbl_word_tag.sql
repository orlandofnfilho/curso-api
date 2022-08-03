CREATE TABLE tb_word_tag (
  tag_id BIGINT NOT NULL,
  word_id BIGINT NOT NULL,
  PRIMARY KEY (tag_id,word_id),
  CONSTRAINT tag_id_fk FOREIGN KEY (tag_id) REFERENCES tb_tags (id),
  CONSTRAINT word_id_fk FOREIGN KEY (word_id) REFERENCES tb_words (id)
);