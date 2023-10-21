package SecondSemantic.Semantic;

import SecondSemantic.Lexical.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class ConcreteMethod {

    public Token name;
    public Token isStatic;
    public Token type;
    public HashMap<String, ConcreteAttribute> parameters;
    SymbolTable symbolTable;
    public ArrayList<ConcreteAttribute> parametersInOrder;
    private boolean alreadyChecked = false;

    public ConcreteMethod(Token name, Token type, Token isStatic, SymbolTable symbolTable) {
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
        this.symbolTable = symbolTable;
        parameters = new HashMap<>();
        parametersInOrder = new ArrayList<>();
    }

    public void addParameter(ConcreteAttribute p) {
        if (!parameters.containsKey(p.name.getLexeme())){
            if (p.type.getLexeme().equals("void")){
                symbolTable.semExceptionHandler.show(new SemanticException(p.name,"Parameter " + p.name.getLexeme() + " cannot be void in line "+ p.name.getRow()));
            }else {
                parametersInOrder.add(p);
                parameters.put(p.name.getLexeme(), p);
            }
        }
        else
            symbolTable.semExceptionHandler.show(new SemanticException(p.name,"Parameter " + p.name.getLexeme() + " already defined in line "+ p.name.getRow()));
    }

    public void check() {
        if (!alreadyChecked){
            for (ConcreteAttribute p : parameters.values()){
                if (p.type.getName().equals("idClass")) {
                    if (!symbolTable.classes.containsKey(p.type.getLexeme()) && !symbolTable.interfaces.containsKey(p.type.getLexeme()))
                        symbolTable.semExceptionHandler.show(new SemanticException(p.type,"Class or interface " + p.type.getLexeme() + " not defined in line "+ p.type.getRow()));
                } else if (p.type.getLexeme().equals("void")){
                    symbolTable.semExceptionHandler.show(new SemanticException(p.name,"Parameter " + p.name.getLexeme() + " cannot be void in line "+ p.name.getRow()));
                }
            }
            alreadyChecked = true;
        }
    }



}
