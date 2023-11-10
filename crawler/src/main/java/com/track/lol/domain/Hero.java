package com.track.lol.domain;

import java.util.List;

public class Hero {

    String heroId;

    String name;

    String alias;

    String title;

    String changeLabel;

    List<Skin> skins;

    public List<Skin> getSkins() {
        return skins;
    }

    public void setSkins(List<Skin> skins) {
        this.skins = skins;
    }

    public String getHeroId() {
        return heroId;
    }

    public void setHeroId(String heroId) {
        this.heroId = heroId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChangeLabel() {
        return changeLabel;
    }

    public void setChangeLabel(String changeLabel) {
        this.changeLabel = changeLabel;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "heroId='" + heroId + '\'' +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", title='" + title + '\'' +
                ", changeLabel='" + changeLabel + '\'' +
                ", skins=" + skins +
                '}';
    }
}
