package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final Object detail;

    public ErrorInfo(CharSequence url, ErrorType type, Object detail) {
        this.url = url.toString();
        this.type = type;
        this.detail = detail;
    }
}