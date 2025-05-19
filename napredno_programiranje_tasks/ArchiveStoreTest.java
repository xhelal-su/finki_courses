package midterm1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class NonExistingItemException extends Exception {
    String str;
    NonExistingItemException(String str) {
        super(str);
    }
}

abstract class Archive {
    int id;
    LocalDate dateArchived;
    Archive(int id) {
        this.id = id;
        this.dateArchived = null;
    }
    void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }
    abstract boolean canBeOpened(LocalDate date);
}

class LockedArchive extends Archive {
    LocalDate dateToOpen;
    LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }
    boolean canBeOpened(LocalDate date) {
        return date.isAfter(this.dateToOpen);
    }
}

class SpecialArchive extends Archive {
    int maxOpen;
    int counter;
    SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        counter = 0;
    }
    boolean canBeOpened(LocalDate date) {
        if (counter < maxOpen) {
            counter++;
            return true;
        }
        return false;
    }
}

class ArchiveStore {
    List<Archive> archiveList;
    List<String> archiveStrings;

    ArchiveStore() {
        archiveList = new ArrayList<>();
        archiveStrings = new ArrayList<>();
    }
    void archiveItem(Archive item, LocalDate date) {
        item.setDateArchived(date);

        archiveList.add(item);
        archiveStrings.add(String.format("Item %d archived at %s", item.id, date.toString()));
    }
    void openItem(int id, LocalDate date) throws NonExistingItemException {
        Archive removed = null;
        boolean flag = false;
        for (Archive curr : archiveList) {
            if (curr.id == id) {
                removed = curr;
                if (curr.canBeOpened(date)) {
                    break;
                }
                else {
                    flag = true;
                    if (curr instanceof LockedArchive)
                        archiveStrings.add(String.format("Item %d cannot be opened before %s", curr.id, ((LockedArchive) curr).dateToOpen.toString()));
                    else
                        archiveStrings.add(String.format("Item %d cannot be opened more than %d times", curr.id, ((SpecialArchive) curr).maxOpen));
                }
            }

        }
        if (removed == null)
            throw new NonExistingItemException(String.format("Item with id %d doesn't exist", id));
        else if (!flag)
            archiveStrings.add(String.format("Item %d opened at %s", removed.id, date.toString()));
    }
    String getLog() {
        StringBuilder sb = new StringBuilder();
        archiveStrings.forEach(a -> sb.append(a).append("\n"));
        return sb.toString();
    }
}


public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}