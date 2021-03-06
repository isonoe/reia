/*************************************************************************
 * Copyright (C) 2009/2021 - Cristiano Lehrer (cristiano@ybadoo.com.br)  *
 *                  Ybadoo - Solucoes em Software Livre (ybadoo.com.br)  *
 *                                                                       *
 * Permission is granted to copy, distribute and/or modify this document *
 * under the terms of the GNU Free Documentation License, Version 1.3 or *
 * any later version published by the  Free Software Foundation; with no *
 * Invariant Sections,  no Front-Cover Texts, and no Back-Cover Texts. A *
 * A copy of the  license is included in  the section entitled "GNU Free *
 * Documentation License".                                               *
 *                                                                       *
 * Ubuntu 16.10 (GNU/Linux 4.8.0-39-generic)                             *
 * OpenJDK Version "1.8.0_121"                                           *
 * OpenJDK 64-Bit Server VM (build 25.121-b13, mixed mode)               *
 *************************************************************************/
package com.isonoe.reia;


import com.isonoe.reia.lexical.LexicalAnalysis;
import com.isonoe.reia.lexical.Token;
import com.isonoe.reia.semantic.SemanticAnalysis;
import com.isonoe.reia.syntax.SyntaxAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Classe responsavel pelo compilador da linguagem de programacao
 * SIMPLE 15.01
 */
public class Compiler
{
    /**
     * Metodo principal da linguagem de programacao Java
     *
     * @param args argumentos da linha de comando (nao utilizado)
     */
    public static void main(String[] args) throws Exception {
//        if (args.length != 1)
//        {
//            System.err.println("Por favor, informe o arquivo a ser compilado!");
//
//            return;
//        }

        BufferedReader source = null;

        try
        {
            String basePath = new File("").getAbsolutePath();
            System.out.println(basePath);
            source = new BufferedReader(new FileReader(new File(basePath + "/src/com/isonoe/reia/assets/data/exercicio.txt")));
        }
        catch (final Exception exception)
        {
            exception.printStackTrace();
        }

        System.out.println("In??cio da an??lise l??xica");

        final LexicalAnalysis lexical = new LexicalAnalysis();

        if (!lexical.parser(source))
        {
            for (String key: lexical.getSymbolTable().keySet())
            {
                System.out.println(lexical.getSymbolTable().get(key) + " : " + key);
            }

            for (Token token: lexical.getTokens())
            {
                System.out.println(token);
            }

            System.out.println("Fim da an??lise l??xica");

            System.out.println("\nIn??cio da an??lise sint??tica");
            SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis(lexical);
            syntaxAnalysis.start();
            System.out.println("Fim da an??lise sint??tica");

            System.out.println("\nIn??cio da an??lise semantica");
            SemanticAnalysis semanticAnalysis = new SemanticAnalysis(lexical);
            semanticAnalysis.start();
            System.out.println("Fim da an??lise semantica");
        }


    }
}