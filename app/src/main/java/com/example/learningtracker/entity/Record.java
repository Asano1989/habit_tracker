package com.example.learningtracker.entity;

import java.sql.Time;
import java.util.Date;

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
		private Date date;

		@Column(name = "start_time")
		@DateTimeFormat(pattern = "HH:mm:ss")
		private Time startTime;

		@Column(name = "stop_time")
		@DateTimeFormat(pattern = "HH:mm:ss")
		private Time stopTime;

		@Column(name = "sum_time")
		@DateTimeFormat(pattern = "HH:mm:ss")
		private Time sumTime;

		@Column(name = "pomodoro")
		private Integer pomodoro;

		@Column(name = "uses_pomodoro")
		private boolean usesPomodoro;

		@Column(name = "memo")
		private String memo;

		@Column(name = "break_time")
		@DateTimeFormat(pattern = "HH:mm:ss")
		private Time breakTime;

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

		public Date getDate() {
				return date;
		}

		public void setDate(Date date) {
				this.date = date;
		}

		public Time getStartTime() {
				return startTime;
		}

		public void setStartTime(Time startTime) {
				this.startTime = startTime;
		}

		public Time getStopTime() {
				return stopTime;
		}

		public void setStopTime(Time stopTime) {
				this.stopTime = stopTime;
		}

		public Time getSumTime() {
				return sumTime;
		}

		public void setSumTime(Time sumTime) {
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

		public Time getBreakTime() {
				return breakTime;
		}

		public void setBreakTime(Time breakTime) {
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