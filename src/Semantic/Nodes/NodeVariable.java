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
        System.out.println("Checking variable " + name.getLexeme());
        if (!alreadyChecked){
            //check parameters
            for (Node parameter : parameters){
                parameter.check(symbolTable);
            }
            if (isMethod){
                if (parentChain == null){
                    ConcreteClass currentClass = parentBlock.currentClass;
                    ConcreteMethod methodToMatch = currentClass.methods.get(name.getLexeme());
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
                    System.out.println("Entered with parent chain: " + parentChain);
                    System.out.println("Entered with parent chain type: " + parentChain.getType().getName());
                    if (parentChain.getType().getName().equals("idClass")) {
                        System.out.println("Entered with parent chain: " + parentChain.getType().getLexeme());
                        if (symbolTable.classes.containsKey(parentChain.getType().getLexeme())){
                            ConcreteMethod methodToMatch = symbolTable.classes.get(parentChain.getType().getLexeme()).methods.get(name.getLexeme());
                            System.out.println("Method to match: " + methodToMatch.name.getLexeme());
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
                    System.out.println("Entered with no parent chain");
                    ConcreteAttribute toCheck = parentBlock.getVisible(name.getLexeme());
                    System.out.println("To check: " + toCheck.getType().getLexeme());
                    if (toCheck == null){
                        symbolTable.semExceptionHandler.show(new SemanticException(name,"Attribute " + name.getLexeme() + " is not defined"));
                    } else {
                        System.out.println("Type setted: " + toCheck.getType().getLexeme());
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

    public void setChildChain(NodeVariable nodeVariable){
        childChain = nodeVariable;
        childChain.setParentChain(this);
    }

    public void setParentChain(NodeVariable nodeVariable){
        parentChain = nodeVariable;
    }
}
