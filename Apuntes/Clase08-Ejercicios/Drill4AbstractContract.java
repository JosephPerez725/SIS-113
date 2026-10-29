// Drill 4 — it does not compile. The parent made a demand that the child ignored.
//   java Drill4AbstractContract.java

public class Drill4AbstractContract {

    public static void main(String[] args) {
        Screening morning = new MorningScreening(50);
        System.out.println("morning: Bs " + morning.finalPrice());   // expected: morning: Bs 40.0
    }
}

abstract class Screening {

    private final double basePrice;

    Screening(double basePrice) {
        this.basePrice = basePrice;
    }

    abstract int discountPercent();

    double finalPrice() {
        return basePrice * (100 - discountPercent()) / 100.0;
    }
}

class MorningScreening extends Screening {

    MorningScreening(double basePrice) {
        super(basePrice);
    }
}
