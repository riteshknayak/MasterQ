package com.riteshknayak.masterq.topics;

public class Topic {
    private String topicId, topicImage, topicName;
    private boolean visibility;

    public Topic(String topicId, String topicImage, String topicName) {
        this.topicId = topicId;
        this.topicImage = topicImage;
        this.topicName = topicName;
    }
    public Topic(String topicId, String topicImage, String topicName, boolean visibility) {
        this.topicId = topicId;
        this.topicImage = topicImage;
        this.topicName = topicName;
        this.visibility = visibility;
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

    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
