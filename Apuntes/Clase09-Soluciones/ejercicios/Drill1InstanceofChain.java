// Drill 1 — SOLUTION.
// The cashier no longer decides prices: each entry already knows its own.
// A new entry type now works without touching Cashier.
//   java Drill1InstanceofChain.java

import java.util.ArrayList;
import java.util.List;

public class Drill1InstanceofChain {

    public static void main(String[] args) {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry());
        entries.add(new ChildEntry());
        entries.add(new SeniorEntry());
        entries.add(new KidEntry());

        Cashier cashier = new Cashier();
        System.out.println("total: Bs " + cashier.total(entries));   // expected: total: Bs 120.0
    }
}

class Entry {
    double getPrice() {
        return 50;
    }
}

class ChildEntry extends Entry {
    @Override
    double getPrice() {
        return 25;
    }
}

class SeniorEntry extends Entry {
    @Override
    double getPrice() {
        return 30;
    }
}

class KidEntry extends Entry {
    @Override
    double getPrice() {
        return 15;
    }
}

class Cashier {

    double total(List<Entry> entries) {
        double total = 0;
        for (Entry entry : entries) {
            total += entry.getPrice();   // FIX: ask the entry, do not guess its type
        }
        return total;
    }
}
