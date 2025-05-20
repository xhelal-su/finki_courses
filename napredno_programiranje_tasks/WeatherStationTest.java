package midterm1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


class Measure {
    float temperature;
    float wind;
    float humidity;
    float visibility;
    Date date;

    Measure(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        return temperature + " " + wind + " km/h " + humidity + "% " + visibility + " km " + sdf.format(date);
    }
}

class WeatherStation {
    List<Measure> weatherList;
    int days;

    WeatherStation(int days) {
        this.days = days;
        weatherList = new ArrayList<>();
    }
    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date) {
        weatherList.removeIf(w -> Math.abs(date.getTime() - w.date.getTime()) > TimeUnit.DAYS.toMillis(days));

        for (Measure curr : weatherList) {
            if (Math.abs(date.getTime() - curr.date.getTime()) < 150000)
                return;
        }

        weatherList.add(new Measure(temperature, wind, humidity, visibility, date));
    }

    public int total() {
        return weatherList.size();
    }
    public void status(Date from, Date to) {
        List<Measure> filtered = weatherList.stream()
                .filter(w -> !w.date.before(from) && !w.date.after(to))
                .sorted(Comparator.comparing(a -> a.date))
                .collect(Collectors.toList());


        if (filtered.isEmpty())
            throw new RuntimeException();

        filtered.forEach(System.out::println);
        System.out.printf("Average temperature: %.2f%n", filtered.stream().mapToDouble(w -> w.temperature).average().orElse(0.0));
    }
}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}
