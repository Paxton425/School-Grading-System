package com.example.springbootmvcdemo.controller;

import com.example.springbootmvcdemo.dto.*;
import com.example.springbootmvcdemo.dto.StudentDTO;
import com.example.springbootmvcdemo.model.SchoolClass;
import com.example.springbootmvcdemo.model.Student;
import com.example.springbootmvcdemo.model.Subject;
import com.example.springbootmvcdemo.model.SubjectGrades;
import com.example.springbootmvcdemo.repository.ClassRepository;
import com.example.springbootmvcdemo.repository.GradesRepository;
import com.example.springbootmvcdemo.repository.StudentRepository;
import com.example.springbootmvcdemo.repository.SubjectRepository;
import com.example.springbootmvcdemo.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentController implements CommandLineRunner {
    @Autowired
    ReportService reportService;
    Logger logger = LoggerFactory.getLogger(StudentController.class);

    private StudentRepository studentRepository;
    private GradesRepository gradesRepository;
    private SubjectRepository subjectRepository;
    private ClassRepository classRepository;

    StudentController(StudentRepository studentRepository,
                      SubjectRepository subjectRepository,
                      GradesRepository gradesRepository,
                      ClassRepository classRepository){
        this.studentRepository = studentRepository;
        this.gradesRepository = gradesRepository;
        this.subjectRepository = subjectRepository;
        this.classRepository = classRepository;
    }

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students/students-list";
    }

    @GetMapping("/data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentsJson(
            @RequestParam(defaultValue = "1") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(value = "search[value]", required = false) String searchValue) {

        // 1. Create Pageable (DataTables 'start' is index, not page number)
        Pageable pageable = PageRequest.of(start / length, length, Sort.by("lastName").ascending());

        // 2. Fetch Filtered Data
        Page<Student> page;
        if (searchValue != null && !searchValue.isEmpty()) {
            page = studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchValue, searchValue, pageable);
        } else {
            page = studentRepository.findAll(pageable);
        }

        // 3. Convert Entities to DTOs (The "Shield")
        List<StudentDTO> studentDTOS = page.getContent().stream()
                .map(s -> new StudentDTO(
                        s.getId(),
                        s.getFirstName(),
                        s.getLastName(),
                        s.getGender(),
                        s.getBirthDay(),
                        s.getSchoolClass()
                ))
                .toList();

        // 3. Return DataTables specific JSON structure
        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", studentRepository.count());
        response.put("recordsFiltered", page.getTotalElements());
        response.put("data", studentDTOS);

        return ResponseEntity.ok(response);
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
                            s.getBirthDay(),
                            s.getSchoolClass()
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

    @GetMapping("/grades/report/{studentId}")
    public String getStudentReport(@PathVariable UUID studentId, Model model) {
        Student student = studentRepository.findById(studentId)
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
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("classes", classRepository.findAll());
        model.addAttribute("allSubjects", subjects);
        return "students/student-form";
    }

    @GetMapping("/edit/{uuid}")
    public String editStudentForm(@PathVariable UUID uuid, Model model) {
        Student student = studentRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        model.addAttribute("student", student); // This student has an ID and data
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("classes", classRepository.findAll());
        model.addAttribute("allSubjects", subjectRepository.findAll());

        return "students/student-form"; // Use the SAME file
    }

    @PostMapping("/create/save")
    public ResponseEntity<?> saveStudent(@RequestBody StudentDTO studentDTO) {
        try {
            Long classId = studentDTO.getSchoolClassDTO().getId();
            Student student = new Student();
            student.setFirstName(studentDTO.getFirstName());
            student.setLastName(studentDTO.getLastName());
            student.setGender(studentDTO.getGender());
            student.setBirthDay(studentDTO.getBirthDay());
            if(classId != null){ //Assigning class during registration is optional
                SchoolClass schoolClass = classRepository.findById(classId)
                        .orElseThrow(() -> new RuntimeException("Class with ID: "+classId+"Not found"));
                student.setSchoolClass(schoolClass);
            }
            Student savedStudent = studentRepository.save(student);

            if(studentDTO.getSubjects().isEmpty()){
                return ResponseEntity.ok("Student with "+studentDTO.getSubjects().size()+" enrollments Saved Successfully!");
            }
            for (SubjectDTO enrollmentSubject:
                    studentDTO.getSubjects())
            {
                logger.info("Subject: " + enrollmentSubject.name);
                Subject subject = subjectRepository.findById(enrollmentSubject.Id)
                        .orElseThrow(() -> new EntityNotFoundException("Subject ID " + enrollmentSubject.Id + " not found"));
                SubjectGrades subjectGrades = new SubjectGrades();
                subjectGrades.setStudent(savedStudent);
                subjectGrades.setSubject(subject);
                gradesRepository.save(subjectGrades);
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
            Long classId = studentUpdates.getSchoolClassDTO().getId();

            // 1. Update Basic Info
            studentToUpdate.setFirstName(studentUpdates.getFirstName());
            studentToUpdate.setLastName(studentUpdates.getLastName());
            if(classId != null){ //Assigning class during registration is optional
                SchoolClass schoolClass = classRepository.findById(classId)
                        .orElseThrow(() -> new RuntimeException("Class with ID: "+classId+"Not found"));
                studentToUpdate.setSchoolClass(schoolClass);
            }
            studentToUpdate.setGender(studentUpdates.getGender());
            studentToUpdate.setBirthDay(studentUpdates.getBirthDay());

            Student updatedStudent = studentRepository.save(studentToUpdate);

            // 2. Handle Enrollments (The Sync)
            if (studentUpdates.getSubjects() != null) {
                // Get current IDs to avoid duplicates
                Set<Long> currentSubjectIds = updatedStudent.getSubjectGrades().stream()
                        .map(e -> e.getSubject().getId())
                        .collect(Collectors.toSet());

                // FILTER & EXECUTE
                studentUpdates.getSubjects().stream()
                        .filter(dto -> !currentSubjectIds.contains(dto.Id)) // Only new ones
                        .forEach(dto -> {
                            Subject newSubject = subjectRepository.findById(dto.Id)
                                    .orElseThrow(() -> new EntityNotFoundException("Subject ID " + dto.Id + " not found"));

                            SubjectGrades subjectGrades = new SubjectGrades();
                            subjectGrades.setStudent(updatedStudent);
                            subjectGrades.setSubject(newSubject);
                            gradesRepository.save(subjectGrades);
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