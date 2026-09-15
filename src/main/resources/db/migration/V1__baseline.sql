CREATE TABLE usuario (
                         id UUID PRIMARY KEY,
                         nome VARCHAR(255),
                         email VARCHAR(255) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL
);

CREATE TABLE conta (
                       id UUID PRIMARY KEY,
                       numero VARCHAR(255) UNIQUE,
                       agencia VARCHAR(255),
                       saldo NUMERIC(19,2),
                       usuario_id UUID UNIQUE REFERENCES usuario(id)
);

CREATE TABLE transacao (
                           id UUID PRIMARY KEY,
                           valor NUMERIC(19,2),
                           descricao VARCHAR(255),
                           tipo VARCHAR(30),
                           data_hora TIMESTAMP,
                           conta_id UUID REFERENCES conta(id)
);