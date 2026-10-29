/**
 * Any ticket with a percentage discount.
 *
 * abstract: nobody buys "a discount ticket". You buy a student or a senior
 * ticket. This class holds what they share: how a discount is applied.
 */
public abstract class DiscountTicket extends Ticket {

    protected DiscountTicket(String movie, String seat, double basePrice) {
        super(movie, seat, basePrice);
    }

    /** Each child says how much. This class says how it is applied. */
    protected abstract int getDiscountPercent();

    @Override
    public double getPrice() {
        return getBasePrice() * (100 - getDiscountPercent()) / 100.0;
    }
}
