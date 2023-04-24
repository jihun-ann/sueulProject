package com.springboot.sueulproject.profile;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NaverToken {
        public String access_token;
        public String refresh_token;
        public String token_type;
        public String expires_in;

}
