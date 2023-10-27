package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.*;

import java.util.ArrayList;

public class NodeVariable implements Node{

    public Token name;
    public NodeBlock parentBlock;
    private boolean alreadyChecked = false;
    public Token type;
    public NodeVariable childChain, parentChain;
    public boolean isMethod = false;
    public ArrayList<Node> parameters = new ArrayList<>();

    public NodeVariable(Token name, NodeBlock parentBlock) {
        this.name = name;
        this.parentBlock = parentBlock;
    }

    public NodeVariable(){
        return;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (!alreadyChecked){
            //check parameters
            for (Node parameter : parameters){
                parameter.check(symbolTable);
            }
            if (isMethod){
                if (parentChain == null){
                    ConcreteClass currentClass = parentBlock.currentClass;
                    ConcreteMethod methodToMatch = currentClass.methods.get(name.getLexeme());
                    if (parentBlock.currentMethod.isStatic.getLexeme().equals("static") && !methodToMatch.isStatic.getLexeme().equals("static")){
                        if (!(parentChain instanceof NodeVariableConstructor))
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Static method " + name.getLexeme() + " cannot be called from a non static method"));
                    }
                    if (methodToMatch == null){
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + currentClass.name.getLexeme()));
                    } else {
                        if (methodToMatch.parameters.size() != parameters.size()){
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + currentClass.name.getLexeme() + " with the given parameters"));
                        } else {
                            for (int i = 0; i < parameters.size(); i++){
                                if (!parameters.get(i).getType().getLexeme().equals(methodToMatch.parametersInOrder.get(i).getType().getLexeme())){
                                    symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + currentClass.name.getLexeme() + " with the given parameters"));
                                }
                            }
                            type = methodToMatch.type;
                        }
                    }
                } else {

                    //im the chain of something
                    if (parentChain.getType().getName().equals("idClass")) {
                        if (symbolTable.classes.containsKey(parentChain.getType().getLexeme())){
                            ConcreteMethod methodToMatch = symbolTable.classes.get(parentChain.getType().getLexeme()).methods.get(name.getLexeme());
                            if (parentBlock.currentMethod.isStatic.getLexeme().equals("static") && !methodToMatch.isStatic.getLexeme().equals("static")){
                                if (!(parentChain instanceof NodeVariableConstructor))
                                    symbolTable.semExceptionHandler.show(new SemanticException(name,"Static method " + name.getLexeme() + " cannot be called from a non static method"));
                            }
                            if (methodToMatch == null){
                                symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme()));
                            } else {
                                if (methodToMatch.parameters.size() != parameters.size()){
                                    symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme() + " with the given parameters"));
                                } else {
                                    for (int i = 0; i < parameters.size(); i++){
                                        if (!parameters.get(i).getType().getLexeme().equals(methodToMatch.parametersInOrder.get(i).getType().getLexeme())){
                                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme() + " with the given parameters"));
                                        }
                                    }
                                    type = methodToMatch.type;
                                }
                            }
                        } else {
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme()));
                        }
                    } else {
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Method " + name.getLexeme() + " is not defined"));
                    }


                }
            } else {
                //then is access to an attribute
                if (parentChain == null){
                    ConcreteAttribute toCheck = parentBlock.getVisible(name.getLexeme());
                    if (toCheck == null){
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined"));
                    } else {
                        if (parentBlock.currentMethod.isStatic.getLexeme().equals("static") && !toCheck.isStatic.getLexeme().equals("static") && parentBlock.getVisible(name.getLexeme()) == null){
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Non static attribute " + name.getLexeme() + " cannot be called from a static method"));
                        }
                        if (parentBlock.currentMethod.isStatic.getLexeme().equals("static") && parentBlock.getVisible(name.getLexeme()).isStatic.getLexeme().equals("-") && !parentBlock.currentMethod.parameters.containsKey(name.getLexeme()))
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"ASDASDAS Non static attribute " + name.getLexeme() + " cannot be called from a static method"));

                        type = toCheck.getType();
                    }
                } else {
                    //im the chain of something
                    if (parentChain.getType().getName().equals("idClass")) {
                        if (symbolTable.classes.containsKey(parentChain.getType().getLexeme())){
                            ConcreteAttribute attributeToMatch = symbolTable.classes.get(parentChain.getType().getLexeme()).attributes.get(name.getLexeme());
                            if (attributeToMatch == null){
                                symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme()));
                            } else {
                                if (parentBlock.currentMethod.isStatic.getLexeme().equals("static") && !attributeToMatch.isStatic.getLexeme().equals("static")){
                                    if (!(parentChain instanceof NodeVariableConstructor))
                                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Non static attribute " + name.getLexeme() + " cannot be called from a static method"));
                                }
                                type = attributeToMatch.getType();
                            }
                        } else {
                            symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined in class " + parentChain.getType().getLexeme()));
                        }
                    } else {
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined"));
                    }
                }
            }

            //then check the child chain
            if (childChain != null){
                childChain.check(symbolTable);
                type = childChain.type;
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
        parentBlock = nodeBlock;
    }

    @Override
    public Token getToken() {
        return name;
    }

    public void setChildChain(NodeVariable nodeVariable){
        childChain = nodeVariable;
        childChain.setParentChain(this);
    }

    public void setParentChain(NodeVariable nodeVariable){
        parentChain = nodeVariable;
    }

    public boolean inTheLastIsMethod(){
        if (childChain == null)
            return isMethod;
        else
            return childChain.inTheLastIsMethod();
    }
}
