package com.example.learningtracker.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "records")
public class Record extends BaseEntity {
		@Id
		@Column(name = "id")
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Integer id;

		@NotNull
		@Column(name = "learning_subject_id")
		private Integer learningSubjectId;

		@NotNull
		@Column(name = "date")
		@DateTimeFormat(pattern = "yyyy/MM/dd")
		private LocalDate date;

		@Column(name = "start_time")
		@DateTimeFormat(pattern = "HH:mm:ss")
		private LocalTime startTime;

		@Column(name = "stop_time")
		@DateTimeFormat(pattern = "HH:mm:ss")
		private LocalTime stopTime;

		@Column(name = "sum_time")
		@DateTimeFormat(pattern = "HH:mm:ss")
		private LocalTime sumTime;

		@Column(name = "pomodoro")
		private Integer pomodoro;

		@Column(name = "uses_pomodoro")
		private boolean usesPomodoro;

		@Column(name = "memo")
		private String memo;

		@Column(name = "break_time")
		@DateTimeFormat(pattern = "HH:mm:ss")
		private LocalTime breakTime;

		@Column(name = "is_published")
		private boolean isPublished;

		@ManyToOne
		@JoinColumn(name = "learning_subject_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
		private LearningSubject learningSubject;

		public Integer getId() {
				return id;
		}

		public void setId(Integer id) {
				this.id = id;
		}

		public Integer getLearningSubjectId() {
				return learningSubjectId;
		}

		public void setLearningSubjectId(Integer learningSubjectId) {
			this.learningSubjectId = learningSubjectId;
		}

		public LocalDate getDate() {
				return date;
		}

		public void setDate(LocalDate date) {
				this.date = date;
		}

		public LocalTime getStartTime() {
				return startTime;
		}

		public void setStartTime(LocalTime startTime) {
				this.startTime = startTime;
		}

		public LocalTime getStopTime() {
				return stopTime;
		}

		public void setStopTime(LocalTime stopTime) {
				this.stopTime = stopTime;
		}

		public LocalTime getSumTime() {
				return sumTime;
		}

		public void setSumTime(LocalTime sumTime) {
				this.sumTime = sumTime;
		}

		public Integer getPomodoro() {
				return pomodoro;
		}

		public void setPomodoro(Integer pomodoro) {
				this.pomodoro = pomodoro;
		}

		public boolean getUsesPomodoro() {
				return usesPomodoro;
		}

		public void setUsesPomodoro(boolean usesPomodoro) {
				this.usesPomodoro = usesPomodoro;
		}

		public String getMemo() {
				return memo;
		}

		public void setMemo(String memo) {
				this.memo = memo;
		}

		public LocalTime getBreakTime() {
				return breakTime;
		}

		public void setBreakTime(LocalTime breakTime) {
				this.breakTime = breakTime;
		}

		public boolean getIsPublished() {
				return isPublished;
		}

		public void setIsPublished(boolean isPublished) {
				this.isPublished = isPublished;
		}

		public LearningSubject getLearningSubject() {
			return learningSubject;
		}

		public void setLearningSubject(LearningSubject lSubject) {
			this.learningSubject = lSubject;
		}
}