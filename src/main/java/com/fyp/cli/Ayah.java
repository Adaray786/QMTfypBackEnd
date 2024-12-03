package com.fyp.cli;

public class Ayah {
    private int ayahId; // AyahID field
    private int surahId; // SurahID field (Foreign Key)
    private int ayahNumber; // Ayah_Number field
    private String arabicText; // Arabic_Text field
    private int wordCount; // Word_Count field

    // Constructor
    public Ayah(int ayahId, int surahId, int ayahNumber, String arabicText, int wordCount) {
        this.ayahId = ayahId;
        this.surahId = surahId;
        this.ayahNumber = ayahNumber;
        this.arabicText = arabicText;
        this.wordCount = wordCount;
    }

    // Default Constructor
    public Ayah() {
    }

    // Getters and Setters
    public int getAyahId() {
        return ayahId;
    }

    public void setAyahId(int ayahId) {
        this.ayahId = ayahId;
    }

    public int getSurahId() {
        return surahId;
    }

    public void setSurahId(int surahId) {
        this.surahId = surahId;
    }

    public int getAyahNumber() {
        return ayahNumber;
    }

    public void setAyahNumber(int ayahNumber) {
        this.ayahNumber = ayahNumber;
    }

    public String getArabicText() {
        return arabicText;
    }

    public void setArabicText(String arabicText) {
        this.arabicText = arabicText;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    // toString Method for easy debugging and display
    @Override
    public String toString() {
        return "Ayah{" +
                "ayahId=" + ayahId +
                ", surahId=" + surahId +
                ", ayahNumber=" + ayahNumber +
                ", arabicText='" + arabicText + '\'' +
                ", wordCount=" + wordCount +
                '}';
    }
}

