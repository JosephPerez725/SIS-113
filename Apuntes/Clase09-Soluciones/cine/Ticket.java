/**
 * A regular cinema ticket. The base of the hierarchy.
 *
 *              Ticket
 *             /      \
 *   DiscountTicket   PremiumTicket
 *     /        \
 * StudentTicket SeniorTicket
 */
public class Ticket {

    private final String movie;
    private final String seat;
    private final double basePrice;

    public Ticket(String movie, String seat, double basePrice) {
        if (movie == null || movie.isBlank()) {
            throw new IllegalArgumentException("movie is required");
        }
        if (seat == null || seat.isBlank()) {
            throw new IllegalArgumentException("seat is required");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("base price must be positive, got " + basePrice);
        }
        this.movie = movie;
        this.seat = seat;
        this.basePrice = basePrice;
    }

    public String getMovie() {
        return movie;
    }

    public String getSeat() {
        return seat;
    }

    /** protected: the children need it to compute their price; the outside world does not. */
    protected double getBasePrice() {
        return basePrice;
    }

    /** What the customer pays. Children override this. */
    public double getPrice() {
        return basePrice;
    }

    public String getType() {
        return "Regular";
    }

    @Override
    public String toString() {
        return getType() + " | " + movie + " | seat " + seat + " | Bs " + getPrice();
    }
}
