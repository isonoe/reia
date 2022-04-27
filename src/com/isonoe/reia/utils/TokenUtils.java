package com.isonoe.reia.utils;

import com.isonoe.reia.lexical.Symbol;
import com.isonoe.reia.lexical.Token;

import java.util.ArrayList;
import java.util.List;

public class TokenUtils {

    static public ArrayList<ArrayList<Token>> getLinesList(List<Token> tokens){
        ArrayList<ArrayList<Token>> linhas = new ArrayList<>();

        ArrayList<Token> linha = new ArrayList<>();

        for (Token token : tokens) {
            if (token.getType() == Symbol.LF) {
                linhas.add(linha);
                linha = new ArrayList<>();
            } else {
                linha.add(token);
            }

            // System.out.println(token);
        }

        if (linha.size() > 0) {
            linhas.add(linha);
        }

        return linhas;
    }
}
