package ru.alxstn.stackexchangeclient.model.source;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Full /search response:
 * <pre>{@code
 *  {
 *      "items": item[ ],
 *      "has_more": Boolean,
 *      "quota_max": Integer,
 *      "quota_remaining": Integer,
 *  }}</pre>
 */

public class ResponseWrapper {

    @JsonProperty("items")
    private List<ItemEntry> items;

    @JsonProperty("has_more")
    private Boolean hasMore;

    @JsonProperty("quota_max")
    private Integer quotaMax;

    @JsonProperty("quota_remaining")
    private Integer quotaRemaining;

    @JsonProperty("items")
    public List<ItemEntry> getItems() {
        return items;
    }

    @JsonProperty("has_more")
    public Boolean getHasMore() {
        return hasMore;
    }

    public Integer getQuotaMax() {
        return quotaMax;
    }

    public Integer getQuotaRemaining() {
        return quotaRemaining;
    }

}

