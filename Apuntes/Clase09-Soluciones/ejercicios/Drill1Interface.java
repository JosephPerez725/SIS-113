// Drill 1 — ALTERNATIVE: Entry as an interface.
// Same fixed Cashier, plus a guarantee: a new entry type that forgets its
// price does not compile. The cost: the adult entry needs its own class.

import java.util.ArrayList;
import java.util.List;

public class Drill1Interface {

    public static void main(String[] args) {
        List<Entry> entries = new ArrayList<>();
        entries.add(new AdultEntry());
        entries.add(new ChildEntry());
        entries.add(new SeniorEntry());
        entries.add(new KidEntry());

        Cashier cashier = new Cashier();
        System.out.println("total: Bs " + cashier.total(entries));
    }
}

interface Entry {
    double getPrice();
}

class AdultEntry implements Entry {
    @Override
    public double getPrice() {
        return 50;
    }
}

class ChildEntry implements Entry {
    @Override
    public double getPrice() {
        return 25;
    }
}

class SeniorEntry implements Entry {
    @Override
    public double getPrice() {
        return 30;
    }
}

class KidEntry implements Entry {
    @Override
    public double getPrice() {
        return 15;
    }
}

class Cashier {
    double total(List<Entry> entries) {
        double total = 0;
        for (Entry entry : entries) {
            total += entry.getPrice();
        }
        return total;
    }
}
