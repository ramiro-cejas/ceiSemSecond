package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class SentenceReturn implements Sentence{
    private ExpressionBinary expression;

    public SentenceReturn(){
        this.expression = null;
    }

    public void setExpression(ExpressionBinary expression){
        this.expression = expression;
    }

    public ExpressionBinary getExpression(){
        return this.expression;
    }

    @Override
    public void check(SymbolTable ts, ConcreteClass currentClass, ConcreteMethod currentMethod) throws SemanticException {
        if(this.expression == null){
            if(!currentMethod.type.getName().equals("pr_void")){
                throw new SemanticException(currentMethod.name,"Error: el metodo "+currentMethod.name+" debe retornar un valor de tipo "+currentMethod.type.getName());
            }
        }else{
            if(!currentMethod.type.getName().equals(this.expression.getType().getName())){
                throw new SemanticException(currentMethod.name,"Error: el metodo "+currentMethod.name+" debe retornar un valor de tipo "+currentMethod.type.getName());
            }
        }
    }
}
