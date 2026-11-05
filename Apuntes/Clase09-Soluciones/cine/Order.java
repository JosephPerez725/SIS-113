import java.util.ArrayList;
import java.util.List;

/**
 * A purchase: several tickets of ANY type, and one way to pay.
 *
 * Order never asks "what kind of ticket is this?". It asks every ticket the
 * same question, getPrice(), and each one answers in its own way. That is
 * polymorphism.
 */
public class Order {

    private final List<Ticket> tickets = new ArrayList<>();

    public void add(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("ticket is required");
        }
        tickets.add(ticket);
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (Ticket ticket : tickets) {
            subtotal += ticket.getPrice();
        }
        return subtotal;
    }

    /** Works with ANY payment method, including ones that do not exist yet. */
    public double totalWith(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("payment method is required");
        }
        double subtotal = getSubtotal();
        return subtotal + method.fee(subtotal);
    }

    /** A copy: nobody outside can add or remove tickets without add(). */
    public List<Ticket> getTickets() {
        return new ArrayList<>(tickets);
    }

    public int size() {
        return tickets.size();
    }
}
