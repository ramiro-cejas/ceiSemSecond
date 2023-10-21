package SecondSemantic.Semantic;

import SecondSemantic.Lexical.Token;

public class ConcreteAttribute {

    Token isStatic;
    Token name;
    Token type;

    public ConcreteAttribute(Token name, Token type, Token isStatic) {
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
    }

    public Token getName() {
        return name;
    }

    public Token getType() {
        return type;
    }

    public Token isStatic() {
        return isStatic;
    }
}
