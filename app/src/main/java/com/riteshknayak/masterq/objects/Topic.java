package com.riteshknayak.masterq.objects;

public class Topic {
    private String topicId, topicImage, topicName, topicMainImage, topicDescription ;
    private int players, reviewNumber;
    private boolean visibility;
    private boolean free;

    public void Topic() {}

    public Topic(String topicId, String topicImage, String topicName, String topicMainImage, String topicDescription, int players, int reviewNumber, boolean visibility, boolean free) {
        this.topicId = topicId;
        this.topicImage = topicImage;
        this.topicName = topicName;
        this.topicMainImage = topicMainImage;
        this.topicDescription = topicDescription;
        this.players = players;
        this.reviewNumber = reviewNumber;
        this.visibility = visibility;
        this.free = free;
    }

    public Topic(){};

    public String getTopicId() {
        return topicId;
    }


    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicImage() {
        return topicImage;
    }

    public void setTopicImage(String topicImage) {
        this.topicImage = topicImage;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public String getTopicMainImage() {
        return topicMainImage;
    }

    public void setTopicMainImage(String topicMainImage) {
        this.topicMainImage = topicMainImage;
    }

    public int getReviewNumber() {
        return reviewNumber;
    }

    public void setReviewNumber(int reviewNumber) {
        this.reviewNumber = reviewNumber;
    }
}
