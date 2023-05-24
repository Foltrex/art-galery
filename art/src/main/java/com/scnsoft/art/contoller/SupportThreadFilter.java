package com.scnsoft.art.contoller;

import com.scnsoft.art.entity.SupportThread;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SupportThreadFilter {
    private UUID accountId;
    private SupportThread.SupportThreadStatus status;
}
