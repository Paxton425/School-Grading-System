package com.example.springbootmvcdemo.loader;

import com.example.springbootmvcdemo.model.*;
import com.example.springbootmvcdemo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.Month;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired private StudentRepository studentRepo;
    @Autowired private SubjectRepository subjectRepo;
    @Autowired private EnrollmentRepository enrollmentRepo;
    @Autowired private AssessmentRepository assessmentRepo;
    @Autowired private ResultRepository achievementRepo;
    @Autowired private TeacherRepository teacherRepo;

    public void insertSampleData() {
        // 1. Create the Student
        Student student = new Student();
        student.setFirstName("Thabo");
        student.setLastName("Mokoena");
        student.setGrade(10);
        student.setGender(Student.Gender.MALE);
        student.setBirthDay(LocalDate.of(2009, Month.AUGUST, 12));
        student = studentRepo.save(student);

        Student student2 = new Student();
        student2.setFirstName("Lebogang");
        student2.setLastName("Moroko");
        student2.setGrade(10);
        student2.setGender(Student.Gender.FEMALE);
        student2.setBirthDay(LocalDate.of(2009, Month.JUNE, 22));
        student2 = studentRepo.save(student2);

        // 2. Create a Subject
        Subject subject = new Subject();
        subject.setSubjectCode("CAT");
        subject.setName("Computer Applications Technology");
        subject.setDepartment(Department.SERVICES_AND_TECH);
        subject.setGrade(10);
        subject.setSbaWeight(0.25);
        subject.setExamWeight(0.75);
        subject = subjectRepo.save(subject);

        Subject subject2 = new Subject();
        subject2.setSubjectCode("Maths");
        subject2.setName("Mathematics");
        subject2.setDepartment(Department.MATHEMATICS);
        subject2.setGrade(10);
        subject2.setSbaWeight(0.25);
        subject2.setExamWeight(0.75);
        subject2 = subjectRepo.save(subject2);

        Teacher teacher = new Teacher();
        teacher.setFirstName("Sthandiwe");
        teacher.setLastName("Zwane");
        teacher.setEmployeeId("EM-001");
        teacher.setDepartment(Department.SERVICES_AND_TECH);
        teacher.setRole(Teacher.Role.HOD);
        teacher.setGender(Teacher.Gender.FEMALE);
        teacher.setPhone("08976543");
        teacher.setEmail("sthandiwe@gmail.com");
        teacher = teacherRepo.save(teacher);


        // 3. Create the Enrollment (Links Thabo to IT)
        SubjectEnrollment enrollment = new SubjectEnrollment();
        enrollment.setStudent(student);
        enrollment.setSubject(subject);
        enrollment.setTeacher(teacher);
        enrollment = enrollmentRepo.save(enrollment);

        SubjectEnrollment enrollment2 = new SubjectEnrollment();
        enrollment2.setStudent(student2);
        enrollment2.setSubject(subject2);
        enrollment2.setTeacher(teacher);
        enrollment2 = enrollmentRepo.save(enrollment2);

        // 4. Create the Assessment Definitions (Definitions usually exist already)
        // A Project (SBA)
        Assessment term2Project = new Assessment();
        term2Project.setTitle("Term 1 Programming Project");
        term2Project.setType(Assessment.AssessmentType.SBA); // Projects fall under SBA
        term2Project.setMaxPoints(50);
        term2Project.setSubject(subject);
        assessmentRepo.save(term2Project);

        // A PAT
        Assessment itPat = new Assessment();
        itPat.setTitle("Coding Practicals 1");
        itPat.setType(Assessment.AssessmentType.PAT);
        itPat.setMaxPoints(100);
        itPat.setSubject(subject);
        assessmentRepo.save(itPat);

        // 5. Insert the actual Achievement (The Student's Marks)
        Result projectGrade = new Result();
        projectGrade.setEnrollment(enrollment);
        projectGrade.setAssessment(term2Project);
        projectGrade.setTerm(Result.Term.TERM_2);
        projectGrade.setScore(42.0); // 42/50
        achievementRepo.save(projectGrade);

        Result patGrade = new Result();
        patGrade.setEnrollment(enrollment);
        patGrade.setTerm(Result.Term.TERM_1);
        patGrade.setAssessment(itPat);
        patGrade.setScore(85.0); // 85/100
        achievementRepo.save(patGrade);
    }

    @Override
    public void run(String... args) throws Exception {
        insertSampleData();
    }
}
