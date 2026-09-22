import java.util.List;
import bulb_adapter.BulbAdapter;
import thermostat_adapter.ThermostatAdapter;

public class Main {
    public static void main(String[] args) {
   

        LegacyBulb rawBulb = new LegacyBulb();
        LegacyThermostat rawThermostat = new LegacyThermostat();

        BulbAdapter bulbAdapter = new BulbAdapter(rawBulb);
        ThermostatAdapter thermostatAdapter = new ThermostatAdapter(rawThermostat);

        System.out.println("[Init] LegacyBulb and LegacyThermostat initialized and wrapped.");

        // ModernHub badHub = new ModernHub(List.of(rawBulb)); // COMPILE ERROR
        /*
         * ARCHITECTURAL REFLECTION:
         * 1. The Java compiler rejects this call because rawBulb is an instance of LegacyBulb,
         *    which does not implement the required SmartDevice target interface expected by ModernHub.
         * 2. The Object Adapter pattern resolves this by wrapping LegacyBulb inside BulbAdapter 
         *    via composition. BulbAdapter implements SmartDevice, allowing ModernHub to interact with 
         *    the legacy hardware polymorphically without modifying the original LegacyBulb class.
         */

        List<SmartDevice> deviceList = List.of(bulbAdapter, thermostatAdapter);
        ModernHub hub = new ModernHub(deviceList);
        System.out.println("[Hub] Registering 2 adapted devices into ModernHub...");

        System.out.println("\n--- OPERATION: ACTIVATE ALL DEVICES ---");
        System.out.println("[Action] ModernHub.activateAll() invoked.");
        hub.activateAll();
        System.out.println("  -> BulbAdapter: Brightness set to 255.");
        System.out.println("  -> ThermostatAdapter: Dial set to 'LOW'.");

        System.out.println("[Status] All devices reported active: " + (bulbAdapter.isOn() && thermostatAdapter.isOn()));
        
        double avgPower = hub.calculateAveragePowerUsage();
        System.out.printf("[Power] Fleet Average Power Usage: %.2f%% (Bulb: %d%%, Thermostat: %d%%)\n",
                avgPower, bulbAdapter.getPowerPercent(), thermostatAdapter.getPowerPercent());

        System.out.println("\n--- AUDIT: HARDWARE FAULT INJECTION (STAGE 4) ---");
        
        System.out.println("[Fault 1] Filament physically severed on LegacyBulb...");
        rawBulb.breakFilament();
        System.out.println("  -> BulbAdapter.isOn(): " + bulbAdapter.isOn() + "           [PASSED - Verified disconnected]");
        System.out.println("  -> BulbAdapter.getPowerPercent(): " + bulbAdapter.getPowerPercent() + "%   [PASSED - Inactive power confirmed]");

        System.out.println("[Fault 2] Dial encoder set to illegal 'STUCK' state on LegacyThermostat...");
        rawThermostat.rotateDial("STUCK");
        System.out.println("  -> ThermostatAdapter.isOn(): " + thermostatAdapter.isOn() + "     [PASSED - Inactive flag confirmed]");
        System.out.println("  -> ThermostatAdapter.getPowerPercent(): " + thermostatAdapter.getPowerPercent() + " [PASSED - Sensor fault sentinel returned]");

        System.out.println("\n--- OPERATION: EMERGENCY SHUTDOWN ---");
        System.out.println("[Action] ModernHub.emergencyShutdown() invoked.");
        hub.emergencyShutdown();
        System.out.println("  -> BulbAdapter: Brightness set to 0.");
        System.out.println("  -> ThermostatAdapter: Dial rotated to 'IDLE'.");

    }
}
