package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.exception.NotExistUserException;
import com.onulstore.exception.UpdatePasswordException;
import com.onulstore.web.dto.MemberDto;
import com.onulstore.web.dto.PasswordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberDto.MemberResponse getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberDto.MemberResponse::of)
                .orElseThrow(() -> new NotExistUserException("로그인 유저 정보가 없습니다."));
    }

    @Transactional
    public MemberDto.MemberResponse updateProfile(MemberDto.updateRequest updateRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        Member updateMember = member.updateProfile(updateRequest);
        return MemberDto.MemberResponse.of(memberRepository.save(updateMember));
    }

    @Transactional
    public void deleteProfile() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        memberRepository.delete(member);
    }

    @Transactional
    public void updatePassword(PasswordDto passwordDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));

        if (!passwordDto.getNewPassword().equals(passwordDto.getNewPasswordConfirm())) {
            throw new UpdatePasswordException("입력한 새 비밀번호가 일치하지 않습니다.");
        }

        Member updateMember = member.updatePassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        memberRepository.save(updateMember);
    }

}
