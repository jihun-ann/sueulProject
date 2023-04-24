package com.springboot.sueulproject.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUser {

        public String resultcode;
        public String message;
        public Response response;

        @Getter
        @Setter
        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Response {

        public String id;
        public String nickname;
        public String mobile;
        public String mobileE164;
        public String name;

    }
}
