package com.example.interestrateapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "interest-rate")
public class InterestRateConfig {

    private double baseRate = 1.5;
    private IncomeThresholds incomeThresholds = new IncomeThresholds();

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public IncomeThresholds getIncomeThresholds() {
        return incomeThresholds;
    }

    public void setIncomeThresholds(IncomeThresholds incomeThresholds) {
        this.incomeThresholds = incomeThresholds;
    }

    public static class IncomeThresholds {
        private double low = 2000.0;
        private double medium = 4000.0;
        private double high = 8000.0;
        private double lowModifier = 0.3;
        private double mediumModifier = 0.0;
        private double highModifier = -0.1;
        private double veryHighModifier = -0.2;

        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        public double getMedium() {
            return medium;
        }

        public void setMedium(double medium) {
            this.medium = medium;
        }

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public double getLowModifier() {
            return lowModifier;
        }

        public void setLowModifier(double lowModifier) {
            this.lowModifier = lowModifier;
        }

        public double getMediumModifier() {
            return mediumModifier;
        }

        public void setMediumModifier(double mediumModifier) {
            this.mediumModifier = mediumModifier;
        }

        public double getHighModifier() {
            return highModifier;
        }

        public void setHighModifier(double highModifier) {
            this.highModifier = highModifier;
        }

        public double getVeryHighModifier() {
            return veryHighModifier;
        }

        public void setVeryHighModifier(double veryHighModifier) {
            this.veryHighModifier = veryHighModifier;
        }
    }
}