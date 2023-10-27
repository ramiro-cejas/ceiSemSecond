package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeVariableConstructor extends NodeVariable{

    private boolean alreadyChecked = false;

    public NodeVariableConstructor(Token name, NodeBlock parentBlock, Token type) {
        super(name, parentBlock);
        this.type = type;
    }

    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked)
            return;
        System.out.println("Checking variable constructor");
        for (Node parameter : parameters){
            parameter.check(symbolTable);
        }
        if (symbolTable.classes.containsKey(type.getLexeme())){
            System.out.println("Type is a class");
            if (symbolTable.classes.get(type.getLexeme()).constructor == null){
                System.out.println("Class has no constructor");
                symbolTable.semExceptionHandler.show(new SemanticException(type,"Class " + type.getLexeme() + " has no constructor"));
            } else {
                System.out.println("Class has constructor");
                if (symbolTable.classes.get(type.getLexeme()).constructor.parameters.size() != parameters.size()){
                    System.out.println("Constructor has different number of parameters");
                    System.out.println("Constructor has " + symbolTable.classes.get(type.getLexeme()).constructor.parameters.size() + " parameters");
                    System.out.println("Variable constructor has " + parameters.size() + " parameters");
                    symbolTable.semExceptionHandler.show(new SemanticException(type,"Constructor of class " + type.getLexeme() + " has different number of parameters"));
                } else {
                    System.out.println("Constructor has same number of parameters");
                    for (int i = 0; i < parameters.size(); i++){
                        System.out.println("Checking parameter " + i);
                        //check if the parameter is a subtype of the parameter in the constructor
                        //if the parameter is a class, check if it's a subtype of the parameter in the constructor
                        if (parameters.get(i).getType().getName().equals("idClass")){
                            System.out.println("Parameter is a class");
                            if (!symbolTable.classes.get(parameters.get(i).getType().getLexeme()).isSubTypeOf(symbolTable.classes.get(symbolTable.classes.get(type.getLexeme()).constructor.parametersInOrder.get(i).getType().getLexeme()))){
                                System.out.println("Parameter is not a subtype of the parameter in the constructor");
                                symbolTable.semExceptionHandler.show(new SemanticException(type,"Parameter " + i + " is not a subtype of the parameter in the constructor"));
                            }
                        } else {
                            System.out.println("Parameter is not a class");
                            if (!parameters.get(i).getType().getLexeme().equals(symbolTable.classes.get(type.getLexeme()).constructor.parametersInOrder.get(i).getType().getLexeme())){
                                System.out.println("Parameter is not the same type as the parameter in the constructor");
                                symbolTable.semExceptionHandler.show(new SemanticException(type,"Parameter " + i + " is not the same type as the parameter in the constructor"));
                            }
                        }
                    }
                }
            }
            if(childChain != null){
                System.out.println("Checking child chain: "+childChain+" of the parent chain: "+this);
                childChain.check(symbolTable);
                type = childChain.type;
            }
        } else {
            System.out.println("Type is not a class");
            symbolTable.semExceptionHandler.show(new SemanticException(type,"Type " + type.getLexeme() + " is not a defined class"));
        }
    }
}
