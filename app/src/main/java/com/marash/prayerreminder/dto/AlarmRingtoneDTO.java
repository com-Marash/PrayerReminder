package com.marash.prayerreminder.dto;

public class AlarmRingtoneDTO {
    public AlarmRingtoneDTO(AlarmRingtoneType type, String ringtoneTitle, String ringtoneURI, String azanValue) {
        this.type = type;
        this.ringtoneTitle = ringtoneTitle;
        this.ringtoneURI = ringtoneURI;
        this.azanValue = azanValue;
    }
    private final AlarmRingtoneType type;
    private final String ringtoneTitle;
    private final String ringtoneURI;
    private final String azanValue;

    public AlarmRingtoneType getType() {
        return type;
    }

    public String getRingtoneTitle() {
        return ringtoneTitle;
    }

    public String getRingtoneURI() {
        return ringtoneURI;
    }

    public String getAzanValue() {
        return azanValue;
    }
}
