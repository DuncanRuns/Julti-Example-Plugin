package xyz.duncanruns.julti.exampleplugin;

import com.google.common.io.Resources;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiAppLaunch;
import xyz.duncanruns.julti.plugin.PluginEvents;
import xyz.duncanruns.julti.plugin.PluginInitializer;
import xyz.duncanruns.julti.plugin.PluginManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;

public class ExamplePlugin implements PluginInitializer {
    public static void main(String[] args) throws IOException {
        // This is only used to test the plugin in the dev environment
        // ExamplePlugin.main itself is never used when users run Julti

        JultiAppLaunch.launchWithDevPlugin(args, PluginManager.JultiPluginData.fromString(
                Resources.toString(Resources.getResource(ExamplePlugin.class, "/julti.plugin.json"), Charset.defaultCharset())
        ));
    }

    @Override
    public void initialize() {
        // This gets run once when Julti launches

        Julti.log(Level.INFO, "Example Plugin Initialized");

        PluginEvents.registerRunnableEvent(PluginEvents.RunnableEventType.RELOAD, () -> {
            // This gets run when Julti launches and every time the profile is switched
            Julti.log(Level.INFO, "Example Plugin Reloaded!");
        });

        AtomicLong timeTracker = new AtomicLong(System.currentTimeMillis());

        PluginEvents.registerRunnableEvent(PluginEvents.RunnableEventType.END_TICK, () -> {
            // This gets run every tick (1 ms)
            long currentTime = System.currentTimeMillis();
            if (currentTime - timeTracker.get() > 3000) {
                // This gets ran every 3 seconds
                Julti.log(Level.INFO, "Example Plugin ran for another 3 seconds.");
                timeTracker.set(currentTime);
            }
        });

        PluginEvents.registerRunnableEvent(PluginEvents.RunnableEventType.STOP, () -> {
            // This gets run when Julti is shutting down
            Julti.log(Level.INFO, "Example plugin shutting down...");
        });
    }
}
