// Drill 2 — SOLUTION.
// Check the real type before using what only staff badges have.
//   java Drill2BadDowncast.java

import java.util.ArrayList;
import java.util.List;

public class Drill2BadDowncast {

    public static void main(String[] args) {
        List<Badge> badges = new ArrayList<>();
        badges.add(new StaffBadge("Ana", "S-001"));
        badges.add(new GuestBadge("Luis"));
        badges.add(new StaffBadge("Rosa", "S-002"));

        for (Badge badge : badges) {
            // FIX: pattern matching checks the type and casts in one step
            if (badge instanceof StaffBadge staff) {
                System.out.println(badge.getName() + " · staff id " + staff.getStaffId());
            } else {
                System.out.println(badge.getName());
            }
        }
        // expected:
        //   Ana · staff id S-001
        //   Luis
        //   Rosa · staff id S-002
    }
}

class Badge {

    private final String name;

    Badge(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class StaffBadge extends Badge {

    private final String staffId;

    StaffBadge(String name, String staffId) {
        super(name);
        this.staffId = staffId;
    }

    String getStaffId() {
        return staffId;
    }
}

class GuestBadge extends Badge {

    GuestBadge(String name) {
        super(name);
    }
}
