package com.ted.resonance.utils.web;

/**
 * 用于返回自定义的页面， jpa的Page很多无用信息
 * @param <T>
 */
public class MyPage<T> {
    private Integer totalPages;
    private Long totalElements;
    private Integer size;
    private Integer number;
    private T content;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
