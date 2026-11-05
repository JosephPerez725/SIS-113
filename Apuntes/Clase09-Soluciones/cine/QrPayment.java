public class QrPayment implements PaymentMethod {

    private final String bank;

    public QrPayment(String bank) {
        if (bank == null || bank.isBlank()) {
            throw new IllegalArgumentException("bank is required");
        }
        this.bank = bank;
    }

    @Override
    public String getName() {
        return "QR (" + bank + ")";
    }

    @Override
    public double fee(double amount) {
        return 0;
    }
}
