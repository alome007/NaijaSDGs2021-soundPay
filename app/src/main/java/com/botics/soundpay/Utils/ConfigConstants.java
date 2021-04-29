package com.botics.soundpay.Utils;

public class ConfigConstants {

    public static final String FREQUENCY_ZERO = "etprefFrequencyZero";
    public static final String BIT_PERIOD = "etprefBitperiod";
    public static final String PAUSE_PERIOD = "etprefPauseperiod";
    public static final String NUMBER_OF_FREQUENCIES = "lpprefNFrequencies";
    public static final String SPACE_BETWEEN_FREQUENCIES = "etprefFrequencyspace";
    public static final String NUMBER_OF_BYTES = "etprefNMaxBytes";
    public static final String LOUDNESS = "sbprefLoudness";
    public static final String PRESET = "setPresetPreferences";

    public static final String SETTING_FREQUENCY_ZERO_DEFAULT = "18000";
    public static final String SETTING_BIT_PERIOD_DEFAULT = "100";
    public static final String SETTING_PAUSE_PERIOD_DEFAULT = "0";
    public static final String SETTING_NUMBER_OF_FREQUENCIES_DEFAULT = "16";
    public static final String SETTING_SPACE_BETWEEN_FREQUENCIES_DEFAULT = "100";
    public static final String SETTING_NUMBER_OF_BYTES_DEFAULT = "18";
    public static final String SETTING_LOUDNESS_DEFAULT = "70";


    public static final int SETTING_BIT_PERIOD_LOWER_LIMIT = 30;
    public static final int SETTING_BIT_PERIOD_UPPER_LIMIT = 200;
    public static final int SETTING_PAUSE_PERIOD_LOWER_LIMIT = 0;
    public static final int SETTING_PAUSE_PERIOD_UPPER_LIMIT = 200;
    public static final int SETTING_FREQUENCY_ZERO_LOWER_LIMIT = 50;
    public static final int SETTING_FREQUENCY_ZERO_UPPER_LIMIT = 20000;
    public static final int SETTING_NUMBER_OF_BYTES_LOWER_LIMIT = 2;
    public static final int SETTING_NUMBER_OF_BYTES_UPPER_LIMIT = 30;
    public static final int SETTING_SPACE_BETWEEN_FREQUENCIES_LOWER_LIMIT = 50;
    public static final int SETTING_SPACE_BETWEEN_FREQUENCIES_UPPER_LIMIT = 200;
    public static final int SETTING_LOUDNESS_LOWER_LIMIT = 1;
    public static final int SETTING_LOUDNESS_UPPER_LIMIT = 100;

    public static final String PREFERENCE_RESET_PREFERENCES = "resetPreferences";
    public static final String PREFERENCE_PRESET_PREFERENCES = "setPresetPreferences";

    public static final String CURRENT_VOLUME = "current-volume";
    public static final int CURRENT_VOLUME_DEFAULT = 70;

    public static final String SEND_BUTTON_ENABLED = "send-button-enabled";
    public static final String LISTEN_BUTTON_ENABLED = "listen-button-enabled";
    public static final String STOP_LISTEN_BUTTON_ENABLED = "stop-listen-button-enabled";

    public static final String TEXT_TO_SEND = "text-to-send";
}
