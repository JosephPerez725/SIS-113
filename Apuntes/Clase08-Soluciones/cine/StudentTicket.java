public class StudentTicket extends DiscountTicket {

    private final String studentId;

    public StudentTicket(String movie, String seat, double basePrice, String studentId) {
        super(movie, seat, basePrice);
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("student id is required");
        }
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    @Override
    protected int getDiscountPercent() {
        return 30;
    }

    @Override
    public String getType() {
        return "Student";
    }
}
