package online.starsmc.simplesetspawn.module;

import online.starsmc.simplesetspawn.Main;
import online.starsmc.simplesetspawn.service.CommandService;
import online.starsmc.simplesetspawn.service.ConfigurationService;
import online.starsmc.simplesetspawn.service.ListenerService;
import online.starsmc.simplesetspawn.service.Service;
import online.starsmc.simplesetspawn.utils.BukkitConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import team.unnamed.inject.AbstractModule;

public class PluginModule extends AbstractModule {
        private final Main plugin;

        public PluginModule(Main plugin) {
            this.plugin = plugin;
        }

        @Override
        protected void configure() {
            FileConfiguration config = plugin.getConfig();

            bind(Main.class).toInstance(plugin);
            bind(FileConfiguration.class).toInstance(config);

            multibind(Service.class)
                    .asSet()
                    .to(CommandService.class)
                    .to(ListenerService.class)
                    .to(ConfigurationService.class);

            bind(BukkitConfiguration.class)
                    .named("spawns")
                    .toInstance(new BukkitConfiguration(plugin, "spawns"));

            install(new CommandModule());
            install(new ListenerModule());
        }
}
