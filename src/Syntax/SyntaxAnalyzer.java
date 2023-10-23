package SecondSemantic.Syntax;

import SecondSemantic.Lexical.LexicalAnalyzer;
import SecondSemantic.Lexical.LexicalException;
import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.*;
import SecondSemantic.Semantic.Nodes.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class SyntaxAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Token tokenActual;
    private boolean verbose = false;
    private SymbolTable symbolTable;
    private Token currentMemberType, currentMemberStatic;
    private ArrayList<Token> currentMemberId;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) {

        this.lexicalAnalyzer = lexicalAnalyzer;
        this.currentMemberId = new ArrayList<Token>();
    }

    public void analyze() throws LexicalException, SyntaxException, IOException, SemanticException {
        symbolTable = new SymbolTable();
        tokenActual = lexicalAnalyzer.getNextToken();
        inicial();
    }

    private void match(String tokenName) throws SyntaxException, LexicalException, IOException {
        if (tokenName.equals(tokenActual.getName())){
            String oldTokenName = tokenActual.getName();
            tokenActual = lexicalAnalyzer.getNextToken();
            print("Se hizo match con: "+oldTokenName+" | Token nuevo: " + tokenActual.getName());
        }else {
            print("Error en match");
            throw new SyntaxException(lexicalAnalyzer.getLine(), tokenName, tokenActual.getLexeme());
        }
    }

    private void inicial() throws LexicalException, SyntaxException, IOException, SemanticException {
        listaClases();
        symbolTable.check();
        match("EOF");
    }

    private void listaClases() throws SyntaxException, LexicalException, IOException, SemanticException {
        print("Entre en listaClases");
        if (tokenActual.getName().equals("keyword_class") || tokenActual.getName().equals("keyword_interface")){
            clase();
            listaClases();
        } else {
            //Epsilon
        }
    }

    private void clase() throws SyntaxException, LexicalException, IOException, SemanticException {
        print("Entre en clase");
        if (tokenActual.getName().equals("keyword_class")){
            claseConcreta();
        } else if (tokenActual.getName().equals("keyword_interface")){
            interfaceConcreta();
        } else {
            print("Error en clase");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "class o interface", tokenActual.getLexeme());
        }
    }

    private void claseConcreta() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en claseConcreta");
        match("keyword_class");
        Token idClass = tokenActual;
        match("idClass");
        genericoConID();
        symbolTable.currentClass = new ConcreteClass(idClass, symbolTable);
        herenciaOpcional();
        match("punctuator_{");
        listaMiembros();
        match("punctuator_}");
        symbolTable.addClass(symbolTable.currentClass);
    }

    private void interfaceConcreta() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en interfaceConcreta");
        match("keyword_interface");
        Token idClass = tokenActual;
        match("idClass");
        genericoConID();
        symbolTable.currentClass = new ConcreteClass(idClass, symbolTable);
        extiendeOpcional();
        match("punctuator_{");
        listaEncabezados();
        match("punctuator_}");
        symbolTable.addInterface(symbolTable.currentClass);
    }

    private void herenciaOpcional() throws LexicalException, SyntaxException, IOException {
        print("Entre en herenciaOpcional");
        if (tokenActual.getName().equals("keyword_extends")){
            heredaDe();
        } else if (tokenActual.getName().equals("keyword_implements")){
            implementaA();
        } else {
            //Epsilon
        }
    }

    private void heredaDe() throws SyntaxException, LexicalException, IOException {
        print("Entre en heredaDe");
        if (tokenActual.getName().equals("keyword_extends")){
            match("keyword_extends");
            Token idClass = tokenActual;
            match("idClass");
            genericoOpcional();
            symbolTable.currentClass.setExtendsName(idClass);
        } else {
            print("Error en heredaDe");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "extends", tokenActual.getLexeme());
        }
    }

    private void implementaA() throws SyntaxException, LexicalException, IOException {
        print("Entre en implementaA");
        if (tokenActual.getName().equals("keyword_implements")){
            match("keyword_implements");
            Token idClass = tokenActual;
            match("idClass");
            genericoOpcional();
            symbolTable.currentClass.setImplementsName(idClass);
        } else {
            print("Error en implementaA");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "implements", tokenActual.getLexeme());
        }
    }

    private void extiendeOpcional() throws LexicalException, SyntaxException, IOException {
        print("Entre en extiendeOpcional");
        if (tokenActual.getName().equals("keyword_extends")){
            match("keyword_extends");
            Token idClass = tokenActual;
            match("idClass");
            genericoOpcional();
            symbolTable.currentClass.setExtendsName(idClass);
        } else {
            //Epsilon
            symbolTable.currentClass.setExtendsName(new Token("$", "$", -1));
        }
    }

    private void listaMiembros() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en listaMiembros");
        if (tokenActual.getName().equals("keyword_static") || tokenActual.getName().equals("keyword_boolean") || tokenActual.getName().equals("keyword_char") || tokenActual.getName().equals("keyword_int") || tokenActual.getName().equals("keyword_float") || tokenActual.getName().equals("idClass") || tokenActual.getName().equals("keyword_void") || tokenActual.getName().equals("keyword_public")){
            miembro();
            listaMiembros();
        } else {
            //Epsilon
        }
    }

    private void listaEncabezados() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en listaEncabezados");
        if (tokenActual.getName().equals("keyword_static") || tokenActual.getName().equals("keyword_boolean") || tokenActual.getName().equals("keyword_char") || tokenActual.getName().equals("keyword_int") || tokenActual.getName().equals("keyword_float") || tokenActual.getName().equals("idClass") || tokenActual.getName().equals("keyword_void")){
            encabezadoMetodo();
            listaEncabezados();
        } else {
            //Epsilon
        }
    }

    private void miembro() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en miembro");
        currentMemberId.clear();
        currentMemberType = null;
        currentMemberStatic = null;
        if (tokenActual.getName().equals("keyword_static") || tokenActual.getName().equals("keyword_boolean") || tokenActual.getName().equals("keyword_char") || tokenActual.getName().equals("keyword_int") || tokenActual.getName().equals("keyword_float") || tokenActual.getName().equals("idClass") || tokenActual.getName().equals("keyword_void")){
            parte1Miembro();
            metodoOAtributo();
        } else if (tokenActual.getName().equals("keyword_public")){
            constructor();
        } else {
            print("Error en miembro");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "static o un tipo", tokenActual.getLexeme());
        }
    }

    private void metodoOAtributo() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en metodoOAtributo");
        if (tokenActual.getName().equals("punctuator_;") || tokenActual.getName().equals("assignment_=") || tokenActual.getName().equals("punctuator_,")){
            posiblesExtrasAtributos();
            asignacionOpcionalDeExpresion();
            match("punctuator_;");
            for (Token id : currentMemberId){
                symbolTable.currentClass.addAttribute(new ConcreteAttribute(id, currentMemberType, currentMemberStatic));
            }
            currentMemberId.clear();
        } else if (tokenActual.getName().equals("punctuator_(")){
            ConcreteMethod method = new ConcreteMethod(currentMemberId.get(0), currentMemberType, currentMemberStatic, symbolTable);
            symbolTable.currentClass.currentMethod = method;
            argsFormales();
            symbolTable.currentClass.addMethod(method);
            bloque();
        } else {
            print("Error en metodoOAtributo");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "( o ;", tokenActual.getLexeme());
        }
    }

    private void posiblesExtrasAtributos() throws LexicalException, SyntaxException, IOException {
        print("Entre en posiblesExtrasAtributos");
        if (tokenActual.getName().equals("punctuator_,")){
            match("punctuator_,");
            Token idMetVar = tokenActual;
            match("idMetVar");
            currentMemberId.add(idMetVar);
            posiblesExtrasAtributos();
        } else {
            //Epsilon
        }
    }

    private void encabezadoMetodo() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en encabezadoMetodo");
        currentMemberId.clear();
        parte1Miembro();
        symbolTable.currentClass.currentMethod = new ConcreteMethod(currentMemberId.get(0), currentMemberType, currentMemberStatic, symbolTable);
        argsFormales();
        match("punctuator_;");
        symbolTable.currentClass.addMethod(symbolTable.currentClass.currentMethod);
    }

    private void parte1Miembro() throws LexicalException, SyntaxException, IOException {
        print("Entre en parte1Miembro");
        currentMemberStatic = estaticoOpcional();

        if (tokenActual.getName().equals("operator_<")){
            metodoGenerico();
        } else if (tokenActual.getName().equals("keyword_boolean") || tokenActual.getName().equals("keyword_char") || tokenActual.getName().equals("keyword_int") || tokenActual.getName().equals("keyword_float") || tokenActual.getName().equals("idClass") || tokenActual.getName().equals("keyword_void") || tokenActual.getName().equals("idMetVar")){
            Token type = tipoMiembro();
            Token idMetVar = tokenActual;
            match("idMetVar");
            currentMemberType = type;
            currentMemberId.add(idMetVar);
        } else {
            print("Error en parte1Miembro");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "< o un tipo", tokenActual.getLexeme());
        }
    }

    private void metodoGenerico() throws LexicalException, SyntaxException, IOException {
        print("Entre en metodoGenerico");
        match("operator_<");
        match("idClass");
        listaDeClasesOpcionales();
        match("operator_>");
        tipoMiembro();
        match("idMetVar");
    }

    private void constructor() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en constructor");
        match("keyword_public");
        Token idClass = tokenActual;
        match("idClass");
        symbolTable.currentClass.currentMethod = new ConcreteMethod(idClass, idClass, new Token("", "-", -1), symbolTable);
        genericoOpcional();
        argsFormales();
        bloque();
        symbolTable.currentClass.addConstructor(symbolTable.currentClass.currentMethod);
    }

    private Token tipoMiembro() throws SyntaxException, LexicalException, IOException {
        print("Entre en tipoMiembro");
        if (tokenActual.getName().equals("keyword_boolean") || tokenActual.getName().equals("keyword_char") || tokenActual.getName().equals("keyword_int") || tokenActual.getName().equals("keyword_float") || tokenActual.getName().equals("idClass")){
            return tipo();
        } else if (tokenActual.getName().equals("keyword_void")){
            Token toReturn = tokenActual;
            match("keyword_void");
            return toReturn;
        } else {
            print("Error en tipoMiembro");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "un tipo", tokenActual.getLexeme());
        }
    }

    private Token tipo() throws SyntaxException, LexicalException, IOException {
        Token toReturn = null;
        print("Entre en tipo");
        if (tokenActual.getName().equals("keyword_boolean") || tokenActual.getName().equals("keyword_char") || tokenActual.getName().equals("keyword_int") || tokenActual.getName().equals("keyword_float")){
            toReturn = tipoPrimitivo();
        } else if (tokenActual.getName().equals("idClass")){
            Token tokenToReturn = tokenActual;
            match("idClass");
            genericoConID();
            toReturn = tokenToReturn;
        } else {
            print("Error en tipo");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "un tipo", tokenActual.getLexeme());
        }
        return toReturn;
    }

    private Token tipoPrimitivo() throws SyntaxException, LexicalException, IOException {
        Token toReturn = null;
        print("Entre en tipoPrimitivo");
        if (tokenActual.getName().equals("keyword_boolean")){
            toReturn = tokenActual;
            match("keyword_boolean");
        } else if (tokenActual.getName().equals("keyword_char")){
            toReturn = tokenActual;
            match("keyword_char");
        } else if (tokenActual.getName().equals("keyword_int")){
            toReturn = tokenActual;
            match("keyword_int");
        } else if (tokenActual.getName().equals("keyword_float")){
            toReturn = tokenActual;
            match("keyword_float");
        } else {
            print("Error en tipoPrimitivo");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "boolean, char, int o float", tokenActual.getLexeme());
        }
        return toReturn;
    }

    private Token estaticoOpcional() throws LexicalException, SyntaxException, IOException {
        Token toReturn = new Token("", "-", -1);
        print("Entre en estaticoOpcional");
        if (tokenActual.getName().equals("keyword_static")){
            toReturn = tokenActual;
            match("keyword_static");
        } else {
            //Epsilon
        }
        return toReturn;
    }

    private void argsFormales() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en argsFormales");
        match("punctuator_(");
        listaArgsFormalesOpcional();
        match("punctuator_)");
    }

    private void listaArgsFormalesOpcional() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en listaArgsFormalesOpcional");
        if (tokenActual.getName().equals("keyword_boolean") || tokenActual.getName().equals("keyword_char") || tokenActual.getName().equals("keyword_int") || tokenActual.getName().equals("keyword_float") || tokenActual.getName().equals("idClass")){
            argFormal();
            listaArgsFormales();
        } else {
            //Epsilon
        }
    }

    private void listaArgsFormales() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en listaArgsFormales");
        if (tokenActual.getName().equals("punctuator_,")){
            match("punctuator_,");
            argFormal();
            listaArgsFormales();
        } else {
            //Epsilon
        }
    }

    private void argFormal() throws LexicalException, SyntaxException, IOException, SemanticException {
        print("Entre en argFormal");
        Token type = tipo();
        Token idMetVar = tokenActual;
        match("idMetVar");
        symbolTable.currentClass.currentMethod.addParameter(new ConcreteAttribute(idMetVar, type, new Token("", "-", -1)));
    }

    private Node bloque() throws LexicalException, SyntaxException, IOException {
        print("Entre en bloque");
        match("punctuator_{");

        NodeBlock newSubBlock = new NodeBlock(symbolTable.currentClass, symbolTable.currentClass.currentMethod);
        //first time we create a block in a method we update the current block and the parent block of the new block will be null
        //this is because the block is the first block in the method
        //this will not cause any problems related of null pointer because the parent block is only used to check the visibility of the variables
        //if the parent block was null, then the variables will be the parameters and the attributes of the class
        //updated when call the check method of the block
        symbolTable.currentClass.currentMethod.currentBlock = newSubBlock;
        listaSentencias();
        match("punctuator_}");
        //When we finish the block, we go back to the parent block
        symbolTable.currentClass.currentMethod.currentBlock = newSubBlock.parentBlock;
        return newSubBlock;
    }

    private void listaSentencias() throws LexicalException, SyntaxException, IOException {
        print("Entre en listaSentencias");
        if (tokenActual.getName().equals("punctuator_;") || tokenActual.getName().equals("operator_+") || tokenActual.getName().equals("operator_-") || tokenActual.getName().equals("operator_!") || tokenActual.getName().equals("keyword_null") || tokenActual.getName().equals("keyword_true") || tokenActual.getName().equals("keyword_false") || tokenActual.getName().equals("intLiteral") || tokenActual.getName().equals("charLiteral") || tokenActual.getName().equals("strLiteral") || tokenActual.getName().equals("floatLiteral") || tokenActual.getName().equals("keyword_this") || tokenActual.getName().equals("idMetVar") || tokenActual.getName().equals("keyword_new") || tokenActual.getName().equals("idClass") || tokenActual.getName().equals("punctuator_(") || tokenActual.getName().equals("keyword_var") || tokenActual.getName().equals("keyword_return") || tokenActual.getName().equals("keyword_if") || tokenActual.getName().equals("keyword_while") || tokenActual.getName().equals("punctuator_{")){
            symbolTable.currentClass.currentMethod.currentBlock.addSentence(sentencia());
            listaSentencias();
        } else {
            //Epsilon
        }
    }

    private Node sentencia() throws LexicalException, SyntaxException, IOException {
        print("Entre en sentencia");
        Node sentence = null;
        if (tokenActual.getName().equals("punctuator_;")){
            match("punctuator_;");
            sentence = new EmptyNode();
        } else if (tokenActual.getName().equals("operator_+") || tokenActual.getName().equals("operator_-") || tokenActual.getName().equals("operator_!") || tokenActual.getName().equals("keyword_null") || tokenActual.getName().equals("keyword_true") || tokenActual.getName().equals("keyword_false") || tokenActual.getName().equals("intLiteral") || tokenActual.getName().equals("charLiteral") || tokenActual.getName().equals("strLiteral") || tokenActual.getName().equals("floatLiteral") || tokenActual.getName().equals("keyword_this") || tokenActual.getName().equals("idMetVar") || tokenActual.getName().equals("keyword_new") || tokenActual.getName().equals("idClass") || tokenActual.getName().equals("punctuator_(")){
            sentence = asignacionOLlamada();
            match("punctuator_;");
        } else if (tokenActual.getName().equals("keyword_var")){
            sentence = varLocal();
            match("punctuator_;");
        } else if (tokenActual.getName().equals("keyword_return")){
            sentence = retorno();
            match("punctuator_;");
        } else if (tokenActual.getName().equals("keyword_if")){
            sentence = sentenciaIf();
        } else if (tokenActual.getName().equals("keyword_while")){
            sentenciaWhile();
        } else if (tokenActual.getName().equals("punctuator_{")){
            sentence = bloque();
        } else {
            print("Error en sentencia");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "una sentencia", tokenActual.getLexeme());
        }

        return sentence;
    }

    private Node asignacionOLlamada() throws LexicalException, SyntaxException, IOException{
        print("Entre en asignacionOLlamada");
        return expresion();
    }

    private Node varLocal() throws LexicalException, SyntaxException, IOException {
        print("Entre en varLocal");
        match("keyword_var");
        Token name = tokenActual;
        match("idMetVar");
        match("assignment_=");
        Node expression = expresionCompuesta();
        NodeVarDeclaration varDeclaration = new NodeVarDeclaration(name, expression);
        return varDeclaration;
    }

    private Node retorno() throws LexicalException, SyntaxException, IOException {
        print("Entre en retorno");
        match("keyword_return");
        Node toReturn = new NodeReturn(expresionOpcional());
        return toReturn;
    }

    private Node expresionOpcional() throws LexicalException, SyntaxException, IOException {
        print("Entre en expresionOpcional");
        if (tokenActual.getName().equals("operator_+") || tokenActual.getName().equals("operator_-") || tokenActual.getName().equals("operator_!") || tokenActual.getName().equals("keyword_null") || tokenActual.getName().equals("keyword_true") || tokenActual.getName().equals("keyword_false") || tokenActual.getName().equals("intLiteral") || tokenActual.getName().equals("charLiteral") || tokenActual.getName().equals("strLiteral") || tokenActual.getName().equals("floatLiteral") || tokenActual.getName().equals("keyword_this") || tokenActual.getName().equals("idMetVar") || tokenActual.getName().equals("keyword_new") || tokenActual.getName().equals("idClass") || tokenActual.getName().equals("punctuator_(")){
            return expresion();
        } else {
            //Epsilon
            return null;
        }
    }

    private Node sentenciaIf() throws LexicalException, SyntaxException, IOException {
        print("Entre en sentenciaIf");
        match("keyword_if");
        match("punctuator_(");
        Node condition = expresion();
        match("punctuator_)");
        Node bodyThen = sentencia();
        Node bodyElse = elseOpcional();

        return new NodeIf(condition, bodyThen, bodyElse);
    }

    private Node elseOpcional() throws LexicalException, SyntaxException, IOException {
        print("Entre en elseOpcional");
        if (tokenActual.getName().equals("keyword_else")){
            match("keyword_else");
            return sentencia();
        } else {
            //Epsilon
            return null;
        }
    }

    private void sentenciaWhile() throws LexicalException, SyntaxException, IOException {
        print("Entre en sentenciaWhile");
        match("keyword_while");
        match("punctuator_(");
        expresion();
        match("punctuator_)");
        sentencia();
    }

    private Node expresion() throws LexicalException, SyntaxException, IOException {
        print("Entre en expresion");
        Node composedExpression = expresionCompuesta();
        Node toReturn = asignacionOpcionalDeExpresion(composedExpression);
        return null; //TODO: return something
    }

    private void asignacionOpcionalDeExpresion() throws LexicalException, SyntaxException, IOException {
        print("Entre en asignacionOpcionalDeExpresion");
        if (tokenActual.getName().equals("assignment_=")){
            match("assignment_=");
            expresion();
        } else {
            //Epsilon
        }
    }

    private Node expresionCompuesta() throws LexicalException, SyntaxException, IOException {
        print("Entre en expresionCompuesta");
        Node basicExpression = expresionBasica();
        return recursionExpresionCompuesta(basicExpression);
    }

    private void recursionExpresionCompuesta() throws LexicalException, SyntaxException, IOException {
        print("Entre en recursionExpresionCompuesta");
        if (tokenActual.getName().equals("operator_||") || tokenActual.getName().equals("operator_&&") || tokenActual.getName().equals("operator_==") || tokenActual.getName().equals("operator_!=") || tokenActual.getName().equals("operator_<") || tokenActual.getName().equals("operator_>") || tokenActual.getName().equals("operator_<=") || tokenActual.getName().equals("operator_>=") || tokenActual.getName().equals("operator_+") || tokenActual.getName().equals("operator_-") || tokenActual.getName().equals("operator_*") || tokenActual.getName().equals("operator_/") || tokenActual.getName().equals("operator_%")){
            operadorBinario();
            expresionBasica();
            recursionExpresionCompuesta();
        } else {
            //Epsilon
        }
    }

    private void operadorBinario() throws LexicalException, SyntaxException, IOException {
        print("Entre en operadorBinario");
        if (tokenActual.getName().equals("operator_||")){
            match("operator_||");
        } else if (tokenActual.getName().equals("operator_&&")){
            match("operator_&&");
        } else if (tokenActual.getName().equals("operator_==")){
            match("operator_==");
        } else if (tokenActual.getName().equals("operator_!=")){
            match("operator_!=");
        } else if (tokenActual.getName().equals("operator_<")){
            match("operator_<");
        } else if (tokenActual.getName().equals("operator_>")){
            match("operator_>");
        } else if (tokenActual.getName().equals("operator_<=")){
            match("operator_<=");
        } else if (tokenActual.getName().equals("operator_>=")){
            match("operator_>=");
        } else if (tokenActual.getName().equals("operator_+")){
            match("operator_+");
        } else if (tokenActual.getName().equals("operator_-")){
            match("operator_-");
        } else if (tokenActual.getName().equals("operator_*")){
            match("operator_*");
        } else if (tokenActual.getName().equals("operator_/")){
            match("operator_/");
        } else if (tokenActual.getName().equals("operator_%")){
            match("operator_%");
        } else {
            print("Error en operadorBinario");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "operador binario", tokenActual.getLexeme());
        }
    }

    private Node expresionBasica() throws LexicalException, SyntaxException, IOException {
        print("Entre en expresionBasica");
        Node toReturn = null;
        if (tokenActual.getName().equals("operator_+") || tokenActual.getName().equals("operator_-") || tokenActual.getName().equals("operator_!")){
            Token operator = operadorUnario();
            Node expression = operando();
            toReturn = new NodeUnaryOp(operator,expression, symbolTable.currentClass.currentMethod.currentBlock);
        } else if (tokenActual.getName().equals("keyword_null") || tokenActual.getName().equals("keyword_true") || tokenActual.getName().equals("keyword_false") || tokenActual.getName().equals("intLiteral") || tokenActual.getName().equals("charLiteral") || tokenActual.getName().equals("strLiteral") || tokenActual.getName().equals("floatLiteral") || tokenActual.getName().equals("keyword_this") || tokenActual.getName().equals("idMetVar") || tokenActual.getName().equals("keyword_new") || tokenActual.getName().equals("idClass") || tokenActual.getName().equals("punctuator_(")){
            toReturn = operando();
        } else {
            print("Error en expresionBasica");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "operador unario u operando", tokenActual.getLexeme());
        }
        return toReturn;
    }

    private Token operadorUnario() throws LexicalException, SyntaxException, IOException {
        print("Entre en operadorUnario");
        Token toReturn = tokenActual;
        if (tokenActual.getName().equals("operator_+")){
            match("operator_+");
        } else if (tokenActual.getName().equals("operator_-")){
            match("operator_-");
        } else if (tokenActual.getName().equals("operator_!")){
            match("operator_!");
        } else {
            print("Error en operadorUnario");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "operador unario", tokenActual.getLexeme());
        }
        return toReturn;
    }

    private Node operando() throws SyntaxException, LexicalException, IOException {
        print("Entre en operando");
        if (tokenActual.getName().equals("keyword_null") || tokenActual.getName().equals("keyword_true") || tokenActual.getName().equals("keyword_false") || tokenActual.getName().equals("intLiteral") || tokenActual.getName().equals("charLiteral") || tokenActual.getName().equals("strLiteral") || tokenActual.getName().equals("floatLiteral")){
            return literal();
        } else if (tokenActual.getName().equals("keyword_this") || tokenActual.getName().equals("idMetVar") || tokenActual.getName().equals("keyword_new") || tokenActual.getName().equals("idClass") || tokenActual.getName().equals("punctuator_(")){
            return acceso(); //TODO: return something
        } else {
            print("Error en operando");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "literal o acceso", tokenActual.getLexeme());
        }
    }

    //TODO: IMPORTANT - All the nodes must be created with the current block of the method

    private Node literal() throws SyntaxException, LexicalException, IOException {
        print("Entre en literal");
        Node toReturn = new NodeLiteral(tokenActual, symbolTable.currentClass.currentMethod.currentBlock);
        if (tokenActual.getName().equals("keyword_null")){
            match("keyword_null");
        } else if (tokenActual.getName().equals("keyword_true")){
            match("keyword_true");
        } else if (tokenActual.getName().equals("keyword_false")){
            match("keyword_false");
        } else if (tokenActual.getName().equals("intLiteral")){
            match("intLiteral");
        } else if (tokenActual.getName().equals("charLiteral")){
            match("charLiteral");
        } else if (tokenActual.getName().equals("strLiteral")){
            match("strLiteral");
        } else if (tokenActual.getName().equals("floatLiteral")){
            match("floatLiteral");
        } else {
            print("Error en literal");
            throw new SyntaxException(lexicalAnalyzer.getLine(), "literal", tokenActual.getLexeme());
        }
        return toReturn;
    }

    private Node acceso() throws SyntaxException, LexicalException, IOException {
        print("Entre en acceso");
        Node toReturn = null;

        toReturn = primario();
        encadenadoOpcional(toReturn);

        return toReturn;
    }

    private void primario() throws SyntaxException, LexicalException, IOException {
        print("Entre en primario");
        if (tokenActual.getName().equals("keyword_this")){
            accesoThis();
        } else if (tokenActual.getName().equals("idMetVar")){
            accesoMetVar();
        } else if (tokenActual.getName().equals("keyword_new")){
            accesoConstructor();
        } else if (tokenActual.getName().equals("idClass")){
            accesoMetodoEstatico();
        } else if (tokenActual.getName().equals("punctuator_(")){
            expresionParentizada();
        } else {
            print("Error en primario");
            throw new SyntaxException(lexicalAnalyzer.getLine(), tokenActual.getName(), "this, identificador, new o (");
        }
    }

    private void accesoThis() throws LexicalException, SyntaxException, IOException {
        print("Entre en accesoThis");
        match("keyword_this");
    }

    private void accesoMetVar() throws LexicalException, SyntaxException, IOException {
        print("Entre en accesoMetVar");
        match("idMetVar");
        continuacionMetodo();
    }

    private void continuacionMetodo() throws LexicalException, SyntaxException, IOException {
        print("Entre en continuacionMetodo");
        if (tokenActual.getName().equals("punctuator_(")){
            argsActuales();
        } else {
            //Epsilon
        }
    }

    private void accesoConstructor() throws LexicalException, SyntaxException, IOException {
        print("Entre en accesoConstructor");
        match("keyword_new");
        match("idClass");
        genericoOpcional();
        argsActuales();
    }

    private void expresionParentizada() throws LexicalException, SyntaxException, IOException {
        print("Entre en expresionParentizada");
        match("punctuator_(");
        expresion();
        match("punctuator_)");
    }

    private void accesoMetodoEstatico() throws LexicalException, SyntaxException, IOException {
        print("Entre en accesoMetodoEstatico");
        match("idClass");
        genericoOpcional();
        match("punctuator_.");
        match("idMetVar");
        argsActuales();
    }

    private void argsActuales() throws LexicalException, SyntaxException, IOException {
        print("Entre en argsActuales");
        match("punctuator_(");
        listaExpsOpcional();
        match("punctuator_)");
    }

    private void listaExpsOpcional() throws LexicalException, SyntaxException, IOException {
        print("Entre en listaExpsOpcional");
        if (tokenActual.getName().equals("operator_+") || tokenActual.getName().equals("operator_-") || tokenActual.getName().equals("operator_!") || tokenActual.getName().equals("keyword_null") || tokenActual.getName().equals("keyword_true") || tokenActual.getName().equals("keyword_false") || tokenActual.getName().equals("intLiteral") || tokenActual.getName().equals("charLiteral") || tokenActual.getName().equals("strLiteral") || tokenActual.getName().equals("floatLiteral") || tokenActual.getName().equals("keyword_this") || tokenActual.getName().equals("idMetVar") || tokenActual.getName().equals("keyword_new") || tokenActual.getName().equals("idClass") || tokenActual.getName().equals("punctuator_(")){
            expresion();
            listaExps();
        } else {
            //Epsilon
        }
    }

    private void listaExps() throws LexicalException, SyntaxException, IOException {
        print("Entre en listaExps");
        if (tokenActual.getName().equals("punctuator_,")){
            match("punctuator_,");
            expresion();
            listaExps();
        } else {
            //Epsilon
        }
    }

    private void encadenadoOpcional(Node primary) throws LexicalException, SyntaxException, IOException {
        print("Entre en encadenadoOpcional");
        Node access = new NodeAccess(primary, symbolTable.currentClass.currentMethod.currentBlock);
        if (tokenActual.getName().equals("punctuator_.")){
            puntoYMetVar();
            argsOpcionales();
            encadenadoOpcional(access);
        } else {
            //Epsilon
        }
    }

    private void puntoYMetVar() throws LexicalException, SyntaxException, IOException {
        print("Entre en puntoYMetVar");
        match("punctuator_.");
        Token idMetVar = tokenActual;
        match("idMetVar");
    }

    private void argsOpcionales() throws LexicalException, SyntaxException, IOException {
        print("Entre en argsOpcionales");
        if (tokenActual.getName().equals("punctuator_(")){
            argsActuales();
        } else {
            //Epsilon
        }
    }

    private void genericoOpcional() throws LexicalException, SyntaxException, IOException {
        print("Entre en genericoOpcional");
        if (tokenActual.getName().equals("operator_<")){
            match("operator_<");
            claseOpcional();
            match("operator_>");
        } else {
            //Epsilon
        }
    }

    private void claseOpcional() throws LexicalException, SyntaxException, IOException {
        print("Entre en claseOpcional");
        if (tokenActual.getName().equals("idClass")){
            match("idClass");
            listaDeClasesOpcionales();
        } else {
            //Epsilon
        }
    }

    private void listaDeClasesOpcionales() throws LexicalException, SyntaxException, IOException {
        print("Entre en listaDeClasesOpcionales");
        if (tokenActual.getName().equals("punctuator_,")){
            match("punctuator_,");
            match("idClass");
            listaDeClasesOpcionales();
        } else {
            //Epsilon
        }
    }

    private void genericoConID() throws LexicalException, SyntaxException, IOException {
        print("Entre en genericoConID");
        if (tokenActual.getName().equals("operator_<")){
            match("operator_<");
            match("idClass");
            listaDeClasesOpcionales();
            match("operator_>");
        } else {
            //Epsilon
        }
    }

    private void print(String s){
        if (verbose){
            System.out.println(s);
        }
    }

    public void enableVerbose() {
        this.verbose = true;
    }

    public void disableVerbose() {
        this.verbose = false;
    }

    public String getST() {
        return symbolTable.toString();
    }

    public Collection<? extends Exception> getErrors() {
        return symbolTable.getErrors();
    }
}