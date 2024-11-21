package com.nighthawk.aetha_backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Document("profile_settings")
public class Settings {
    @Id
    private String id;

    @DocumentReference(collection = "users")
    private AuthUser user;

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