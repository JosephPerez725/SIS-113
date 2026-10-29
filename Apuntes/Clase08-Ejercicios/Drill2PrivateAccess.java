// Drill 2 — it does not compile. The child inherits the field... or does it?
//   java Drill2PrivateAccess.java

public class Drill2PrivateAccess {

    public static void main(String[] args) {
        VipSeat seat = new VipSeat("A1", 40);
        System.out.println(seat.total());   // expected: 60.0
    }
}

class Seat {

    private final String code;
    private final double price;

    Seat(String code, double price) {
        this.code = code;
        this.price = price;
    }

    String getCode() {
        return code;
    }
}

class VipSeat extends Seat {

    VipSeat(String code, double price) {
        super(code, price);
    }

    double total() {
        return price * 1.5;
    }
}
