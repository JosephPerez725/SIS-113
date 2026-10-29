// Drill 1 — it does not compile. Read the message: who is Java looking for?
//   java Drill1MissingSuper.java

public class Drill1MissingSuper {

    public static void main(String[] args) {
        Premiere premiere = new Premiere("Dune: Part Three", "Zendaya");
        System.out.println(premiere.describe());   // expected: Dune: Part Three, with Zendaya
    }
}

class Movie {

    private final String title;

    Movie(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }
}

class Premiere extends Movie {

    private final String guest;

    Premiere(String title, String guest) {
        this.guest = guest;
    }

    String describe() {
        return getTitle() + ", with " + guest;
    }
}
