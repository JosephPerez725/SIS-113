// Drill 5 — it compiles and runs. Nothing crashes. And still, something is wrong.
// Read the output, then the class declaration, then answer: is popcorn a ticket?
//   java Drill5WrongIsA.java

public class Drill5WrongIsA {

    public static void main(String[] args) {
        Popcorn large = new Popcorn("Large", 20);
        System.out.println(large.describe());
    }
}

class Pass {

    private final String movie;
    private final String seat;
    private final double price;

    Pass(String movie, String seat, double price) {
        this.movie = movie;
        this.seat = seat;
        this.price = price;
    }

    String describe() {
        return movie + " | seat " + seat + " | Bs " + price;
    }
}

/** "Popcorn also has a price, so let's reuse Pass." */
class Popcorn extends Pass {

    Popcorn(String size, double price) {
        super(size + " popcorn", "none", price);
    }
}
