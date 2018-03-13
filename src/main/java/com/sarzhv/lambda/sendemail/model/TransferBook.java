package com.sarzhv.lambda.sendemail.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class TransferBook {

    @Getter
    @Setter
    private String bookId;

    @Getter
    @Setter
    private String bookName;

    @Getter
    @Setter
    private List<Map<String, String>> chapters;

}
