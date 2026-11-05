public class CashPayment implements PaymentMethod {

    @Override
    public String getName() {
        return "Cash";
    }

    @Override
    public double fee(double amount) {
        return 0;
    }
}
