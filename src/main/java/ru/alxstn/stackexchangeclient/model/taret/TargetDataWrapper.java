package ru.alxstn.stackexchangeclient.model.taret;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.alxstn.stackexchangeclient.model.source.ResponseWrapper;

import java.util.List;
import java.util.stream.Collectors;

    /**
     *  Full answer for client:
     *  <pre>{@code
     *     {
     *       "questions": question[ ],
     *       "quota_remaining": Integer,
     *       "quota_max": Integer,
     *       "has_more": Boolean
     *     }} </pre>
     */
public class TargetDataWrapper {
    @JsonProperty("has_more")
    private final Boolean hasMore;

    @JsonProperty("quota_remaining")
    private final Integer quotaRemaining;

    @JsonProperty("quota_max")
    private final Integer quota_max;

    @JsonProperty("questions")
    private final List<TargetDataEntry> questions;

    public List<TargetDataEntry> getQuestions() {
        return questions;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public Integer getQuotaRemaining() {
        return quotaRemaining;
    }

    public Integer getQuota_max() {
        return quota_max;
    }

    public TargetDataWrapper(ResponseWrapper input) {
        this.hasMore = input.getHasMore();
        this.quota_max = input.getQuotaMax();
        this.quotaRemaining = input.getQuotaRemaining();
        this.questions = input.getItems()
                .stream()
                .map(TargetDataEntry::new)
                .collect(Collectors.toList());
    }
}
