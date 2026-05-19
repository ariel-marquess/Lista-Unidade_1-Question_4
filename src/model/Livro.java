package model;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private boolean estaEmprestado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public boolean isEstaEmprestado() {
        return estaEmprestado;
    }

    public void setEstaEmprestado(boolean estaEmprestado) {
        this.estaEmprestado = estaEmprestado;
    }

    public Livro(int id, String titulo, String autor, boolean estaEmprestado) {
        setId(id);
        setTitulo(titulo);
        setAutor(autor);
        setEstaEmprestado(estaEmprestado);
    }
}
