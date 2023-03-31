package rama;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class DelayedTask {

    private final Plugin plugin;
    private int taskId;
    private long delayHours;
    private long timeRemaining;
    private boolean taskExecuted;
    private long startTime;
    private long elapsedTime;

    private long configDelayHours;

    public DelayedTask(Plugin plugin, long delayHours) {
        this.plugin = plugin;
        this.delayHours = delayHours;
        configDelayHours = plugin.getConfig().getLong("config.restart_time");
    }

    public void start() {
        timeRemaining = delayHours * 60 * 60 * 20;
        Bukkit.getLogger().info("Starting task");

        startTime = System.currentTimeMillis();

        taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                executeTask();
            }
        }, delayHours * 60 * 60 * 20);
    }

    public Long getRemainingTime() {
        elapsedTime = startTime - System.currentTimeMillis();
        elapsedTime = abs(elapsedTime);
        timeRemaining = (delayHours * 3600000) - elapsedTime;
        return timeRemaining;
    }

    public String getTimeFormatted(){
        timeRemaining = getRemainingTime();
        long days = TimeUnit.MILLISECONDS.toDays(timeRemaining);
        timeRemaining -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(timeRemaining);
        timeRemaining -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
        timeRemaining -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeRemaining);
        return String.format("%d días, %d horas, %d minutos, %d segundos", days, hours, minutes, seconds);
    }

    public void changeDelay(long delayHours) {
        this.delayHours = delayHours;
        Bukkit.getScheduler().cancelTask(taskId);
        start();
        this.delayHours = configDelayHours;
    }

    private synchronized void executeTask() {
        try {
            startTime = System.currentTimeMillis();
            WorldManagement wm = new WorldManagement();
            wm.restartWorld();
            Bukkit.getScheduler().cancelTask(taskId);
            this.delayHours = configDelayHours;
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTime() {
        plugin.getConfig().set("time", getRemainingTime());
        plugin.saveConfig();
        plugin.reloadConfig();
    }
}
