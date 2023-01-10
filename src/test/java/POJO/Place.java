package POJO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Place {


    String placeName;
    String longitude;
    String state;
    String stateAbbreviation;
    String latitude;


    public String getPlaceName() {
        return placeName;
    }

    public String getLongitude() {
        return longitude;
    }

    @JsonProperty("place name")
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setState(String state) {
        this.state = state;
    }


    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getState() {
        return state;
    }


    @JsonProperty("state abbreviation")
    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public String getLatitude() {
        return latitude;
    }

}
