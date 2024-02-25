package edu.java.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StackOverflowDataList(@JsonProperty("items") List<StackOverflowData> infoList) {

}
