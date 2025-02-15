package Client.utils;
import java.util.Timer;
import java.util.TimerTask;
public class TimerUtil {
    private int seconds;
    private int minutes;
    static int interval;
    static Timer timer;

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void addTime() {
        seconds += 10;
    }

    public void subtractTime() {
        seconds -= 10;
    }

    public void countdown() { //displays a string of the time; line by line
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        interval = 1800;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                int seconds = setInterval();
                String time = String.format("%02d:%02d", seconds / 60, seconds % 60);
                System.out.println(time);
            }
        }, delay, period);
    }
    private static int setInterval() {
        if (interval == 1)
            timer.cancel();
        return interval--;
    }

    public String toString() {
        return "BombTimer{" +
                "seconds=" + seconds +
                ", minutes=" + minutes +
                '}';
    }
}

