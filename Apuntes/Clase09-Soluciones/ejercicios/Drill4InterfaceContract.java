// Drill 4 — SOLUTION.
// implements is a promise: every method of the interface must be written.
//   java Drill4InterfaceContract.java

public class Drill4InterfaceContract {

    public static void main(String[] args) {
        PaymentOption crypto = new CryptoOption();
        System.out.println(crypto.getName() + " fee on Bs 200: " + crypto.fee(200));   // expected: Crypto fee on Bs 200: 5.0
    }
}

interface PaymentOption {

    String getName();

    double fee(double amount);
}

class CryptoOption implements PaymentOption {

    @Override
    public String getName() {
        return "Crypto";
    }

    // FIX: the missing method of the contract
    @Override
    public double fee(double amount) {
        return amount * 2.5 / 100;
    }
}
