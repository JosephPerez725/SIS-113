// Drill 5 — it compiles and runs, but the night fare is never applied.
// The author THOUGHT they overrode getPrice. Did they?
//   java Drill5OverloadNotOverride.java

public class Drill5OverloadNotOverride {

    public static void main(String[] args) {
        Fare day = new Fare();
        Fare night = new NightFare();
        System.out.println("day:   Bs " + day.getPrice());     // expected: day:   Bs 50.0
        System.out.println("night: Bs " + night.getPrice());   // expected: night: Bs 35.0
    }
}

class Fare {

    double getPrice() {
        return 50;
    }
}

class NightFare extends Fare {

    double getPrice(int discount) {
        return 50 - discount;
    }
}
