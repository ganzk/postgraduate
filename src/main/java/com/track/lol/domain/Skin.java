package com.track.lol.domain;

public class Skin {

    String skinId;

    String heroId;

    String heroName;

    String heroTitle;

    String name;

    String description;

    String mainImg;

    public String getSkinId() {
        return skinId;
    }

    public void setSkinId(String skinId) {
        this.skinId = skinId;
    }

    public String getHeroId() {
        return heroId;
    }

    public void setHeroId(String heroId) {
        this.heroId = heroId;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getHeroTitle() {
        return heroTitle;
    }

    public void setHeroTitle(String heroTitle) {
        this.heroTitle = heroTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    @Override
    public String toString() {
        return "Skin{" +
                "skinId='" + skinId + '\'' +
                ", heroId='" + heroId + '\'' +
                ", heroName='" + heroName + '\'' +
                ", heroTitle='" + heroTitle + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mainImg='" + mainImg + '\'' +
                '}';
    }
}
