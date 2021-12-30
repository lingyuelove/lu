package com.luxuryadmin.admin.mongo.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("runoob")
public class RunoobDO {

    private String id;

    private String name;
}
