/**
 * A contract with no code inside: anything that can pay must say its name
 * and how much it charges on top.
 *
 * Interface, not abstract class: cash, card and QR share no data at all.
 * They only share what they can DO.
 */
public interface PaymentMethod {

    String getName();

    double fee(double amount);
}
