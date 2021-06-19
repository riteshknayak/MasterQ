package com.riteshknayak.masterq.objects;

public class SliderItem {
    private String imageUrl, onClickLocation, catLocation, topicLocation; //◀ location id of the category or the topic
                                          //🔼 can be Category or Topic only

    public SliderItem(){}

    public String getCatLocation() {
        return catLocation;
    }

    public void setCatLocation(String catLocation) {
        this.catLocation = catLocation;
    }

    public String getTopicLocation() {
        return topicLocation;
    }

    public void setTopicLocation(String topicLocation) {
        this.topicLocation = topicLocation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOnClickLocation() {
        return onClickLocation;
    }

    public void setOnClickLocation(String onClickLocation) {
        this.onClickLocation = onClickLocation;
    }

}
