package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeIf implements Node{

    public Node condition;
    public Node ifSentence;
    public Node elseSentence;
    public NodeBlock parentBlock;

    public NodeIf(Node condition, Node ifSentence, Node elseSentence) {
        this.condition = condition;
        this.ifSentence = ifSentence;
        this.elseSentence = elseSentence;
    }

    @Override
    public void check(SymbolTable symbolTable) {
        condition.check(symbolTable);
        if (!condition.getType().getLexeme().equals("boolean")){
            symbolTable.semExceptionHandler.show(new SemanticException(condition.getType(),"Condition must be boolean in line "+ condition.getType().getRow()));
        }
        ifSentence.check(symbolTable);
        if (elseSentence != null){
            elseSentence.check(symbolTable);
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
