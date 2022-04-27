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
            }else if(fnToken.getType() == Symbol.LET){
                this.funcLet(novalinha);
            }else if(fnToken.getType() == Symbol.PRINT){
                this.funcPrint(novalinha);
            }else if(fnToken.getType() == Symbol.GOTO){
                this.funcGoto(novalinha);
            }else if(fnToken.getType() == Symbol.IF){
                this.funcIf(novalinha);
            }else if(fnToken.getType() == Symbol.END){
                this.funcEnd(novalinha);
            }

        }

    }

    public void funcEnd(ArrayList<Token> linha) throws Exception {

        if (linha.size() != 2) {
            throw new Exception("Instrucao Input mal formatada: linha " + linha.get(0).getLine() + ". padrao: input <variable>");
        }

    }

    public void funcInput(ArrayList<Token> linha) throws Exception {

        if (linha.size() != 3) {
            throw new Exception("Instrucao Input mal formatada: linha " + linha.get(0).getLine() + ". padrao: input <variable>");
        }

        checkVariavel(linha.get(2));
    }

    public void funcGoto(ArrayList<Token> linha) throws Exception {

        if (linha.size() != 3) {
            throw new Exception("Instrucao Go To mal formatada: linha " + linha.get(0).getLine() + ". padrao: input <variable>");
        }

        checkCte(linha.get(2));
    }

    public void funcPrint(ArrayList<Token> linha) throws Exception {

        if (linha.size() != 3) {
            throw new Exception("Instrucao Print mal formatada: linha " + linha.get(0).getLine() + ". padrao: input <variable>");
        }

        checkVariavel(linha.get(2));
    }

    public void funcIf(ArrayList<Token> linha) throws Exception {

        if (linha.size() != 7) {
            throw new Exception("Instrucao If mal formatada: linha " + linha.get(0).getLine() + ". padrao: input <variable>");
        }

        checkVariavelOuCte(linha.get(2));
        checkRelatedOperator(linha.get(3));
        checkVariavelOuCte(linha.get(4));
        checkGoto(linha.get(5));
        checkCte(linha.get(6));

    }

    public void funcLet(ArrayList<Token> linha) throws Exception {

        if (linha.size() < 5 || linha.size() > 7) {
            throw new Exception("Instrucao Let mal formatada: linha " + linha.get(0).getLine() + ". padrao: input <variable>");
        }

        checkVariavel(linha.get(2));
        checkAtribuicao(linha.get(3));

        if(linha.size() == 5){
            this.checkVariavelOuCte(linha.get(4));
        }else if(linha.size() == 6){
            this.checkNegativeNumber(linha.get(4),linha.get(5));
        }else if(linha.size() == 7){
            this.checkAritmeticExpression(linha.get(4),linha.get(5),linha.get(6));
        }
    }

    public void checkIdLinha(Token token) throws Exception {
        if (token.getType() != Symbol.INTEGER) {
            throw new Exception("Identificador de linha nao encontrado: " + token);
        }
    }

    public void checkAtribuicao(Token token) throws Exception {
        if (token.getType() != Symbol.ASSIGNMENT) {
            throw new Exception("Atribuicao nao encontrada: " + token);
        }
    }

    public void checkGoto(Token token) throws Exception {
        if (token.getType() != Symbol.GOTO) {
            throw new Exception("Pulo (Go To) nao encontrada: " + token);
        }
    }

    public void checkVariavel(Token token) throws Exception {
        if (token.getType() != Symbol.VARIABLE) {
            throw new Exception("Variavel nao encontrada: " + token);
        }
    }

    public void checkCte(Token token) throws Exception {
        if (token.getType() != Symbol.INTEGER) {
            throw new Exception("Variavel nao encontrada: " + token);
        }
    }

    public void checkAritmeticExpression(Token token,Token token2,Token token3) throws Exception {
        this.checkVariavelOuCte(token);
        this.checkAritmeticOperator(token2);
        this.checkVariavelOuCte(token3);
        this.checkDivideZero(token2,token3);
    }

    public void checkAritmeticOperator(Token token) throws Exception{
        ArrayList<Symbol> operators = new ArrayList<>(Arrays.asList(
                Symbol.ADD,
                Symbol.SUBTRACT,
                Symbol.MULTIPLY,
                Symbol.DIVIDE,
                Symbol.MODULO
        ));

        if (!operators.contains(token.getType())) {
            throw new Exception("Instrucao mal formatada: operador aritmetico nao encontrado, linha " + token);
        }
    }

    public void checkRelatedOperator(Token token) throws Exception{
        ArrayList<Symbol> operators = new ArrayList<>(Arrays.asList(
                Symbol.GT,
                Symbol.GE,
                Symbol.LT,
                Symbol.LE,
                Symbol.EQ,
                Symbol.NE
        ));

        if (!operators.contains(token.getType())) {
            throw new Exception("Instrucao mal formatada: operador relacional nao encontrado, linha " + token);
        }
    }

    public void checkDivideZero(Token token,Token token2) throws Exception {
        if(token.getType() == Symbol.DIVIDE && this.lexical.getSymbolTable().get("0") == token2.getAddress()){
            throw new Exception("Divisao por zero negada: " + token);
        }

    }

    public void checkNegativeNumber(Token token,Token token2) throws Exception {
        if (token.getType() != Symbol.SUBTRACT) {
            throw new Exception("Simbolo negativo nao encontrado: " + token);
        }
        this.checkVariavelOuCte(token2);
    }

    public void checkVariavelOuCte(Token token) throws Exception {
        if (token.getType() != Symbol.VARIABLE && token.getType() != Symbol.INTEGER) {
            throw new Exception("Variavel ou constante nao encontrada: " + token);
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
