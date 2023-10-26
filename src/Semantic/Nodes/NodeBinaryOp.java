package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeBinaryOp implements Node{

    public Node leftExpression;
    public Node rightExpression;
    public Token operator;
    public NodeBlock parentBlock;
    private Token type;
    public boolean alreadyChecked = false;

    public NodeBinaryOp(Token operator, Node leftExpression, Node rightExpression, NodeBlock parentBlock) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.operator = operator;
        this.parentBlock = parentBlock;
    }
    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked){
            return;
        }
        System.out.println("Checking binary operation with operator: " + operator.getLexeme() + " and left expression: " + leftExpression + " and right expression: " + rightExpression);
        leftExpression.check(symbolTable);
        rightExpression.check(symbolTable);
        Token leftType = leftExpression.getType();
        Token rightType = rightExpression.getType();
        System.out.println("Left type: " + leftType.getLexeme());
        System.out.println("Right type: " + rightType.getLexeme());
        System.out.println("Operator: " + operator.getLexeme());
        //if the operator is + - * / % only int and float are allowed (check with the lexeme of the token)
        if (operator.getLexeme().equals("+") || operator.getLexeme().equals("-") || operator.getLexeme().equals("*") || operator.getLexeme().equals("/") || operator.getLexeme().equals("%")){
            // if the types are different then error
            if (!leftType.getLexeme().equals(rightType.getLexeme())){
                symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation between different types"));
            }
            if (!leftType.getLexeme().equals("int") && !leftType.getLexeme().equals("float")){
                symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation only allowed between int and float"));
            }
            type = leftType;
            System.out.println("Type: " + type.getLexeme() + " setted on the object " + this);
        } else
            //if the operator is && || only boolean is allowed (check with the lexeme of the token)
            if (operator.getLexeme().equals("&&") || operator.getLexeme().equals("||")){
                // if the types are different then error
                if (!leftType.getLexeme().equals(rightType.getLexeme())){
                    symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation between different types"));
                }
                if (!leftType.getLexeme().equals("boolean")){
                    symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation only allowed between boolean"));
                }
                type = leftType;
            }
            else
                //if the operator is == != only conform types are allowed (check with the lexeme of the token)
                //if the type of the right conform with the left one, then the type will be boolean, otherwise error
                if (operator.getLexeme().equals("!=") || operator.getLexeme().equals("==")){
                    if (leftType.getLexeme().equals("int") || leftType.getLexeme().equals("float") || leftType.getLexeme().equals("char") || leftType.getLexeme().equals("boolean")){
                        if (!leftType.getLexeme().equals(rightType.getLexeme())){
                            symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation between different types"));
                        }
                        type = new Token("keyword_boolean", "boolean", operator.getRow());
                    } else {
                        if (symbolTable.classes.get(rightType.getLexeme()).isSubTypeOf(symbolTable.classes.get(leftType.getLexeme()))){
                            type = new Token("keyword_boolean", "boolean", operator.getRow());
                        } else {
                            symbolTable.semExceptionHandler.show(new SemanticException(operator,"Binary operation between different types"));
                        }
                    }
                }
        alreadyChecked = true;
    }

    @Override
    public Token getType() {
        return type;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {

    }
}
