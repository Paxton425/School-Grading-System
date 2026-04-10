package com.nexusgrade.app.controller;

import com.nexusgrade.app.dto.*;
import com.nexusgrade.app.dto.StudentDTO;
import com.nexusgrade.app.dto.StudentGradeSummaryDTO;
import com.nexusgrade.app.dto.StudentReportDTO;
import com.nexusgrade.app.model.SchoolClass;
import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.model.Subject;
import com.nexusgrade.app.repository.ClassRepository;
import com.nexusgrade.app.repository.StudentRepository;
import com.nexusgrade.app.repository.SubjectRepository;
import com.nexusgrade.app.service.ReportService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentController implements CommandLineRunner {
    @Autowired
    ReportService reportService;
    Logger logger = LoggerFactory.getLogger(StudentController.class);

    private StudentRepository studentRepository;
    private SubjectRepository subjectRepository;
    private ClassRepository classRepository;

    StudentController(StudentRepository studentRepository,
                      SubjectRepository subjectRepository,
                      ClassRepository classRepository){
        this.studentRepository = studentRepository;
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
                .map(s -> new StudentDTO(s))
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
        return "students/student-profile";
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
                    .map(s -> new StudentDTO(s))
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

        try{
            // Generate report
            StudentReportDTO report = reportService.generateReport(student);
            model.addAttribute("report", report);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return "students/report";
    }

    @GetMapping("/create")
    public String CreateStudentForm(Model model) {
        List<Subject> subjects = subjectRepository.findAll();
        Map<Integer, List<SchoolClass>> classes = classRepository.findAll().stream()
                .collect(Collectors.groupingBy(SchoolClass::getGrade));
        model.addAttribute("student", new Student());
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("classes", classes);
        model.addAttribute("statuses", Student.Status.values());
        return "students/student-form";
    }

    @GetMapping("/edit/{uuid}")
    public String editStudentForm(@PathVariable UUID uuid, Model model) {
        Student student = studentRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        Map<Integer, List<SchoolClass>> classes = classRepository.findAll().stream()
                .collect(Collectors.groupingBy(SchoolClass::getGrade));

        model.addAttribute("student", student); // This student has an ID and data
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("classes", classes);
        model.addAttribute("statuses", Student.Status.values());

        return "students/student-form"; // Use the SAME file
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("student") Student student, RedirectAttributes ra) {
        try {
            boolean isEdit = (student.getId() != null);
            studentRepository.save(student);
            String msg = isEdit ? "updated" : "enrolled";
            ra.addFlashAttribute("success", "Student " + student.getFirstName() + " " + msg + " successfully!");
            return "redirect:/students";
        } catch (Exception e) {
            logger.error("Save failed", e);
            ra.addFlashAttribute("error", "Save failed: " + e.getMessage());
            return student.getId() == null ? "redirect:/students/create" : "redirect:/students/edit/" + student.getId();
        }
    }

    @Override
    public void run(String... args) throws Exception {

    }
}