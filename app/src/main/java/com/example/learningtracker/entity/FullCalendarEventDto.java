package com.example.learningtracker.entity;

import lombok.Data;

@Data
public class FullCalendarEventDto {
    private String id;
    private String title;
    private String start;
    private String url;

    public FullCalendarEventDto(String id, String title, String start, String url) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.url = url;
    }
	
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getStart() {
			return start;
		}
		public void setStart(String start) {
			this.start = start;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
}
