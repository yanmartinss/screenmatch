package br.com.swgdigital.screenmatch.model;

public enum Category {
    ACAO("Action"),
    ROMANCE("Romance"),
    CRIME("Crime"),
    COMEDIA("Comedy"),
    DRAMA("Drama");

    private String categoryOmdb;
    Category(String categoryOmdb) {
        this.categoryOmdb = categoryOmdb;
    }

    public static Category fromString(String text) {
        for (Category c : Category.values()) {
            if (c.categoryOmdb.equalsIgnoreCase(text)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Nenhum categoria encontrada para a string fornecida: " + text);
    }
}
