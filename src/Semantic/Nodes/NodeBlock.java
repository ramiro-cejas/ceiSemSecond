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

    @Override
    public Token getType() {
        return new Token("keyword_null", "null", -1);
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }
}
