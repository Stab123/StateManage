package org.jeecg.modules.prom.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "log")
public class Log{
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    Integer id; //id

    @Column(name = "app_id")
    String AppId;

    @Column(name = "service_id")
    String ServiceId;

    @Column(name = "service_name")
    String ServiceName;

    @Column(name = "content")
    String Content;

    @Column(name = "create_time")
    Date CreateTime;

    @Column(name = "level")
    String Level;
}

