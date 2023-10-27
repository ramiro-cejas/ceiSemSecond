package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.*;

import java.util.ArrayList;

public class NodeBlock implements Node{

    public Token initialToken;
    private ArrayList<Node> sentences = new ArrayList<>();
    ArrayList<ConcreteAttribute> classAttributes = new ArrayList<>();
    ArrayList<ConcreteAttribute> methodParameters = new ArrayList<>();
    ArrayList<ConcreteAttribute> localVariables = new ArrayList<>();

    public NodeBlock parentBlock;
    public ConcreteClass currentClass;
    public ConcreteMethod currentMethod;

    public NodeBlock(Token initialToken, ConcreteClass currentClass, ConcreteMethod currentMethod, NodeBlock parentBlock) {
        this.initialToken = initialToken;
        this.currentClass = currentClass;
        this.currentMethod = currentMethod;
        this.parentBlock = parentBlock;
    }

    public void addSentence(Node sentence){
        sentence.setParentBlock(this);
        sentences.add(sentence);
    }

    public void check(SymbolTable symbolTable) throws SemanticException {
        if (parentBlock != null){
            classAttributes.addAll(parentBlock.classAttributes);
            methodParameters.addAll(parentBlock.methodParameters);
            localVariables.addAll(parentBlock.localVariables);
            System.out.println("_______________ SE AGREGARON CON REFERENCIA AL PADRE _______________");
            //show all the local variables
            System.out.println("Local variables: ");
            for (ConcreteAttribute attribute : localVariables){
                System.out.println(attribute.getName().getLexeme() + " : " + attribute.getType().getLexeme());
            }
        }else{
            classAttributes.addAll(currentMethod.parameters.values());
            methodParameters.addAll(currentClass.attributes.values());
            System.out.println("_______________ SE AGREGARON SIN REFERENCIA AL PADRE _______________");
        }
        for (Node sentence : sentences){
            sentence.check(symbolTable);
            if (sentence instanceof NodeExpression || sentence instanceof NodeBinaryOp || sentence instanceof NodeUnaryOp || sentence instanceof NodeLiteral){
                symbolTable.semExceptionHandler.show(new SemanticException(sentence.getToken(),"Not a valid sentence"));
            }
            if (sentence instanceof NodeVariable){
                if (!((NodeVariable) sentence).inTheLastIsMethod())
                    symbolTable.semExceptionHandler.show(new SemanticException(sentence.getToken(),"Not a valid sentence"));
            }
        }
    }

    public ConcreteAttribute getVisible(String toSearch){
        //search in the collections of attributes, parameters and local variables and return the last one found
        for (int i = localVariables.size() - 1; i >= 0; i--){
            if (localVariables.get(i).getName().getLexeme().equals(toSearch))
                return localVariables.get(i);
        }
        for (int i = methodParameters.size() - 1; i >= 0; i--){
            if (methodParameters.get(i).getName().getLexeme().equals(toSearch))
                return methodParameters.get(i);
        }
        for (int i = classAttributes.size() - 1; i >= 0; i--){
            if (classAttributes.get(i).getName().getLexeme().equals(toSearch))
                return classAttributes.get(i);
        }
        return null;
    }

    @Override
    public Token getType() {
        return new Token("keyword_null", "null", -1);
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }

    @Override
    public Token getToken() {
        return null;
    }
}
