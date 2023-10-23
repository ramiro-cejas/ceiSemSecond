package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeUnaryOp implements Node{

    public Token unaryOp;
    public Node expression;
    private NodeBlock parentBlock;
    private boolean alreadyChecked = false;

    public NodeUnaryOp(Token unaryOp, Node expression, NodeBlock parentBlock) {
        this.unaryOp = unaryOp;
        this.expression = expression;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) {
        if (!alreadyChecked){
            expression.check(symbolTable);
            if (unaryOp.getLexeme().equals("!")) {
                if (!expression.getType().getLexeme().equals("boolean")) {
                    symbolTable.semExceptionHandler.show(new SemanticException(unaryOp, "Unary operator ! must be applied to boolean expressions"));
                }
            } else if (unaryOp.getLexeme().equals("-")) {
                if (!expression.getType().getLexeme().equals("int") && !expression.getType().getLexeme().equals("float")) {
                    symbolTable.semExceptionHandler.show(new SemanticException(unaryOp, "Unary operator - must be applied to int or float expressions"));
                }
            } else {
                // This should never happen, but just in case
                symbolTable.semExceptionHandler.show(new SemanticException(unaryOp, "Unary operator " + unaryOp.getLexeme() + " is not valid"));
            }
        }
        alreadyChecked = true;
    }

    @Override
    public Token getType() {
        return expression.getType();
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }
}
