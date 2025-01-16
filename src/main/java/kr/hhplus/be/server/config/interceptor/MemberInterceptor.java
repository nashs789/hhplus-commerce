package kr.hhplus.be.server.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.domain.member.exception.MemberException;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.Objects;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // PathVariable 값 추출
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String memberId = pathVariables.get("memberId");

        if(Objects.isNull(memberId)) {
            response.setStatus(SC_BAD_REQUEST);
            response.getWriter().write("Missing memberId in URL");
            return false;
        }

        try {
            memberService.findMemberById(Long.valueOf(memberId));

        } catch(MemberException me) {
            response.setStatus(SC_NOT_FOUND);
            response.setContentType("text/plain;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter()
                    .write(me.getMessage());

            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // TODO
    }
}
