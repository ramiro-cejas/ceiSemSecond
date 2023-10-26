package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeVariableThis extends NodeExpression{

    public ConcreteClass thisClass;
    public NodeBlock parentBlock;
    private Token type;

    public NodeVariableThis(ConcreteClass thisClass, NodeBlock parentBlock) {
        this.thisClass = thisClass;
        this.parentBlock = parentBlock;
        type = new Token("idClass", thisClass.name.getLexeme(), -1);
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        // Nothing to check
    }

    @Override
    public Token getType() {
        return type;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }
}
