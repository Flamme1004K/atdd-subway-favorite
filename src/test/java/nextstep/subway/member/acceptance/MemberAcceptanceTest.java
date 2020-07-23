package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> readResponse = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(readResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        회원_삭제됨(response);
    }

    @DisplayName("내 회원 정보 관련 기능 테스트")
    @Nested
    public class MemberMeAcceptanceTest extends AcceptanceTest {

        private TokenResponse tokenResponse;

        @BeforeEach
        public void setUp() {
            super.setUp();

            // given
            회원_등록되어_있음(EMAIL, PASSWORD, AGE);
            tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
        }

        @DisplayName("내 회원 정보를 수정한다")
        @Test
        void updateMemberMe() {
            // when
            ExtractableResponse<Response> response = 내_회원_정보_수정_요청(tokenResponse, "new" + EMAIL, "new" + PASSWORD, AGE + 1);

            // then
            회원_정보_수정됨(response);
        }

        @DisplayName("내 회원 정보를 삭제한다")
        @Test
        void deleteMemberMe() {
            // when
            ExtractableResponse<Response> response = 내_회원_정보_삭제_요청(tokenResponse);

            // then
            회원_삭제됨(response);
        }
    }
}
