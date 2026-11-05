import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Sentinel — Clase 9: several rules, one loop.
 *
 *   java -cp out Main 80 60 < ../../phase1-bash/samples/muestra-2026-09-17.txt
 *                     ^   ^
 *                memory  cpu
 */
public class Main {

    public static void main(String[] args) {
        int memoryThreshold = args.length > 0 ? Integer.parseInt(args[0]) : 80;
        int cpuThreshold = args.length > 1 ? Integer.parseInt(args[1]) : 60;

        // Declared type: Rule. Runtime types: MemoryRule and CpuRule.
        List<Rule> rules = new ArrayList<>();
        rules.add(new MemoryRule(memoryThreshold));
        rules.add(new CpuRule(cpuThreshold));

        History history = new History();
        Console console = new Console();
        int read = 0;
        int invalid = 0;

        console.header(rules);

        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            String line = input.nextLine();
            read++;
            try {
                Snapshot reading = Contract.parse(line);
                history.add(reading);

                // Every rule must be evaluated, even after one already alerted,
                // or its hit counter would miss readings.
                boolean alert = false;
                for (Rule rule : rules) {
                    if (rule.evaluate(reading)) {
                        alert = true;
                    }
                }
                console.show(reading, alert);
            } catch (IllegalArgumentException e) {
                invalid++;
                console.invalidLine(line, e.getMessage());
            }
        }

        console.summary(history, rules, read, invalid);
    }
}
