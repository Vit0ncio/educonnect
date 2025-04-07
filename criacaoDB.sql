create database if not exists educonnect;
use educonnect;

create table usuarios (
	id int auto_increment primary key,
	nome varchar(100) not null,
	idade int,
	email varchar(100) not null unique,
	senha varchar(100) not null,
	tipo enum('aluno', 'professor', 'instituicao') not null,
	cnpj varchar(20),
	presenca boolean default false
);

create table cursos (
	id int auto_increment primary key,
	nome varchar(100) not null,
	descricao text,
	data_inicio date
);

create table inscricoes (
	usuario_id int,
	curso_id int,
	data_inscricao timestamp default current_timestamp,
	primary key (usuario_id, curso_id),
	foreign key (usuario_id) references usuarios(id),
	foreign key (curso_id) references cursos(id)
);

create table topicos_forum (
	id int auto_increment primary key,
	titulo varchar(100) not null,
	descricao text
);

create table posts_forum (
	id int auto_increment primary key,
	topico_id int,
	autor_id int,
	titulo varchar(100) not null,
	conteudo text not null,
	data_post timestamp default current_timestamp,
	fixado boolean default false,
	upvotes int default 0,
	favoritado boolean default false,
	foreign key (topico_id) references topicos_forum(id),
	foreign key (autor_id) references usuarios(id)
);

create table tarefas (
	id int auto_increment primary key,
	usuario_id int,
	titulo varchar(100) not null,
	descricao text,
	data_entrega date,
	concluido boolean default false,
	feedback text,
	arquivo_path varchar(255),
	foreign key (usuario_id) references usuarios(id)
);

create table notas (
	id int auto_increment primary key,
	usuario_id int,
	curso_id int,
	materia varchar(100) not null,
	semestre varchar(20) not null,
	nota decimal(4,2),
	foreign key (usuario_id) references usuarios(id),
	foreign key (curso_id) references cursos(id)
);

insert into usuarios (nome, idade, email, senha, tipo) values
('Aluno', 20, 'aluno@educonnect.com', 'aluno123', 'aluno'),
('Professor', 34, 'professor@educonnect.com', 'professor123', 'professor'),
('Instituicao', 11, 'instituicao@educonnect.com', 'instituicao123', 'instituicao');