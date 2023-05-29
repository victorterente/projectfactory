package com.example.projectfactory;

public class Evento {
    private String nome;
    private String descricao;
    private String data;
    private String end;
    private String start;
    private String id;

    public Evento(String nome, String descricao, String data, String end, String start, String id) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.end = end;
        this.start = start;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public String getDescricao() {
        return descricao;
    }
public String getId(){
        return id;
}
    public String getData() {
        return data;
    }
    public String getStart() {
        return start;
    }
    public String getEnd() {
        return end;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
}