package com.cst438.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
public class AssignmentController {

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@PutMapping("/addAssignment")
	@Transactional
	public AssignmentDTO addAssignment (@RequestBody AssignmentDTO assignmentDTO) {
		
		Assignment assignment = new Assignment();
		Course course = courseRepository.findById(assignmentDTO.courseId).orElse(null);
		
		assignment.setName(assignmentDTO.name);
		assignment.setDueDate(assignmentDTO.dueDate);
		assignment.setCourse(course);
		assignment.setNeedsGrading(1);
		
		course.getAssignments().add(assignment);
		assignmentRepository.save(assignment);
		return assignmentDTO;
	}
	
	@PutMapping("/changeAssignmentName")
	@Transactional
	public AssignmentDTO changeAssignmentName (@RequestBody AssignmentDTO assignmentDTO) {
		
		Assignment assignment = assignmentRepository.findById(assignmentDTO.assignmentId).orElse(null);
			assignment.setName(assignmentDTO.name);
			assignmentRepository.save(assignment);
			return assignment.toDTO();
	}
	
	@DeleteMapping("/deleteAssignment")
	@Transactional
	public AssignmentDTO deleteAssignment (@RequestBody AssignmentDTO assignmentDTO) {
		
		Assignment assignment = assignmentRepository.findById(assignmentDTO.assignmentId).orElse(null);
		    if (!(assignment.getNeedsGrading() == 0)) {
		        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignment has grade, cannot delete.");
		    }
		    assignmentRepository.delete(assignment);
			return assignmentDTO;
	}
}
