
package ff;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkAnalyzer {

    public static void main(String[] args) {
        // Set the parameters directly
        String host = "google.com";
        int interval = 5; // seconds
        int count = 10;

        analyzePerformance(host, interval, count);
    }

    public static void analyzePerformance(String host, int interval, int count) {
        System.out.println("Analyzing network performance to " + host + " every " + interval + " seconds for " + count + " times...");
        try {
            for (int i = 0; i < count; i++) {
                ProcessBuilder processBuilder = new ProcessBuilder("ping", "-n", "1", host);
                Process process = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                int rtt = parseRTT(output.toString());
                int packetLoss = parsePacketLoss(output.toString());
                int uptime = parseUptime(output.toString());

                System.out.println("Round-trip time (RTT): " + rtt + " ms");
                System.out.println("Packet loss: " + packetLoss + "%");
                System.out.println("Uptime: " + uptime + " ms");

                Thread.sleep(interval * 1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int parseRTT(String output) {
        Pattern pattern = Pattern.compile("time=([0-9]+)ms");
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1; // Return -1 if not found
    }

    public static int parsePacketLoss(String output) {
        Pattern pattern = Pattern.compile("Lost = ([0-9]+) \\(.*?\\)");
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1; // Return -1 if not found
    }

    public static int parseUptime(String output) {
        Pattern pattern = Pattern.compile("time=([0-9]+)ms");
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1; // Return -1 if not found
    }
}
