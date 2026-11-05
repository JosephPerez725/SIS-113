// Drill 3 — it does not compile. The object HAS the method. So why?
//   java Drill3DeclaredType.java

public class Drill3DeclaredType {

    public static void main(String[] args) {
        Voucher voucher = new GiftVoucher(100, "Happy birthday!");
        System.out.println("amount: Bs " + voucher.getAmount());
        System.out.println("message: " + voucher.getMessage());   // expected: message: Happy birthday!
    }
}

class Voucher {

    private final double amount;

    Voucher(double amount) {
        this.amount = amount;
    }

    double getAmount() {
        return amount;
    }
}

class GiftVoucher extends Voucher {

    private final String message;

    GiftVoucher(double amount, String message) {
        super(amount);
        this.message = message;
    }

    String getMessage() {
        return message;
    }
}
