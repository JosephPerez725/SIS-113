public class PremiumTicket extends Ticket {

    private static final double SNACK_PRICE = 15;

    private final boolean withSnack;

    public PremiumTicket(String movie, String seat, double basePrice, boolean withSnack) {
        super(movie, seat, basePrice);
        this.withSnack = withSnack;
    }

    public boolean hasSnack() {
        return withSnack;
    }

    /** Reclining seat: 50 % more. The snack is optional. */
    @Override
    public double getPrice() {
        double price = getBasePrice() * 1.5;
        if (withSnack) {
            price += SNACK_PRICE;
        }
        return price;
    }

    @Override
    public String getType() {
        return "Premium";
    }

    /** super.toString() reuses the parent's text and adds to it. */
    @Override
    public String toString() {
        return super.toString() + (withSnack ? " (with snack)" : "");
    }
}
