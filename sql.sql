DROP DATABASE ProjetoCadastroCurriculoSenai;

CREATE DATABASE ProjetoCadastroCurriculoSenai;
USE ProjetoCadastroCurriculoSenai;

CREATE TABLE palavra_chave(
	codigo INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(40));
 
 CREATE TABLE curso(
	codigo INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(40)
    );
 
 
 CREATE TABLE aluno(
	codigo Integer NOT NULL PRIMARY KEY AUTO_INCREMENT,
	cpf VARCHAR(100) UNIQUE,
    nome VARCHAR(100),
    data_nasc DATE,
    curso INTEGER NOT NULL,
    turma VARCHAR(100),
    senha VARCHAR(100),
    email VARCHAR(100),
    situacao CHAR(1) DEFAULT 'A',
    FOREIGN KEY (curso) REFERENCES curso(codigo)
    );
    
CREATE TABLE aluno_palavra_chave(
	codigo INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    codigo_aluno INT NOT NULL,
    codigo_palavra_chave INT NOT NULL,
    FOREIGN KEY(codigo_aluno) REFERENCES aluno(codigo),
    FOREIGN KEY(codigo_palavra_chave) REFERENCES palavra_chave(codigo));
    
    INSERT INTO palavra_chave(nome) VALUES ("Primeiro emprego"), ("Com experiencia"),("Sem experiencia"),("Pró-ativo"),("Trabalho em grupo"),("Comunicativo"); 

    INSERT INTO CURSO(nome) VALUES ("Tecnico em informatica"), ("Almoxarifado"),("Eletronica"),("Automação"),("Qualidade"),("Excel Avançado");

    INSERT INTO aluno(cpf, nome, data_nasc, curso, turma, senha, email) VALUES 
    ("19562468860", "Sebastiao Donizete Vida", sysdate(), 1, "1A", "1234", "sebastiaovida1998@outlook.com"),
    ("46939417869", "Donizete Junior Ribeiro Vida", sysdate(), 3, "3INF", "1234", "donizetevida1998@outlook.com");
    
    
    UPDATE aluno SET email = "AAA", situacao = "I", senha = "989898" WHERE codigo = 1;
    
    UPDATE aluno SET curso = 2 WHERE codigo = 1;
	SELECT * FROM aluno;
	SELECT * FROM curso;
    SELECT * FROM palavra_chave;

   
   UPDATE aluno_palavra_chave SET codigo_aluno = 1, codigo_palavra_chave  = 2 WHERE codigo = 1;
   
   SELECT apc.codigo FROM aluno_palavra_chave apc WHERE apc.codigo_aluno = 2;
   SELECT * FROM aluno_palavra_chave;
   
   
   
   SELECT a.nome FROM aluno AS a;
   
   
   SELECT a.*, c.nome AS nomeCurso, c.codigo AS codigoCurso FROM aluno AS a INNER JOIN curso AS c ON c.codigo = a.curso;
   
   
    SELECT a.codigo, a.cpf, a.nome, a.data_nasc, c.nome AS nomeCurso, c.codigo AS codigoCurso, a.turma, a.senha FROM aluno AS a, curso AS c WHERE a.nome LIKE "DONIZETE" AND c.codigo = (SELECT a.curso FROM aluno AS a WHERE a.nome LIKE "DONIZETE");
    SELECT a.codigo, a.cpf, a.nome, a.data_nasc, c.nome AS curso, a.turma, a.senha FROM aluno AS a, curso AS c WHERE a.cpf LIKE "46939417869" AND c.codigo = (SELECT a.curso FROM aluno AS a WHERE a.cpf LIKE "46939417869");
    

    SELECT a.*, c.nome AS nomeCurso, c.codigo AS codigoCurso FROM aluno_palavra_chave AS apc
    INNER JOIN aluno AS a ON a.codigo = apc.codigo_aluno
    INNER JOIN curso AS c ON c.codigo = a.curso
	WHERE apc.codigo_palavra_chave = (SELECT p.codigo FROM palavra_chave AS p WHERE p.nome LIKE "Medico");
    
	SELECT a.*, c.nome AS nomeCurso, c.codigo AS codigoCurso FROM aluno AS a
    INNER JOIN curso AS c ON c.codigo = a.curso
    WHERE c.nome LIKE "Psicologia";
    
    SELECT a.*, c.nome AS nomeCurso, c.codigo AS codigoCurso FROM aluno AS a
    INNER JOIN curso AS c ON c.codigo = a.curso
    WHERE c.nome LIKE "Psicologia";
   

    

    
    INSERT INTO aluno_palavra_chave VALUES(0, 13, 1);
    SELECT * FROM aluno_palavra_chave;
    DELETE FROM aluno_palavra_chave WHERE codigo_aluno = 13;
    
    SELECT pc.nome FROM aluno_palavra_chave AS apc 
    INNER JOIN palavra_chave AS pc ON pc.codigo = apc.codigo_palavra_chave
    WHERE apc.codigo_aluno = 13;
    
    