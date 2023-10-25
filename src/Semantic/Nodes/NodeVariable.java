package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.*;

import java.util.ArrayList;

public class NodeVariable implements Node{

    public Token name;
    public NodeBlock parentBlock;
    private boolean alreadyChecked = false;
    private Token type;
    public NodeVariable childChain, parentChain;
    public boolean isMethod = false;
    public ArrayList<ConcreteAttribute> parameters = new ArrayList<>();

    public NodeVariable(Token name, NodeBlock parentBlock) {
        this.name = name;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) {
        if (!alreadyChecked){
            if (isMethod){
                if (parentChain == null){
                    symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined"));
                } else {
                    //im the chain of something
                    if (parentChain.type.getName().equals("idClass")) {
                        if (symbolTable.classes.containsKey(parentChain.type.getLexeme())){
                            ConcreteMethod methodToMatch = symbolTable.classes.get(parentChain.type.getLexeme()).methods.get(name.getLexeme());
                            if (methodToMatch == null){
                                symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.type.getLexeme()));
                            } else {
                                if (methodToMatch.parameters.size() != parameters.size()){
                                    symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.type.getLexeme() + " with the given parameters"));
                                } else {
                                    for (int i = 0; i < parameters.size(); i++){
                                        if (!parameters.get(i).getType().getLexeme().equals(methodToMatch.parametersInOrder.get(i).getType().getLexeme())){
                                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.type.getLexeme() + " with the given parameters"));
                                        }
                                    }
                                    type = methodToMatch.type;
                                }
                            }
                        } else {
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.type.getLexeme()));
                        }
                    } else {
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined"));
                    }
                }
            } else {
                //then is access to an attribute
                if (parentChain == null){
                    //should never happen
                    symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined"));
                } else {
                    //im the chain of something
                    if (parentChain.type.getName().equals("idClass")) {
                        if (symbolTable.classes.containsKey(parentChain.type.getLexeme())){
                            ConcreteAttribute attributeToMatch = symbolTable.classes.get(parentChain.type.getLexeme()).attributes.get(name.getLexeme());
                            if (attributeToMatch == null){
                                symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined in class " + parentChain.type.getLexeme()));
                            } else {
                                type = attributeToMatch.getType();
                            }
                        } else {
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined in class " + parentChain.type.getLexeme()));
                        }
                    } else {
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined"));
                    }
                }
            }

            //then check the child chain
            if (childChain != null){
                childChain.check(symbolTable);
            }
            alreadyChecked = true;
        }
    }

    @Override
    public Token getType() {
        return type;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {

    }
}