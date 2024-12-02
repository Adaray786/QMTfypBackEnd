package com.fyp.cli;

public class Surah {
    private int surahId; // SurahID field
    private String surahNameArabic; // Surah_Name_Arabic field
    private String surahNameEnglish; // Surah_Name_English field
    private int totalAyahs; // Total_Ayahs field

    // Constructor
    public Surah(int surahId, String surahNameArabic, String surahNameEnglish, int totalAyahs) {
        this.surahId = surahId;
        this.surahNameArabic = surahNameArabic;
        this.surahNameEnglish = surahNameEnglish;
        this.totalAyahs = totalAyahs;
    }

    // Default Constructor
    public Surah() {
    }

    // Getters and Setters
    public int getSurahId() {
        return surahId;
    }

    public void setSurahId(int surahId) {
        this.surahId = surahId;
    }

    public String getSurahNameArabic() {
        return surahNameArabic;
    }

    public void setSurahNameArabic(String surahNameArabic) {
        this.surahNameArabic = surahNameArabic;
    }

    public String getSurahNameEnglish() {
        return surahNameEnglish;
    }

    public void setSurahNameEnglish(String surahNameEnglish) {
        this.surahNameEnglish = surahNameEnglish;
    }

    public int getTotalAyahs() {
        return totalAyahs;
    }

    public void setTotalAyahs(int totalAyahs) {
        this.totalAyahs = totalAyahs;
    }

    // toString Method for easy debugging and display
    @Override
    public String toString() {
        return "Surah{" +
                "surahId=" + surahId +
                ", surahNameArabic='" + surahNameArabic + '\'' +
                ", surahNameEnglish='" + surahNameEnglish + '\'' +
                ", totalAyahs=" + totalAyahs +
                '}';
    }
}
