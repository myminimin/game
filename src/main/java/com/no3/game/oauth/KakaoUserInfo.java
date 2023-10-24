package com.no3.game.oauth;

import lombok.extern.log4j.Log4j2;

import java.util.Map;
@Log4j2
public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes; // getAttributes
    private Map<String, Object> attributesProperties; // getAttributes
    private Map<String, Object> attributesAccount; // getAttributes
    private Map<String, Object> attributesProfile; // getAttributes

    public KakaoUserInfo(Map<String,Object> attributes){
        this.attributes = attributes;
        log.info("Kakao User Info Attributes: {}", attributes);
        this.attributesProperties = (Map<String, Object>) attributes.get("properties");
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");
    }


    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }



    @Override
    public String getEmail() {
        return (String) attributesAccount.get("email");
    }

    @Override
    public String getName() {
        return (String) attributesProperties.get("nickname");
    }
}
