package kr.hhplus.be.server.api.member.request;

public record PointRequest(Long userId, Long requestTime) {

    public PointRequest(Long userId) {
        this(userId, System.currentTimeMillis());
    }
}
