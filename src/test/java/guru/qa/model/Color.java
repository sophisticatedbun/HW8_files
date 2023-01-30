package guru.qa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Color {
    public String name;
    public Boolean isDefault;
    public String type;
    @JsonProperty("code")
    public Code code;

    public static class Code {
        public List<Integer> rgba;
        public String hex;

    }
}
