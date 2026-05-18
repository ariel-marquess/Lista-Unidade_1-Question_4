# Sistema de Gestão de Acervo "Biobliotech"
   
### Contexto: 
* Você foi contratado para desenvolver o módulo principal de uma biblioteca. Este módulo é responsável por gerenciar os livros e controlar os empréstimos.

## Requisitos Funcionais

### Modelo de Dados:
* Crie uma classe para representar um Livro (ID, Título, Autor e um booleano estaEmprestado).
* Crie uma classe Biblioteca que contenha uma lista (ou array) de livros.

### Operações Obrigatórias:
* Um método para adicionar livros ao acervo.
* Um método para buscar um livro por ID.
* Um método para realizar empréstimo, que altera o status do livro para "emprestado".
* Um método para devolver o livro, voltando o status para "disponível".

### Regras de Negócio e Exceções (Obrigatório):
* O seu código deve prever falhas e lançar as seguintes exceções personalizadas:
  * __LivroNaoEncontradoException:__ Lançada quando o usuário tenta buscar, emprestar ou devolver um livro com um ID que não existe na lista.
  * __EmprestimoInvalidoException:__ Lançada quando o usuário tenta emprestar um livro que já consta como "emprestado" no sistema.
  * __LimiteAcervoException:__ Lançada se o sistema tentar adicionar mais livros do que a capacidade suportada (defina um limite, ex: 100 livros).

## O Desafio de Integração
* Na sua classe principal (Main), você deve simular o seguinte fluxo:
  * Tentar emprestar um livro que existe.
  * Tentar emprestar o mesmo livro novamente (deve cair no catch de EmprestimoInvalidoException).
  * Tentar buscar um ID inexistente (deve cair no catch de LivroNaoEncontradoException).
* __Uso do Finally:__ Em todas as tentativas de empréstimo, utilize o bloco finally para exibir a mensagem: "Sessão de atendimento finalizada."