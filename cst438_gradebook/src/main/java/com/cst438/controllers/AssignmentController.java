package com.cst438.controllers;

//import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@CrossOrigin(origins = {"http://localhost:3000"})
public class AssignmentController {

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@PostMapping("/assignment")
	@Transactional
	public AssignmentDTO newAssignment(@RequestBody AssignmentDTO dto) {
		String userEmail = "dwisneski@csumb.edu";
		// validate course and that the course instructor is the user
		Course c = courseRepository.findById(dto.courseId).orElse(null);
		if (c != null && c.getInstructor().equals(userEmail)) {
			// create and save new assignment
			// update and return dto with new assignment primary key
			Assignment a = new Assignment();
			a.setCourse(c);
			a.setName(dto.name);
//			a.setDueDate(Date.valueOf(dto.dueDate));
			a.setDueDate(dto.dueDate);
			a.setNeedsGrading(1);
			a = assignmentRepository.save(a);
			dto.assignmentId=a.getId();
			return dto;
			
		} else {
			// invalid course
			throw new ResponseStatusException( 
                           HttpStatus.BAD_REQUEST, 
                          "Invalid course id.");
		}
	}

	
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
