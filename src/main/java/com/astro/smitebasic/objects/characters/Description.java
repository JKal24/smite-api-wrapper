package com.astro.smitebasic.objects.characters;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Description {

    @JsonProperty("Description")
    Ability ability;

    @JsonProperty("Id")
    private Integer id;

    @JsonProperty("Summary")
    private String summary;

    @JsonProperty("URL")
    private String URL;

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "Description{" +
                "ability=" + ability +
                ", id=" + id +
                ", summary='" + summary + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}