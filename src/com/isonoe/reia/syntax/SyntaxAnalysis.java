package com.isonoe.reia.syntax;

import com.isonoe.reia.lexical.LexicalAnalysis;
import com.isonoe.reia.lexical.Symbol;
import com.isonoe.reia.lexical.Token;

import java.util.ArrayList;
import java.util.Arrays;

public class SyntaxAnalysis {
    LexicalAnalysis lexical;

    boolean isValid;

    public SyntaxAnalysis(LexicalAnalysis lexical) {
        this.lexical = lexical;
        this.isValid = true;
    }

    public void start() throws Exception {

        ArrayList<ArrayList<Token>> linhas = new ArrayList<>();

        ArrayList<Token> linha = new ArrayList<>();

        for (Token token : lexical.getTokens()) {
            if (token.getType() == Symbol.LF) {
                linhas.add(linha);
                linha = new ArrayList<>();
            } else {
                linha.add(token);
            }

            System.out.println(token);
        }

        if (linha.size() > 0) {
            linhas.add(linha);
        }

        for (ArrayList<Token> novalinha : linhas) {
            Token idToken = novalinha.get(0);
            checkIdLinha(idToken);

            Token fnToken = novalinha.get(1);
            isFunction(fnToken);

            if (fnToken.getType() == Symbol.INPUT) {
                this.funcInput(novalinha);
            }
        }

    }

    public void funcInput(ArrayList<Token> linha) throws Exception {
        checkIdLinha(linha.get(0));

        if (linha.size() != 3) {
            throw new Exception("Instrucao Input mal formatada: linha " + linha.get(0).getLine() + ". padrao: input <variable>");
        }

        checkVariavel(linha.get(2));
    }

    public void checkIdLinha(Token token) throws Exception {
        if (token.getType() != Symbol.INTEGER) {
            throw new Exception("Identificador de linha nao encontrado: " + token);
        }
    }

    public void checkVariavel(Token token) throws Exception {
        if (token.getType() != Symbol.VARIABLE) {
            throw new Exception("Variavel nao encontrada: " + token);
        }
    }

    public void isFunction(Token token) throws Exception {
        ArrayList<Symbol> functions = new ArrayList<>(Arrays.asList(
                Symbol.REM,
                Symbol.INPUT,
                Symbol.LET,
                Symbol.PRINT,
                Symbol.GOTO,
                Symbol.IF,
                Symbol.END
        ));

        if (!functions.contains(token.getType())) {
            throw new Exception("Instrucao mal formatada: funcao nao encontrada, linha " + token.getLine());
        }
    }


}
