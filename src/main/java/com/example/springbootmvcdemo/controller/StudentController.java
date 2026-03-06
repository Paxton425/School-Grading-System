package com.example.springbootmvcdemo.controller;

import com.example.springbootmvcdemo.dto.*;
import com.example.springbootmvcdemo.dto.StudentDTO;
import com.example.springbootmvcdemo.model.SubjectEnrollment;
import com.example.springbootmvcdemo.model.Student;
import com.example.springbootmvcdemo.model.Subject;
import com.example.springbootmvcdemo.repository.EnrollmentRepository;
import com.example.springbootmvcdemo.repository.StudentRepository;
import com.example.springbootmvcdemo.repository.SubjectRepository;
import com.example.springbootmvcdemo.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentController implements CommandLineRunner {
    @Autowired
    ReportService reportService;
    Logger logger = LoggerFactory.getLogger(StudentController.class);

    private StudentRepository studentRepository;
    private EnrollmentRepository enrollmentRepository;
    private SubjectRepository subjectRepository;

    StudentController(StudentRepository studentRepository,
                      SubjectRepository subjectRepository,
                      EnrollmentRepository enrollmentRepository){
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students/students-list";
    }

    @GetMapping("/student/{uuid}")
    public String findStudentById(@PathVariable UUID uuid, Model model){
        Student student = studentRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Subject ID " + uuid + " not found"));

        model.addAttribute(student);
        return "students/student";
    }

    @GetMapping(path = "/search")
    @ResponseBody
    public ResponseEntity<?> getBySearchFilter(@RequestParam String search){
        try {
            String searchLower = search.toLowerCase();
            List<StudentDTO> matches = studentRepository.findAll().stream()
                    .filter(s -> {
                        String first = s.getFirstName() != null ? s.getFirstName().toLowerCase() : "";
                        String last = s.getLastName() != null ? s.getLastName().toLowerCase() : "";
                        return first.contains(searchLower) || last.contains(searchLower);
                    })
                    .map(s -> new StudentDTO(
                            s.getId(),
                            s.getFirstName(),
                            s.getLastName(),
                            s.getGender(),
                            s.getGrade()
                    ))
                    .toList();

            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            logger.error("Search error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong " + e.getMessage());
        }
    }

    @GetMapping("/grades")
    public String getGradesSummary(Model model){
        List<StudentGradeSummaryDTO> studentsSummaries = studentRepository.findAll().stream()
                .map(s -> new StudentGradeSummaryDTO(s))
                .toList();
        model.addAttribute("studentsSummaries", studentsSummaries);
        return "students/grades-summaries";
    }

    @GetMapping("/student/{id}/report")
    public String getStudentReport(@PathVariable UUID id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Generate report
        StudentReportDTO report = reportService.generateReport(student);
        model.addAttribute("report", report);

        return "students/report";
    }

    @GetMapping("/create")
    public String CreateStudentForm(Model model) {
        List<Subject> subjects = subjectRepository.findAll( );
        model.addAttribute("student", new Student());
        // Gender.values() allows us to loop through MALE/FEMALE in the dropdown
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("allSubjects", subjects);
        return "students/student-form";
    }

    @GetMapping("/edit/{uuid}")
    public String editStudentForm(@PathVariable UUID uuid, Model model) {
        Student student = studentRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        model.addAttribute("student", student); // This student has an ID and data
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("allSubjects", subjectRepository.findAll());

        return "students/student-form"; // Use the SAME file
    }

    @PostMapping("/create/save")
    public ResponseEntity<?> saveStudent(@RequestBody StudentDTO studentDTO) {
        try {
            Student student = new Student();
            student.setFirstName(studentDTO.firstName);
            student.setLastName(studentDTO.lastName);
            student.setGrade(studentDTO.grade);
            student.setGender(studentDTO.gender);
            student.setBirthDay(studentDTO.birthDay);
            Student savedStudent = studentRepository.save(student);

            if(studentDTO.enrollmentSubjects.isEmpty()){
                return ResponseEntity.ok("Student with "+studentDTO.enrollmentSubjects.size()+" enrollments Saved Successfully!");
            }
            for (SubjectDTO enrollmentSubject:
                    studentDTO.enrollmentSubjects)
            {
                logger.info("Subject: " + enrollmentSubject.name);
                Subject subject = subjectRepository.findById(enrollmentSubject.Id)
                        .orElseThrow(() -> new EntityNotFoundException("Subject ID " + enrollmentSubject.Id + " not found"));
                SubjectEnrollment enrollment = new SubjectEnrollment();
                enrollment.setStudent(savedStudent);
                enrollment.setSubject(subject);
                enrollmentRepository.save(enrollment);
            }

            return ResponseEntity.ok("Student and Subject Enrollment(s) Saved Successfully!");
        }
        catch (EntityNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.resolve(400))
                    .body(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.resolve(500))
                    .body("Something went wrong " + e.getMessage());
        }
    }

    @PutMapping("/edit/save/{uuid}")
    public ResponseEntity<?> updateStudent(@RequestBody StudentDTO studentUpdates, @PathVariable UUID uuid) {
        try {
            Student studentToUpdate = studentRepository.findById(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));

            // 1. Update Basic Info
            studentToUpdate.setFirstName(studentUpdates.firstName);
            studentToUpdate.setLastName(studentUpdates.lastName);
            studentToUpdate.setGrade(studentUpdates.grade);
            studentToUpdate.setGender(studentUpdates.gender);
            studentToUpdate.setBirthDay(studentUpdates.birthDay);

            Student updatedStudent = studentRepository.save(studentToUpdate);

            // 2. Handle Enrollments (The Sync)
            if (studentUpdates.enrollmentSubjects != null) {
                // Get current IDs to avoid duplicates
                Set<Long> currentSubjectIds = updatedStudent.getEnrollments().stream()
                        .map(e -> e.getSubject().getId())
                        .collect(Collectors.toSet());

                // FILTER & EXECUTE
                studentUpdates.enrollmentSubjects.stream()
                        .filter(dto -> !currentSubjectIds.contains(dto.Id)) // Only new ones
                        .forEach(dto -> {
                            Subject newSubject = subjectRepository.findById(dto.Id)
                                    .orElseThrow(() -> new EntityNotFoundException("Subject ID " + dto.Id + " not found"));

                            SubjectEnrollment enrollment = new SubjectEnrollment();
                            enrollment.setStudent(updatedStudent);
                            enrollment.setSubject(newSubject);
                            enrollmentRepository.save(enrollment);
                        });
            }

            return ResponseEntity.ok("Student Profile and New Enrollments Updated Successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Update failed: " + e.getMessage());
        }
    }

    @Override
    public void run(String... args) throws Exception {

    }
}