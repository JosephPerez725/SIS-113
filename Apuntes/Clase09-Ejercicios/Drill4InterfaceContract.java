// Drill 4 — it does not compile. The class promised something it did not deliver.
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
}
