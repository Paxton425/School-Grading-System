package com.nexusgrade.app.controller;

import com.nexusgrade.app.dto.SubmissionStats;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.repository.AssessmentRepository;
import com.nexusgrade.app.repository.ResultRepository;
import com.nexusgrade.app.repository.StudentRepository;
import com.nexusgrade.app.repository.SubjectRepository;
import com.nexusgrade.app.service.AssessmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/assessments", method = RequestMethod.GET)
public class AssessmentsController {
    @Autowired
    AssessmentService assessmentService;

    private AssessmentRepository assessmentRepository;
    private SubjectRepository subjectRepository;
    private ResultRepository resultRepository;
    private StudentRepository studentRepository;
    AssessmentsController(AssessmentRepository assessmentRepository,
                          SubjectRepository subjectRepository,
                          ResultRepository resultRepository,
                          StudentRepository studentRepository){
        this.assessmentRepository = assessmentRepository;
        this.subjectRepository = subjectRepository;
        this.resultRepository = resultRepository;
        this.subjectRepository = subjectRepository;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String GetAssessments(Model model){
        List<Assessment> assessments = assessmentRepository.findAll();
        model.addAttribute("assessments", assessments);

        return "assessments/assessments";
    }

    @GetMapping("/assessment/{id}")
    public String getaAssessment(@PathVariable(value = "id") Long id, Model model){
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment with ID " + id + " not found"));

        AssessmentService.TimeProgess timeProgress = assessmentService
                .calculateTimeProgress(assessment.getAssignmentIssueDate(),
                        assessment.getAssignmentDeadline());

        model.addAttribute("timeProgress", timeProgress.progress);
        model.addAttribute("statusText", timeProgress.statusText);
        model.addAttribute("isOverdue", timeProgress.daysLeft < 0);
        model.addAttribute("assessment", assessment);

        return "assessments/assessment";
    }

    @GetMapping("/create")
    public String createAssessment(Model model){
        List<Subject> subjects = subjectRepository.findAll();
        model.addAttribute("assessment", new Assessment());
        model.addAttribute("subjects", subjects);
        return "assessments/assessment-form";
    }

    @GetMapping("/edit/{id}")
    public String createAssessment(@PathVariable Long id, Model model){
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment with ID " + id + " not found"));
        List<Subject> subjects = subjectRepository.findAll();

        model.addAttribute("assessment", assessment);
        model.addAttribute("subjects", subjects);
        return "assessments/assessment-form";
    }

    @GetMapping("/submissions/{assessmentId}")
    public String viewSubmissions(@PathVariable Long assessmentId, Model model) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));

        List<Result> submissions = resultRepository.findByAssessment(assessment);
        List<SchoolClass> assignedClasses = assessment.getSchoolClasses();
        long totalEnrolled = 0;
        for(SchoolClass sClass: assignedClasses)
            totalEnrolled =+ sClass.getStudents().size();

        SubmissionStats stats = new SubmissionStats(
                totalEnrolled,
                submissions.size(),
                submissions.stream().mapToInt(Result::getScore).average().orElse(0.0),
                assessment.getMaxPoints(),
                submissions.stream().filter(r -> r.getScore() >= (assessment.getMaxPoints() * 0.5)).count()
        );

        model.addAttribute("assessment", assessment);
        model.addAttribute("submissions", submissions);
        model.addAttribute("stats", stats);

        return "assessments/submissions";
    }

    @PostMapping("/save")
    public String saveAssessment(@ModelAttribute("assessment") Assessment assessment,
                                 RedirectAttributes redirectAttributes) {
        try {
            assessmentRepository.save(assessment);
            redirectAttributes.addFlashAttribute("message", "Assessment '" + assessment.getTitle() + "' saved successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error saving assessment: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/assessments";
    }

    @GetMapping("/record/{assessmentId}")
    public String showRecordMarkForm(@PathVariable Long assessmentId, Model model) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));

        // We only want students doing the assessment
        List<SchoolClass> assignedClasses = assessment.getSchoolClasses();
        List<Student> enrolledStudents = new ArrayList<>();
        for(SchoolClass sClass: assignedClasses)
            for(Student student: sClass.getStudents())
                enrolledStudents.add(student);

        Result result = new Result();
        result.setAssessment(assessment); // Pre-link the assessment

        model.addAttribute("assessment", assessment);
        model.addAttribute("enrolledStudents", enrolledStudents);
        model.addAttribute("result", result);

        return "assessments/submission-form";
    }

    @PostMapping("/results/save")
    public String saveResult(@ModelAttribute Result result, RedirectAttributes ra) {
        // Basic validation to ensure they didn't Exceed the HTML max attribute
        if (result.getScore() > result.getAssessment().getMaxPoints()) {
            ra.addFlashAttribute("message", "Score cannot exceed maximum points!");
            ra.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/assessments/record/" + result.getAssessment().getId();
        }

        resultRepository.save(result);
        ra.addFlashAttribute("message", "Mark recorded for student successfully!");
        ra.addFlashAttribute("alertClass", "alert-success");

        // Redirect back to the assessment list or a "view results" page
        return "redirect:/assessments";
    }
}
