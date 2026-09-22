package bulb_adapter;

import SmartDevice;
import LegacyBulb;

public class BulbAdapter implements SmartDevice {
    private final LegacyBulb bulb;
    // Student ID-іңіздің соңғы цифрын K орнына қойыңыз (Мысалы: ID соңы 4 болса, K = 4)
    private final int K = 4; 

    public BulbAdapter(LegacyBulb bulb) {
        if (bulb == null) {
            throw new IllegalArgumentException("LegacyBulb instances cannot be null");
        }
        this.bulb = bulb;
    }

    @Override
    public void turnOn() {
        bulb.setBrightness(255);
    }

    @Override
    public void turnOff() {
        bulb.setBrightness(0);
    }

    @Override
    public boolean isOn() {
        if (!bulb.hasPower()) {
            return false;
        }
        return bulb.readBrightness() > 0;
    }

    @Override
    public int getPowerPercent() {
        if (!bulb.hasPower()) {
            return 0;
        }

        int rawBrightness = bulb.readBrightness();
        if (rawBrightness == 0) {
            return 0;
        }

        int rawPercent = (int) Math.floor((rawBrightness * 100.0) / 255.0);
        int calibratedPercent = rawPercent + K;

        return Math.min(100, calibratedPercent);
    }
}
