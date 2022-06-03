package org.jeecg.modules.prom.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "client_log")
public class ClientLog{
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    Integer id; //id

    @Column(name = "client_id")
    String ClientId;

    @Column(name = "content")
    String Content;

    @Column(name = "create_time")
    Date CreateTime;

    @Column(name = "level")
    String Level;
}

