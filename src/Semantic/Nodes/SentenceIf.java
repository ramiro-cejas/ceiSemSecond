package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Semantic.SemanticException;

public class SentenceIf implements Sentence{
    public ExpressionBinary condition;
    public Sentence sentenceThen;
    public Sentence sentenceElse;

    public SentenceIf(){
        this.condition = null;
        this.sentenceThen = null;
        this.sentenceElse = null;
    }

    public void setCondition(ExpressionBinary condition){
        this.condition = condition;
    }

    public void setSentenceThen(Sentence sentenceThen){
        this.sentenceThen = sentenceThen;
    }

    public void setSentenceElse(Sentence sentenceElse){
        this.sentenceElse = sentenceElse;
    }

    public ExpressionBinary getCondition(){
        return this.condition;
    }

    public Sentence getSentenceThen(){
        return this.sentenceThen;
    }

    public Sentence getSentenceElse(){
        return this.sentenceElse;
    }

    @Override
    public void check() throws SemanticException {
        if(!this.condition.getType().getName().equals("pr_boolean")){
            throw new SemanticException(this.condition.getType(),"Error: la condicion del if debe ser de tipo boolean");
        }
        this.sentenceThen.check();
        this.sentenceElse.check();
    }

}
