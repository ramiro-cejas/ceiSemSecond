///[Error:&&|22]
class Main {
    static void main() {}
}

class A {
    int i;
}

class B {
    A a;
}

class C {
    B bb;
    B b() {return bb;}
}

class D {
    void m() {
        var x = new C();
        var e = true && x.b().a.i;
    }
}