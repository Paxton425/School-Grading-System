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
        Assessment term1Project = new Assessment();
        term1Project.setTitle("Term 1 Programming Project");
        term1Project.setType(Assessment.AssessmentType.SBA); // Projects fall under SBA
        term1Project.setMaxPoints(50);
        term1Project.setSubject(subject);
        assessmentRepo.save(term1Project);

        Assessment term1Test = new Assessment();
        term1Test.setTitle("Term 1 Programming Test");
        term1Test.setType(Assessment.AssessmentType.SBA); // Projects fall under SBA
        term1Test.setMaxPoints(100);
        term1Test.setSubject(subject);
        assessmentRepo.save(term1Test);

        // A PAT
        Assessment itPat = new Assessment();
        itPat.setTitle("Coding Practicals 1");
        itPat.setType(Assessment.AssessmentType.PAT);
        itPat.setMaxPoints(60);
        itPat.setSubject(subject);
        assessmentRepo.save(itPat);

        Assessment exam = new Assessment();
        exam.setTitle("Coding Exam");
        exam.setType(Assessment.AssessmentType.EXAM);
        exam.setMaxPoints(120);
        exam.setSubject(subject);
        assessmentRepo.save(exam);

        // 5. Insert the actual Achievement (The Student's Marks)
        Result projectGrade = new Result();
        projectGrade.setEnrollment(enrollment);
        projectGrade.setAssessment(term1Project);
        projectGrade.setTerm(Result.Term.TERM_1);
        projectGrade.setScore(42); // 42/50
        achievementRepo.save(projectGrade);

        Result testGrade = new Result();
        testGrade.setEnrollment(enrollment);
        testGrade.setAssessment(term1Test);
        testGrade.setTerm(Result.Term.TERM_1);
        testGrade.setScore(78); // 78/100
        achievementRepo.save(testGrade);

        Result patGrade = new Result();
        patGrade.setEnrollment(enrollment);
        patGrade.setTerm(Result.Term.TERM_1);
        patGrade.setAssessment(itPat);
        patGrade.setScore(85); // 85/100
        achievementRepo.save(patGrade);

        Result examGrade = new Result();
        examGrade.setEnrollment(enrollment);
        examGrade.setTerm(Result.Term.TERM_1);
        examGrade.setAssessment(exam);
        examGrade.setScore(98); // 98/120
        achievementRepo.save(examGrade);
    }

    @Override
    public void run(String... args) throws Exception {
        insertSampleData();
    }
}
