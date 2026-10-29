public class Main {

    public static void main(String[] args) {
        Ticket regular = new Ticket("Dune: Part Three", "F7", 50);
        StudentTicket student = new StudentTicket("Dune: Part Three", "F8", 50, "UCB-2026-114");
        SeniorTicket senior = new SeniorTicket("Dune: Part Three", "F9", 50, 67);
        PremiumTicket premium = new PremiumTicket("Dune: Part Three", "A1", 50, true);

        System.out.println(regular);
        System.out.println(student);
        System.out.println(senior);
        System.out.println(premium);

        double total = regular.getPrice() + student.getPrice() + senior.getPrice() + premium.getPrice();
        System.out.println("Total: Bs " + total);
    }
}
