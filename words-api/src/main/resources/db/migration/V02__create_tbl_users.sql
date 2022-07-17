CREATE TABLE tb_perfis(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
nome VARCHAR(255) NOT NULL);

CREATE TABLE tb_users
(
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   nome VARCHAR (255) NOT NULL,
   email VARCHAR (255) NOT NULL,
   password VARCHAR (255) NOT NULL,
   perfil_id BIGINT NOT NULL,
   FOREIGN KEY (perfil_id) REFERENCES tb_perfis (id)
);