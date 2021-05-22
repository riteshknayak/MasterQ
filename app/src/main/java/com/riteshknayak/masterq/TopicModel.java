package com.riteshknayak.masterq;

public class TopicModel {
    private String topicId, topicImage, topicName;

    public TopicModel(String topicId, String topicImage, String topicName) {
        this.topicId = topicId;
        this.topicImage = topicImage;
        this.topicName = topicName;
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
}
