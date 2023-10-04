package no3.game.service;

import no3.game.dto.MemberJoinDto;

public interface MemberService {
    static class MidExistException extends Exception {

    }

    void join(MemberJoinDto memberJoinDTO) throws MidExistException ;

}
