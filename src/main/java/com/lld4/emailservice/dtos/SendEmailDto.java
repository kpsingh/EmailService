package com.lld4.emailservice.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendEmailDto {
    private String to;
    private String subject;
    private String body;
    private String from;


}
