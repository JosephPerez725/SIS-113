// Drill 3 — it compiles and runs, but the combo costs the same as a snack.
// First find why. Then add the one annotation that would have caught it.
//   java Drill3MissingOverride.java

public class Drill3MissingOverride {

    public static void main(String[] args) {
        Snack popcorn = new Snack();
        Combo combo = new Combo();
        System.out.println("popcorn: Bs " + popcorn.getPrice());   // expected: popcorn: Bs 10.0
        System.out.println("combo:   Bs " + combo.getPrice());     // expected: combo:   Bs 25.0
    }
}

class Snack {

    double getPrice() {
        return 10;
    }
}

class Combo extends Snack {

    double getprice() {
        return 25;
    }
}
