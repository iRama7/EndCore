package rama;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static rama.EndCore.plugin;

public class CronMain {

    /*al activar el plugin
        crear un cronómetro para cada día definido en configuración
            ese cronometro tiene como delay el tiempo restante para su día
      función que devuelva en mili segundos cuánto tiempo falta para una fecha en específico.

     */


    int hour = 0;
    int minute = 0;
    private List<String> commands = new ArrayList<>();

    List<DayOfWeek> days = new ArrayList<>();

    public void startCrons(List<DayOfWeek> days){
        FileConfiguration config = plugin.getConfig();
        for(DayOfWeek day : days){
            startCron(getTimeUntil(day, hour, minute), day, config);
        }
    }


    public Long getTimeUntil(DayOfWeek day, int hour, int minute){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetDateTime = now.with(DayOfWeek.from(day))
                .withHour(hour)
                .withMinute(minute)
                .withSecond(0)
                .withNano(0);

        if (targetDateTime.isBefore(now)) {
            targetDateTime = targetDateTime.plusWeeks(1); // Obtener la próxima ocurrencia del día de la semana objetivo
        }

        long delayInMillis = ChronoUnit.MILLIS.between(now, targetDateTime);
        return delayInMillis + 1000;
    }


    public void startCron(long delay, DayOfWeek day, FileConfiguration config){
        //"Sab, Dom, Mie - 20"
        List<String> commands = config.getStringList("config.dragon_commands");
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, () -> {
            for(String command : commands){
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            startCron(getTimeUntil(day, hour, minute), day, config);
        }, (delay / 1000) * 20);
        Bukkit.getLogger().info("creating cron with " + delay / 1000 + " seconds");
    }

    public String getTimeFormatted(long timeRemaining){
        long days = TimeUnit.MILLISECONDS.toDays(timeRemaining);
        timeRemaining -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(timeRemaining);
        timeRemaining -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
        timeRemaining -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeRemaining);
        String time = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("language.time_string"));
        return String.format(time, days, hours, minutes, seconds);
    }

    public void buildUtils(String timeString){

        commands.addAll(plugin.getConfig().getStringList("config.dragon_commands"));

        //"Sab, Dom, Mie - 20:00"
        String parts[] = timeString.split(" - ");
        //"Sab, Dom, Mie 20:00"
        String daysString = parts[0]; //Sab, Dom, Mie
        String daySplitter[] = daysString.split(", ");
        for (String day : daySplitter) {
            days.add(DayOfWeek.of(Integer.parseInt(day)));
            Bukkit.getLogger().warning("Adding day: " + day + " " + DayOfWeek.of(Integer.parseInt(day)).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }
        String hourString[] = parts[1].split(":"); //20:00
        hour = Integer.parseInt(hourString[0]);
        minute = Integer.parseInt(hourString[1]);
        startCrons(days);
    }

    public Long nearestTimer(List<DayOfWeek> days){
        long nearestExecutionInMillis = 0;
        for(DayOfWeek day : days){
            if(nearestExecutionInMillis == 0 || getTimeUntil(day, hour, minute) < nearestExecutionInMillis){
                nearestExecutionInMillis = getTimeUntil(day, hour, minute);
            }
        }
        return nearestExecutionInMillis;
    }

    public List<DayOfWeek> getDays() {
        return days;
    }
}
