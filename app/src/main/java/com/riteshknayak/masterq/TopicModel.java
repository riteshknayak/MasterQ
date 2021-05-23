package com.riteshknayak.masterq;

public class TopicModel {
    private String topicId, topicImage, topicName;
    private boolean visibility;

    public TopicModel(String topicId, String topicImage, String topicName) {
        this.topicId = topicId;
        this.topicImage = topicImage;
        this.topicName = topicName;
    }
    public TopicModel(String topicId, String topicImage, String topicName, boolean visibility) {
        this.topicId = topicId;
        this.topicImage = topicImage;
        this.topicName = topicName;
        this.visibility = visibility;
    }

    public  TopicModel(){};

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
}