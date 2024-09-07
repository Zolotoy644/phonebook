package com.zolotarev.phonebook.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetails {
    private String message;
    private String details;
    private LocalDateTime timestamp;

    //if missing builder annotation
    /*public static class ErrorDetailsBuilder {
        private String message;
        private String details;
        private LocalDateTime timestamp;

        public ErrorDetailsBuilder() {}

        public ErrorDetailsBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorDetailsBuilder details(String details) {
            this.details = details;
            return this;
        }

        public ErrorDetailsBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ErrorDetails build() {
            return new ErrorDetails(message, details, timestamp);
        }
    }*/
}
