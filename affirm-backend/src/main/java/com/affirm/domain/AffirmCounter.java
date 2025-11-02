package com.affirm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("affirm_counters")
public class AffirmCounter {
    @TableId
    private Long userId;
    private Long totalCount;
    private LocalDateTime lastAffirmAt;
}





