package com.nighthawk.aetha_backend.dto;

@Data
public class SettingsDTO {
    private String id;

    // General settings
    private boolean allowPrivateMessages;
    private boolean hideFromAuthors;
    private boolean showBorderToUsers;
    private boolean showMobileRedirectPopup;

    // Reading settings
    private boolean enableChapterNavigation;
    private boolean neverShowReadingProgress;
    private boolean disableBacktrackingForOwnFictions;
    private boolean useOldReadingBehavior;
    private boolean enableReaderSuggestions;
    private boolean enableFullscreenMode;

    // Forum settings
    private boolean displayUserSignatures;
}
