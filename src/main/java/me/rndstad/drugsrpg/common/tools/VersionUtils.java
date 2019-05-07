package me.rndstad.drugsrpg.common.tools;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class VersionUtils {

    public static String getLatestSpigotVersion(String resourceId) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resourceId).getBytes(StandardCharsets.UTF_8));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (version.length() <= 7) {
                return version;
            }
        } catch (Exception ex) {
            Bukkit.getLogger().info("Failed to check for an update on Spigot.");
        }
        return "error";
    }
}
