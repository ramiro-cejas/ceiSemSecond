package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class ExpressionBinary implements Expression{
    private Token operator;
    private Expression left;
    private Expression right;

    public ExpressionBinary(){
        this.operator = null;
        this.left = null;
        this.right = null;
    }

    public Token getOperator(){
        return this.operator;
    }

    public Expression getLeft(){
        return this.left;
    }

    public Expression getRight(){
        return this.right;
    }

    @Override
    public Token getType() {
        return left.getType();
    }

    @Override
    public void check(SymbolTable ts, ConcreteClass currentClass, ConcreteMethod currentMethod) throws SemanticException {
        //first check the left expression
        this.left.check(ts, currentClass, currentMethod);
        //then check the right expression
        this.right.check(ts, currentClass, currentMethod);

        //if operator is + or - then both expressions must the same, int or float
        if(this.operator.getLexeme().equals("+") || this.operator.getLexeme().equals("-")){
            if(!this.left.getType().getName().equals("pr_int") && !this.left.getType().getName().equals("pr_float")){
                throw new SemanticException(this.operator,"Error: las expresiones deben ser de tipo int o float");
            }
            if(!this.left.getType().getName().equals(this.right.getType().getName())){
                throw new SemanticException(this.operator,"Error: las expresiones deben ser del mismo tipo");
            }
        }


    }
}
