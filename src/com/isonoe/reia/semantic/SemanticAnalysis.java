package com.isonoe.reia.semantic;

import com.isonoe.reia.lexical.LexicalAnalysis;
import com.isonoe.reia.lexical.Token;
import com.isonoe.reia.utils.TokenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SemanticAnalysis {

    LexicalAnalysis lexical;

    boolean isValid;

    public SemanticAnalysis(LexicalAnalysis lexical) {
        this.lexical = lexical;
    }

    public void start() throws Exception {
        validateIncreasingAddresses();
    }

    public void validateIncreasingAddresses() throws Exception {
        ArrayList<ArrayList<Token>> linhas = TokenUtils.getLinesList(lexical.getTokens());

        boolean isValidOrder = true;

        int lastValue = 0;

        for (ArrayList<Token> novalinha : linhas) {
            int address = novalinha.get(0).getAddress();
            int value = Integer.valueOf(getKeyFromValue(address));

            if (value > lastValue) {
                lastValue = value;
            }else{
                isValidOrder = false;
                break;
            }
        }

        if (!isValidOrder) {
            throw new Exception("Ordem de enderecos invalida.");
        }
    }

    public String getKeyFromValue(int value){

        Map<String, Integer> map = lexical.getSymbolTable();

        if (lexical.getSymbolTable().containsValue(value)) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (Objects.equals(entry.getValue(), value)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}
