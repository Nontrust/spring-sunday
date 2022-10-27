package com.study.yongyeon.sunday.spring.springsunday.security.service;

import com.study.yongyeon.sunday.spring.springsunday.dto.ClubAuthMemberDTO;
import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMember;
import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMemberRole;
import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMemberSocial;
import com.study.yongyeon.sunday.spring.springsunday.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubOauth2UserDetailsService extends DefaultOAuth2UserService {
    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest){
        log.info("loadUser : ----------------------------\n{}",userRequest); //OAuthUserRequest객체;

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName : {}",clientName); //google
        log.info("getAdditionalParameters : {}",userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("============================");
        oAuth2User.getAttributes().forEach((k,v)->log.info(k + ":" +v));    //user정보

        String email = new String();
        String name = new String();
        ClubMemberSocial fromMemberSocial = null;

        if("Google".equals(clientName)){
            name = oAuth2User.getAttribute("email");
            email = oAuth2User.getAttribute("email");
            fromMemberSocial = ClubMemberSocial.GOOGLE;
        }

        else if("Naver".equals(clientName)){
            Map NaverResponse = (Map<String, Object>) oAuth2User.getAttribute("response");
            email = (String) NaverResponse.get("email");
            name = (String) NaverResponse.get("name");

            fromMemberSocial = ClubMemberSocial.NAVER;
        }

        log.info("EMAIL : "+email);

        ClubMember clubMember = saveSocialMember(email, fromMemberSocial);
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getName()
                ,clubMember.getPassword()
                ,fromMemberSocial
                ,clubMember.getRoleSet().stream()
                .map((role)->
                    new SimpleGrantedAuthority(role.name())
                ).collect(Collectors.toList())
                , oAuth2User.getAttributes()
        );
        clubAuthMember.setName(clubMember.getName());
        return clubAuthMember;
    }

    private ClubMember saveSocialMember(String email, ClubMemberSocial clientName) {
        Optional<ClubMember> findBySocialMember = clubMemberRepository.findByEmail(email,clientName);
        //DB에 저장 이력이 있다면 조회된 결과를 찾음
        if(findBySocialMember.isPresent()){
            return findBySocialMember.get();
        }

        //아니라면 해당 결과값 저장
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(clientName)
                .build();
        clubMember.addMemberRole(ClubMemberRole.USER);
        clubMemberRepository.save(clubMember);

        return clubMember;
    }
}
