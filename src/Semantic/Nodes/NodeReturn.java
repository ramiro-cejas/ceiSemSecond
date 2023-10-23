package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeReturn implements Node{

    public Node expression;
    private Token type;
    private boolean alreadyChecked = false;
    private NodeBlock parentBlock;

    public NodeReturn(Node expression) {
        this.expression = expression;
    }

    @Override
    public void check(SymbolTable symbolTable) {
        if (alreadyChecked){
            return;
        }
        if (expression != null){
            expression.check(symbolTable);
            type = expression.getType();
        }
        else{
            type = new Token("void", "void", 0);
        }
        if (!type.getLexeme().equals(parentBlock.currentMethod.type.getLexeme())){
            symbolTable.semExceptionHandler.show(new SemanticException(parentBlock.currentMethod.type,"No return found with type "+parentBlock.currentMethod.type.getLexeme()+" in line "+parentBlock.currentMethod.type.getRow()));
        }
        alreadyChecked = true;
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
