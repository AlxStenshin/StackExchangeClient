package ru.alxstn.stackexchangeclient.model.source;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *  Single Item entry from /search response:
 *  <pre>{@code
 *     {
 *       "tags": String[ ],
 *       "owner": owner,
 *       "is_answered": Boolean,
 *       "view_count": Integer,
 *       "answer_count": Integer,
 *       "score": Integer,
 *       "last_activity_date": Long,
 *       "creation_date": Long,
 *       "last_edit_date": Long,
 *       "question_id": Integer,
 *       "content_license": "String,
 *       "link": String,
 *       "title": String
 *     }} </pre>
 */

public class ItemEntry {

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("owner")
    private OwnerEntry owner;

    @JsonProperty("is_answered")
    private Boolean isAnswered;

    @JsonProperty("view_count")
    private Integer viewCount;

    @JsonProperty("answer_count")
    private Integer answerCount;

    @JsonProperty("score")
    private Integer score;

    @JsonProperty("creation_date")
    private Long creationDate;

    @JsonProperty("last_edit_date")
    private Long lastEditDate;

    @JsonProperty("question_id")
    private Integer questionId;

    @JsonProperty("content_license")
    private String contentLicense;

    @JsonProperty("link")
    private String link;

    @JsonProperty("title")
    private String title;

    public OwnerEntry getOwner() {
        return owner;
    }

    public Boolean isAnswered() {
        return isAnswered;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }
}
