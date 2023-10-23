package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SymbolTable;

public class NodeExpression implements Node{

    public Node expression;
    private NodeBlock parentBlock;
    @Override
    public void check(SymbolTable symbolTable) {

    }

    @Override
    public Token getType() {
        return null;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {

    }
}
