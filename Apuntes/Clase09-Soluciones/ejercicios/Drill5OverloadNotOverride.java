// Drill 5 — SOLUTION.
// getPrice(int) was an OVERLOAD: a different method. To replace the parent's
// method, the signature must be identical, and @Override proves it.
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

    // FIX: same signature as Fare.getPrice(), with @Override as the safety check
    @Override
    double getPrice() {
        return 35;
    }
}
