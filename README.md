# kyrosb-01
Backend CadastroAPI

O projeto da API é spring boot, usei intellij IDE para programar ele.
Comunica com um banco MySQL na porta 3306 local. 
Utilizei o Wampserver64 para criar o servidor e o MySql Workbench para criar o database e tabelas:
Segue o SQL utilizado para criação:

CREATE DATABASE basec;

USE basec;

CREATE TABLE Cliente (
    cpf varchar(11) NOT NULL,
    nome varchar(255) NOT NULL,
    data_nascimento varchar(255) NOT NULL,
    email varchar(255),
    telefone varchar(255),
	CONSTRAINT PK_Cliente PRIMARY KEY (cpf)
);

CREATE TABLE Endereco (
	id int auto_increment not null,
    cep int NOT NULL,
    logradouro varchar(255) NOT NULL,    
	numero int NOT NULL,
    bairro varchar(255),
    cidade varchar(255) NOT NULL,
    complemento varchar(255),
    uf varchar(10) NOT NULL,
    cpf_cliente varchar(11) NOT NULL,
    is_primario char(1),
	CONSTRAINT PK_Enderecos PRIMARY KEY (id),
    CONSTRAINT FK_Enderecos FOREIGN KEY (cpf_cliente) REFERENCES cliente(cpf)
);

Dessa forma no Spring as propriedades da aplicação estao definidas como:

spring.datasource.url=jdbc:mysql://localhost:3306/basec
spring.datasource.username=root
spring.datasource.password=

Sem senha.

