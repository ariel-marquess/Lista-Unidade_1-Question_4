create database biblioteca;

use biblioteca;

create table livro (
	id int(11) not null auto_increment,
    titulo varchar(50) not null,
    autor varchar(20) not null,
    estaOcupado boolean not null,
    primary key (id)
);

insert into livro(titulo, autor, estaOcupado) values
    ('Assim Falou Zaratustra', 'Nietzsche', true),
    ('Angústia', 'Graciliano Ramos', false),
    ('Irmãos Karamázov', 'Fiódor Dostoiévsk', false),
    ('A Náusea', 'Jean-Paul Sartre', true),
    ('A Hora da Estrela', 'Clarice Lispector', false);

select * from livro;
