package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeAssignment implements Node{
    public Token sign;
    public Node leftExpression;
    public Node rightExpression;
    public NodeBlock parentBlock;
    public Token type;
    public boolean alreadyChecked = false;

    public NodeAssignment(Token sign, Node leftExpression, Node rightExpression, NodeBlock parentBlock) {
        this.sign = sign;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (!alreadyChecked){
            System.out.println("Checking assignment");
            leftExpression.check(symbolTable);
            rightExpression.check(symbolTable);
            //if le right side conform to the left side then it's ok
            System.out.println("Left expression: " + leftExpression);
            System.out.println("Right expression: " + rightExpression);
            if (!leftExpression.getType().getName().equals(rightExpression.getType().getName())){
                symbolTable.semExceptionHandler.show(new SemanticException(sign, "Cannot assign " + rightExpression.getType().getLexeme() + " to " + leftExpression.getType().getLexeme()));
            } else {
                //check if the left side type is a subtype of the type on the left side
                if (leftExpression.getType().getName().equals("idClass")){
                    if (!symbolTable.classes.get(leftExpression.getType().getLexeme()).isSubTypeOf(symbolTable.classes.get(rightExpression.getType().getLexeme()))){
                        symbolTable.semExceptionHandler.show(new SemanticException(sign, "Cannot assign " + rightExpression.getType().getLexeme() + " to " + leftExpression.getType().getLexeme()));
                    }
                }
            }

            type = leftExpression.getType();
        }
        alreadyChecked = true;
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
