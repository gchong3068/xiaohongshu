package com.gchong3068.xiaohongshu.distributed.id.generator.biz.core;

import com.gchong3068.xiaohongshu.distributed.id.generator.biz.core.common.Result;

public interface IDGen {
    Result get(String key);
    boolean init();
}
