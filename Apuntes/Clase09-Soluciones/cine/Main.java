import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Order order = new Order();
        order.add(new Ticket("Dune: Part Three", "F7", 50));
        order.add(new StudentTicket("Dune: Part Three", "F8", 50, "UCB-2026-114"));
        order.add(new SeniorTicket("Dune: Part Three", "F9", 50, 67));
        order.add(new PremiumTicket("Dune: Part Three", "A1", 50, true));

        System.out.println("== Receipt");
        for (Ticket ticket : order.getTickets()) {
            System.out.println(ticket);
            // A legitimate instanceof: only students have an id to show.
            if (ticket instanceof StudentTicket student) {
                System.out.println("    student id: " + student.getStudentId());
            }
        }
        System.out.println("Subtotal: Bs " + order.getSubtotal());

        System.out.println();
        System.out.println("== Payment options");
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(new CashPayment());
        methods.add(new CardPayment());
        methods.add(new QrPayment("BNB"));
        for (PaymentMethod method : methods) {
            System.out.println(method.getName() + ": Bs " + order.totalWith(method));
        }
    }
}
