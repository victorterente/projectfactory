package com.example.projectfactory;

public class Evento {
    private String nome;
    private String descricao;
    private String data;
    private String end;
    private String start;

    public Evento(String nome, String descricao, String data, String end, String start) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.end = end;
        this.start = start;
    }

    public String getNome() {
        return nome;
    }
    public String getDescricao() {
        return descricao;
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