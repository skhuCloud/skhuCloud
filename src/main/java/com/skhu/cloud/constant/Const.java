package com.skhu.cloud.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Const {
    public static final Long INIT_PAGE_NUMBER = 0L;
    public static final Long PAGE_SIZE = 10L;
    public static final Long DEPTH_LIMIT = 8L; // Depth limit 은 8 이 적당하다, 그래야지 되게 로우 레벨한 곳에서 진행해도 된다.
    public static final String ACCESS_PROHIBIT_FILE = "Containers";
}
