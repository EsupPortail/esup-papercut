package org.esupportail.papercut.config;

public class AnonymizationConfig {

    boolean enabled = false;

    long oldDaysTransactionsLogs = 1825;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getOldDaysTransactionsLogs() {
        return oldDaysTransactionsLogs;
    }

    public void setOldDaysTransactionsLogs(long oldDaysTransactionsLogs) {
        this.oldDaysTransactionsLogs = oldDaysTransactionsLogs;
    }
}
