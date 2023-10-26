package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.*;

import java.util.ArrayList;

public class NodeBlock implements Node{

    private ArrayList<Node> sentences = new ArrayList<>();
    ArrayList<ConcreteAttribute> classAttributes = new ArrayList<>();
    ArrayList<ConcreteAttribute> methodParameters = new ArrayList<>();
    ArrayList<ConcreteAttribute> localVariables = new ArrayList<>();

    public NodeBlock parentBlock;
    public ConcreteClass currentClass;
    public ConcreteMethod currentMethod;

    public NodeBlock(ConcreteClass currentClass, ConcreteMethod currentMethod) {
        this.currentClass = currentClass;
        this.currentMethod = currentMethod;
        parentBlock = null;
        classAttributes.addAll(currentMethod.parameters.values());
        methodParameters.addAll(currentClass.attributes.values());
    }

    public void addSentence(Node sentence){
        sentence.setParentBlock(this);
        sentences.add(sentence);
    }

    public void check(SymbolTable symbolTable) throws SemanticException {
        for (Node sentence : sentences){
            sentence.check(symbolTable);
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
}
