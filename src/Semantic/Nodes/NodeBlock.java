package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteAttribute;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SymbolTable;

import java.util.ArrayList;

public class NodeBlock implements Node{

    private ArrayList<Node> sentences = new ArrayList<>();
    ArrayList<ConcreteAttribute> visibleVariables = new ArrayList<>();
    public NodeBlock parentBlock;
    public ConcreteClass currentClass;
    public ConcreteMethod currentMethod;

    public NodeBlock(ConcreteClass currentClass, ConcreteMethod currentMethod) {
        this.currentClass = currentClass;
        this.currentMethod = currentMethod;
        parentBlock = null;
        visibleVariables.addAll(currentMethod.parameters.values());
        visibleVariables.addAll(currentClass.attributes.values());
    }

    public void addSentence(Node sentence){
        sentence.setParentBlock(this);
        sentences.add(sentence);
    }

    public void check(SymbolTable symbolTable) {
        for (Node sentence : sentences){
            sentence.check(symbolTable);
        }
    }

    @Override
    public Token getType() {
        return null;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }
}
