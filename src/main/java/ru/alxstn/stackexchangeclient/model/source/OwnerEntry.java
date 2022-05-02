package ru.alxstn.stackexchangeclient.model.source;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Single Owner entry inside ItemEntry from /search response:
 * <pre>{@code
 * "owner": {
 *         "account_id": Integer,
 *         "reputation": Integer,
 *         "user_id": Integer,
 *         "user_type": String,
 *         "profile_image": String,
 *         "display_name": String,
 *         "link": String
 *       }</pre>
 **/

public class OwnerEntry {

    @JsonProperty("account_id")
    private Integer accountId;

    @JsonProperty("reputation")
    private Integer reputation;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("user_type")
    private String userType;

    @JsonProperty("profile_image")
    private String profileImage;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("link")
    private String link;

    public String getDisplayName() {
        return displayName;
    }
}
