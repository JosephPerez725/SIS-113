public class SeniorTicket extends DiscountTicket {

    private static final int MIN_AGE = 60;

    private final int age;

    public SeniorTicket(String movie, String seat, double basePrice, int age) {
        super(movie, seat, basePrice);
        if (age < MIN_AGE) {
            throw new IllegalArgumentException("senior tickets are for " + MIN_AGE + "+, got " + age);
        }
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    protected int getDiscountPercent() {
        return 40;
    }

    @Override
    public String getType() {
        return "Senior";
    }
}
