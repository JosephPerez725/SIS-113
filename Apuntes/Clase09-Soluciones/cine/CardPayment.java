public class CardPayment implements PaymentMethod {

    private static final int FEE_PERCENT = 2;

    @Override
    public String getName() {
        return "Card";
    }

    @Override
    public double fee(double amount) {
        return amount * FEE_PERCENT / 100;
    }
}
