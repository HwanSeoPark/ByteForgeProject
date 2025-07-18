package com.byteforge.s3.exception;

import com.byteforge.common.response.message.S3Message;

public class S3Exception extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public S3Exception(String message) {
        super(message);
    }

    public S3Exception(S3Message message) {
        super(message.getMessage());
    }

}