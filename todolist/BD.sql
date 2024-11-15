-- Apaga o banco de dados se já existir
DROP DATABASE IF EXISTS gerenciador_tarefas;

-- Cria o banco de dados
CREATE DATABASE gerenciador_tarefas;

-- Seleciona o banco de dados para usar
USE gerenciador_tarefas;

-- Criação da tabela de usuário
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Criação do domínio para prioridade
CREATE DOMAIN prioridade AS TEXT CHECK (VALUE IN ('baixa', 'média', 'alta'));

-- Criação da tabela de tarefas
CREATE TABLE tarefa (
    id SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    setor VARCHAR(50) NOT NULL,
    prioridade prioridade NOT NULL,  -- Usando o domínio criado
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(10) NOT NULL DEFAULT 'fazer' CHECK (status IN ('a fazer', 'fazendo', 'pronto')),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

ALTER TABLE tarefa
ALTER COLUMN status SET DEFAULT 'a_fazer';

