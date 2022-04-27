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

        }

        if (linha.size() > 0) {
            linhas.add(linha);
        }

        for (ArrayList<Token> novalinha : linhas) {
            ArrayList<Token> sublinha = new ArrayList<>(novalinha);

            Token idToken = sublinha.remove(0);

            checkIdLinha(idToken);

            Token fnToken = sublinha.get(0);
            isFunction(fnToken);

            if (fnToken.getType() == Symbol.INPUT) {
                this.funcInput(sublinha);
            } else if (fnToken.getType() == Symbol.GOTO) {
                this.funcGoto(sublinha);
            } else if (fnToken.getType() == Symbol.PRINT) {
                this.funcPrint(sublinha);
            } else if (fnToken.getType() == Symbol.LET) {
                this.funcLet(sublinha);
            }
        }

    }

    public void funcInput(ArrayList<Token> linha) throws Exception {
        try {
            if (linha.size() != 2) {
                throw new Exception(" Linha " + linha.get(0).getLine());
            }

            validateVariavel(linha.get(1));
        } catch (Exception e) {
            throw new Exception("Instrucao input mal formatada. padrao esperado: input <variable>. " + e.getMessage());
        }
    }

    public void funcGoto(ArrayList<Token> linha) throws Exception {
        try {

            if (linha.size() != 2) {
                throw new Exception("linha " + linha.get(0).getLine() + ". ");
            }

            validateIdentificador(linha.get(1));

        } catch (Exception e) {
            throw new Exception("Instrucao goto mal formatada. padrao esperado: goto <endereco>. " + e.getMessage());
        }
    }

    public void funcPrint(ArrayList<Token> linha) throws Exception {
        try {
            if (linha.size() != 2) {
                throw new Exception(" Linha " + linha.get(0).getLine());
            }

            validateVariavel(linha.get(1));
        } catch (Exception e) {
            throw new Exception("Instrucao print mal formatada. padrao esperado: print <variable>. " + e.getMessage());
        }
    }

    public void funcLet(ArrayList<Token> linha) throws Exception {
        try {
            if (linha.size() < 4 || linha.size() > 6) {
                throw new Exception(" Linha " + linha.get(0).getLine());
            }

            linha.remove(0);

            validateVariavel(linha.remove(0));

            validateAssignmentToken(linha.remove(0));

            validateNumericExpression(linha);

        } catch (Exception e) {
            throw new Exception("Instrucao let mal formatada. padrao esperado: let <variable> = <valor> | <expressao>. " + e.getMessage());
        }
    }

    public void checkIdLinha(Token token) throws Exception {
        try {
            validateIdentificador(token);
        } catch (Exception e) {
            throw new Exception("Identificador de linha nao encontrado." + getTokenDescriptionToMessage(token));
        }
    }

    public void validateIdentificador(Token token) throws Exception {
        if (!isInteiro(token)) {
            throw new Exception("Identificador invalido: " + getTokenDescriptionToMessage(token));
        }
    }

    public boolean isInteiro(Token token) {
        if (token.getType() == Symbol.INTEGER) {
            return true;
        }
        return false;
    }

    public void validateVariavel(Token token) throws Exception {
        if (!isVariavel(token)) {
            throw new Exception("Variavel nao encontrada: " + getTokenDescriptionToMessage(token));
        }
    }

    public boolean isVariavel(Token token) {
        if (token.getType() == Symbol.VARIABLE) {
            return true;
        }
        return false;
    }

    public void isFunction(Token token) throws Exception {
        ArrayList<Symbol> functions = new ArrayList<>(Arrays.asList(
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

    public String getTokenDescriptionToMessage(Token token) {
        StringBuilder message = new StringBuilder();
        message.append("Linha: ");
        message.append(token.getLine());
        message.append(" Coluna: ");
        message.append(token.getColumn());
        message.append(". Token encontrado: ");
        message.append(token);
        message.append(". Tipo: ");
        message.append(token.getType().name());
        return message.toString();
    }

    public void validateOperadorAri(Token token) throws Exception {
        if (isOperadorAri(token)) {
            throw new Exception("Operador aritmetico invalido: " + token);
        }
    }

    public void validateInteiro(Token token) throws Exception {
        if (isOperadorAri(token)) {
            throw new Exception("valor Inteiro esperado. Token encontrado: "
                    + token.getType().name() + " -> " + token);
        }
    }

    public boolean isOperadorAri(Token token) {
        ArrayList<Symbol> operadoresAri = new ArrayList<>(Arrays.asList(
                Symbol.ADD,
                Symbol.SUBTRACT,
                Symbol.MULTIPLY,
                Symbol.MODULO,
                Symbol.DIVIDE
        ));

        if (operadoresAri.contains(token.getType())) {
            return true;
        }
        return false;
    }

    public boolean isOperadorBoolean(Token token) {
        ArrayList<Symbol> operadoresBoolean = new ArrayList<>(Arrays.asList(
                Symbol.GT,
                Symbol.LT,
                Symbol.EQ,
                Symbol.GE,
                Symbol.LE,
                Symbol.NE
        ));

        if (operadoresBoolean.contains(token.getType())) {
            return true;
        }
        return false;
    }


    public void validateAssignmentToken(Token token) throws Exception {
        if (token.getType() != Symbol.ASSIGNMENT) {
            throw new Exception("Simbolo de atribuicao invalido, simbolo encontrdo: " + token.getType().name() + " -> " + token);
        }
    }

    public void validateNumericExpression(ArrayList<Token> linha) throws Exception {
        boolean isNumericExpression = true;
        if (linha.size() == 1) {
            Token token = linha.get(0);
            if (token.getType() != Symbol.VARIABLE &&
                    token.getType() != Symbol.INTEGER) {
                isNumericExpression = false;
            }
        } else if (linha.size() == 2) {
            Token firsttoken = linha.get(0);
            if (firsttoken.getType() != Symbol.SUBTRACT) {
                throw new Exception("Token esperado :  " + Symbol.SUBTRACT.name() + ". Ou expressao valida. token encontrado: "
                        + firsttoken.getType().name() + " -> " + firsttoken);
            } else {
                Token second = linha.get(1);
                if (!isInteiro(second)) {
                    validateInteiro(second);
                }
            }
        } else if (linha.size() == 3) {
            Token first = linha.get(0);
            Token second = linha.get(1);
            Token third = linha.get(2);

            if (!isInteiro(first) && !isVariavel(first)) {
                throw new Exception("Token esperado :  " + Symbol.VARIABLE.name() + " ou " + Symbol.INTEGER.name() + ". token encontrado: "
                        + first.getType().name() + " -> " + first);
            }

            if (!isOperadorAri(second)) {
                throw new Exception("Operaror aritmetico esperado. Token encontrado: "
                        + second.getType().name() + " -> " + second);
            }

            if (!isInteiro(third) && !isVariavel(third)) {
                throw new Exception("Token esperado :  " + Symbol.VARIABLE.name() + " ou " + Symbol.INTEGER.name() + ". token encontrado: "
                        + third.getType().name() + " -> " + third);
            }
        }
    }

    public void validateBooleanExpression(ArrayList<Token> linha) throws Exception {
        if (linha.size() == 3) {
            Token first = linha.get(0);
            Token second = linha.get(1);
            Token third = linha.get(2);

            if (!isInteiro(first) && !isVariavel(first)) {
                throw new Exception("Token esperado :  " + Symbol.VARIABLE.name() + " ou " + Symbol.INTEGER.name() + ". token encontrado: "
                        + first.getType().name() + " -> " + first);
            }

            if (!isOperadorBoolean(second)) {
                throw new Exception("Operaror boleano esperado. Token encontrado: "
                        + second.getType().name() + " -> " + second);
            }

            if (!isInteiro(third) && !isVariavel(third)) {
                throw new Exception("Token esperado :  " + Symbol.VARIABLE.name() + " ou " + Symbol.INTEGER.name() + ". token encontrado: "
                        + third.getType().name() + " -> " + third);
            }
        }else{
            throw new Exception("Expressao boleana invalida. Padrao: <varable> |<integer> <operador_boolean>  <varable> |<integer>. Linha: " +linha.get(0).getLine());
        }
    }
}
