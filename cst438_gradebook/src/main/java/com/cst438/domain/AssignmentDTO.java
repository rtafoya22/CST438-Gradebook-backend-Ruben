package com.cst438.domain;

import java.sql.Date;

public class AssignmentDTO {
	
	public int assignmentId;
	public int courseId;
	public String name;
	public Date dueDate;
	public int needsGrading;
	
	public AssignmentDTO(int assignmentId, int courseId, String name, Date dueDate, int needsGrading) {
		super();
		this.assignmentId = assignmentId;
		this.courseId = courseId;
		this.name = name;
		this.dueDate = dueDate;
		this.needsGrading = needsGrading;
	}

	public AssignmentDTO() {
		super();
	}

	public int getAssignmentID() {
		return assignmentId;
	}

	public void setAssignmentID(int assignmentID) {
		this.assignmentId = assignmentID;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getassignmentName() {
		return name;
	}

	public void setName(String assignmentName) {
		this.name = assignmentName;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public int getNeedsGrading() {
		return needsGrading;
	}
	public void setNeedsGrading(int needsGrading) {
		this.needsGrading = needsGrading;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssignmentDTO other = (AssignmentDTO) obj;
		if (assignmentId != other.assignmentId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (courseId != other.courseId)
			return false;
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		return true;
	}

}
