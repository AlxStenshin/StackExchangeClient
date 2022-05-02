package ru.alxstn.stackexchangeclient.model.taret;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.alxstn.stackexchangeclient.model.source.ItemEntry;

import java.util.Date;

/**
 *  Single Item entry for result table:
 *  <pre>{@code
 *     {
 *       "date": Date,
 *       "title": String,
 *       "owner": String,
 *       "is_answered": Boolean,
 *       "link": String
 *     }} </pre>
 */

public class TargetDataEntry {

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private final Date date;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("owner")
    private final String ownerName;

    @JsonProperty("is_answered")
    private final Boolean isAnswered;

    @JsonProperty("link")
    private final String link;

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Boolean getAnswered() {
        return isAnswered;
    }

    public String getLink() {
        return link;
    }

    public TargetDataEntry(ItemEntry source) {
        this.date = new Date(source.getCreationDate()*1000);
        this.title = source.getTitle();
        this.ownerName = source.getOwner().getDisplayName();
        this.isAnswered = source.isAnswered();
        this.link = source.getLink();
    }
}
