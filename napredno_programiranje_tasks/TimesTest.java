package midterm1;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class UnsupportedFormatException extends Exception {
    String str;
    UnsupportedFormatException(String str) {
        super(str);
    }
}
class InvalidTimeException extends Exception {
    String str;
    InvalidTimeException(String str) {
        super(str);
    }
}
class TimeTable {
    List<LocalTime> times;

    TimeTable() {
        times = new ArrayList<>();
    }
    private void readOneTime(String part) throws UnsupportedFormatException, InvalidTimeException {
        String[] p = part.split("[.:]");
        if (p.length == 1)
            throw new UnsupportedFormatException(part);
        int hour = Integer.parseInt(p[0]);
        int min = Integer.parseInt(p[1]);
        if (hour < 0 || hour > 23 || min < 0 || min > 59)
            throw new InvalidTimeException(part);
        times.add(LocalTime.of(hour, min));
    }
    void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\s+");
            if (parts.length > 1) {
                for (int i = 0; i < parts.length; i++)
                    readOneTime(parts[i]);
            } else {
                readOneTime(parts[0]);
            }
        }
    }
    void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintStream pw = new PrintStream(outputStream);

        times = times.stream().sorted().collect(Collectors.toList());

        for (LocalTime time : times) {
            if (format == TimeFormat.FORMAT_24) {
                int hour = time.getHour();
                int min = time.getMinute();
                pw.println(String.format("%2d:%02d", hour, min));
            } else {
                String post = "AM";
                int hour = time.getHour();
                int min = time.getMinute();
                if (hour == 0) {
                    hour = 12;
                } else if (hour < 12) {
                    post = "AM";
                } else if (hour == 12) {
                    post = "PM";
                } else {
                    hour -= 12;
                    post = "PM";
                }
                pw.println(String.format("%2d:%02d %s", hour, min, post));
            }
        }
    }
}

public class TimesTest {

    public static void main(String[] args) throws IOException {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }
}
enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}