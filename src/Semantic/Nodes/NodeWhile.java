package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeWhile implements Node{

    public Node condition;
    public Node body;
    public NodeBlock parentBlock;
    private boolean alreadyChecked = false;

    public NodeWhile(Node condition, Node body, NodeBlock parentBlock) {
        this.condition = condition;
        this.body = body;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (!alreadyChecked){
            condition.check(symbolTable);
            if (!condition.getType().getName().equals("keyword_boolean")){
                symbolTable.semExceptionHandler.show(new SemanticException(condition.getType(),"Condition must be boolean in line "+condition.getType().getRow()));
            }
            body.check(symbolTable);
            alreadyChecked = true;
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
