package com.Ucast.models;

public class SearchParameters {

    private String channelName;
    private String podcastName;

    public SearchParameters(String channelName, String podcastName){
        this.channelName = channelName;
        this.podcastName = podcastName;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getPodcastName() {
        return podcastName;
    }
}
