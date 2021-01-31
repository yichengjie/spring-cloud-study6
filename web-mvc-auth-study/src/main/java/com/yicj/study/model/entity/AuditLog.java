package com.yicj.study.model.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String method ;
    private String path ;
    private Integer status ;
    private String username ;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime ;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime ;
}
