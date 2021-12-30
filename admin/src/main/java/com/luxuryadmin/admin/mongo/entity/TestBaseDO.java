package com.luxuryadmin.admin.mongo.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("test_base")
public class TestBaseDO {

    private String id;

    private String name;

    private Integer one;

    private Integer two;
}
