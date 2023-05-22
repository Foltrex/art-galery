package com.scnsoft.art.listener;

import com.scnsoft.art.entity.FileInfo;
import com.scnsoft.art.entity.Organization;

import javax.persistence.PrePersist;
import java.util.Date;

public class FileInfoListener {

    @PrePersist
    private void beforePersist(FileInfo organization) {
        organization.setCreatedAt(new Date());
    }

}

