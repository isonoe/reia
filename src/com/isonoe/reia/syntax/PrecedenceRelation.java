package com.isonoe.reia.syntax;

import lombok.Getter;

public enum PrecedenceRelation {

    MAIOR   ("1", ">"),
    MENOR ("-1", "<"),
    IGUAL("0", "="),
    ACEITA("$$", "$$");

    @Getter
    private final String codigo;

    @Getter
    private final String symbol;

    PrecedenceRelation(final String codigo, final String symbol) {
        this.codigo = codigo;
        this.symbol = symbol;
    }
}
