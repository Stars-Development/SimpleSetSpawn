package online.starsmc.simplesetspawn.utils.external;

import online.starsmc.simplesetspawn.utils.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final int resourceId;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    public void start() {
        if(plugin.getConfig().getBoolean("update_checker")) {
            String serverVersion = plugin.getDescription().getVersion();
            VersionUtil serverVersionVUtil = new VersionUtil(serverVersion);

            new UpdateChecker(plugin, this.resourceId).getVersion(version -> {
                VersionUtil versionUtilV = new VersionUtil(version);

                if (!serverVersion.equals(version)) {
                    if (serverVersionVUtil.compareTo(versionUtilV) > 0) {
                        plugin.getLogger().warning("It seems that you are using a DEV version. May have bugs");
                        plugin.getLogger().warning("Your version: " + serverVersion);
                        plugin.getLogger().warning("Latest version: " + version);
                        plugin.getLogger().warning("Download it here https://www.spigotmc.org/resources/simplesetspawn-1-8-1-19-simple-setspawn-and-spawn.108767/");
                        return;
                    }

                    plugin.getLogger().warning("Found new version: " + version);
                    plugin.getLogger().warning("Your version: " + serverVersion);
                    plugin.getLogger().warning("Download it here https://www.spigotmc.org/resources/simplesetspawn-1-8-1-19-simple-setspawn-and-spawn.108767/");
                    return;
                }
                plugin.getLogger().info("Current VersionUtil: " + serverVersion);
                plugin.getLogger().info("No new version available.");
            });
        }
    }
}
