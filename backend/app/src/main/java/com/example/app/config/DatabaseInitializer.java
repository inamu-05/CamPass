package com.example.app.config;

import com.example.app.entity.CertManage;
import com.example.app.entity.Certificate;
import com.example.app.entity.ClassGroup;
import com.example.app.entity.Course;
import com.example.app.entity.Staff;
import com.example.app.entity.Student;
import com.example.app.entity.Subject;
import com.example.app.entity.SubjectClass;
import com.example.app.repository.CertMRepository;
import com.example.app.repository.CertManageRepository;
import com.example.app.repository.CertificateRepository;
import com.example.app.repository.ClassGroupRepository;
import com.example.app.repository.CourseRepository;
import com.example.app.repository.StaffRepository;
import com.example.app.repository.StudentRepository;
import com.example.app.repository.SubjectClassRepository;
import com.example.app.repository.SubjectRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import java.util.List;

/**
 * Ensures a default test user is present in the database on application startup.
 * This is the preferred method for guaranteed initialization when the password 
 * needs to be hashed by the application's configured PasswordEncoder.
 */
@Configuration
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    // Define a Bean that runs code on application startup
    @Bean
    public CommandLineRunner initDatabase(
            StaffRepository staffRepository, 
            StudentRepository studentRepository,
            ClassGroupRepository classGroupRepository,
            CourseRepository courseRepository,
            SubjectRepository subjectRepository,
            SubjectClassRepository subjectClassRepository,
            CertificateRepository certificateRepository,
            CertManageRepository certManageRepository,
            CertMRepository certMRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            try {

            // --- Course Data ---
            Course courseInfo = courseRepository.save(new Course("01", "情報"));
            Course courseInfo2 = courseRepository.save(new Course("02", "会計"));
            System.out.println("-> Course data created.");

            // --- 3. Insert ClassGroup Data ---
            ClassGroup class1_1 = classGroupRepository.save(new ClassGroup("01", "1-1"));
            ClassGroup class1_2 = classGroupRepository.save(new ClassGroup("02", "1-2"));
            ClassGroup class2_1 = classGroupRepository.save(new ClassGroup("03", "2-1"));
            ClassGroup class2_2 = classGroupRepository.save(new ClassGroup("04", "2-2"));
            System.out.println("-> ClassGroup data created.");

            

            final ClassGroup classId = new ClassGroup("01","1-1");
            final ClassGroup classId2 = new ClassGroup("02","1-2");

            final String initialStaffId = "admin";
            final String initialPassword = "adminpass"; // Plain text password for test user
            final Course initialCourseId = new Course("01", "情報"); 

            // 1. Check if the user already exists to prevent duplicate creation
            // We use findById() which is safe to call before the user is saved.
            if (staffRepository.findById(initialStaffId).isEmpty()) {
                
                // 2. Hash the password using the application's configured encoder
                String hashedPassword = passwordEncoder.encode(initialPassword);

                // 3. Create the user entity
                Staff initialStaff = new Staff(
                    initialStaffId, 
                    "Admin Test User", 
                    "アドミン",
                    initialCourseId, 
                    hashedPassword,
                    "2"
                );

                // 4. Save the user to the database
                staffRepository.save(initialStaff);
                System.out.println("-> Staff 'admin' created.");

                // Console output for confirmation
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Initial user created successfully:");
                System.out.println("ID: " + initialStaffId + " | Pass: " + initialPassword);
                System.out.println("-----------------------------------------------------------------");
            } else {
                System.out.println("-> Staff 'admin' already exists. Skipping.");
            }

            final String newStaffId = "0102001";
            final String newPassword = "teacher001"; // Plain text password for test user
            final Course newCourseId = new Course("01", "情報");

            if (staffRepository.findById("newStaffId").isEmpty()) {
                
                // 2. Hash the password using the application's configured encoder
                String hashedPassword = passwordEncoder.encode(newPassword);

                // 3. Create the user entity
                Staff newStaff = new Staff(
                    newStaffId, 
                    "New Teacher", 
                    "ニュー　ティーチャー",
                    newCourseId, 
                    hashedPassword,
                    "1"
                );

                // 4. Save the user to the database
                staffRepository.save(newStaff);
                System.out.println("-> Staff 'admin' created.");

                // Console output for confirmation
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Initial user created successfully:");
                System.out.println("ID: " + newStaffId + " | Pass: " + newPassword);
                System.out.println("-----------------------------------------------------------------");
            } else {
                System.out.println("-> Staff 'admin' already exists. Skipping.");
            }

            Staff adminStaff = staffRepository.findById(initialStaffId).orElseThrow();
            Staff newStaff = staffRepository.findById(newStaffId).orElseThrow();


            // --- 5. Insert Subject Data ---
            Subject sub01 = subjectRepository.save(new Subject("01", "情報", courseInfo, adminStaff));
            subjectRepository.save(new Subject("02", "簿記", courseInfo, adminStaff));
            subjectRepository.save(new Subject("03", "ネットワーク", courseInfo, adminStaff));
            subjectRepository.save(new Subject("04", "データベース", courseInfo, adminStaff));
            System.out.println("-> Subject data created.");

            // --- 5. Insert Subject Data ---
            subjectRepository.save(new Subject("05", "簿記", courseInfo2, newStaff));
            subjectRepository.save(new Subject("06", "原価計算", courseInfo2, newStaff));
            System.out.println("-> Subject data created.");


            final String stu1Id = "2201001";
            final String initialStudentPassword = "stupass";
            Student student1;
            if (studentRepository.findById(stu1Id).isEmpty()) {
                String hashedPassword = passwordEncoder.encode(initialStudentPassword);
                
                student1 = new Student(
                    stu1Id, 
                    "学生太郎", 
                    "ガクセイタロウ",
                    initialCourseId, 
                    hashedPassword,
                    java.time.LocalDate.of(2000, 1, 1), // birth
                    "090-1234-5678", // tel
                    "2201001@test.com", // mail
                    "東京都", // address
                    classId,
                    "/path/to/img1.jpg", // img
                    "1", // enrollment_status
                    "2022", // entry_year
                    null, // graduation_year
                    false // is_disabled
                );
                studentRepository.save(student1);
                System.out.println("-> Student '2201001' created.");
            } else {
                student1 = studentRepository.findById(stu1Id).orElseThrow(); // Fetch existing
            }

            // Student 2
            final String stu2Id = "2201002";
            Student student2;
            if (studentRepository.findById(stu2Id).isEmpty()) {
                String hashedPassword = passwordEncoder.encode(initialStudentPassword);
                
                student2 = new Student(
                    stu2Id, 
                    "学生花子", 
                    "ガクセイハナコ",
                    initialCourseId, 
                    hashedPassword,
                    java.time.LocalDate.of(2001, 5, 15), // birth
                    "090-8765-4321", // tel
                    "2201002@test.com", // mail
                    "大阪府", // address
                    classId2,
                    "/path/to/img2.jpg", // img
                    "1", // enrollment_status
                    "2022", // entry_year
                    null, // graduation_year
                    false // is_disabled
                );
                studentRepository.save(student2);
                System.out.println("-> Student '2201002' created.");
            } else {
                student2 = studentRepository.findById(stu2Id).orElseThrow(); // Fetch existing
            }

            // Student 1 enrolled in Subject 01
            subjectClassRepository.save(new SubjectClass(sub01, student1, student1.getClassGroup())); 
            
            // Student 2 enrolled in Subject 01
            subjectClassRepository.save(new SubjectClass(sub01, student2, student2.getClassGroup()));
            
            System.out.println("-> Subject enrollment data created.");


            // Certificate テーブル
            Certificate certificate1 = certificateRepository.save(new Certificate("01", "履歴書", 200));
            Certificate certificate2 = certificateRepository.save(new Certificate("02", "在学証明書", 300));
            Certificate certificate3 = certificateRepository.save(new Certificate("03", "健康診断書", 500));
            Certificate certificate4 = certificateRepository.save(new Certificate("04", "成績証明書", 600));
            Certificate certificate5 = certificateRepository.save(new Certificate("05", "卒業見込み証明書", 600));

            // CertManage テーブル
            certMRepository.save(new CertManage(certificate1, student1, 1, "1", "1", java.time.LocalDate.of(2025, 1, 10), false, "1"));
            certMRepository.save(new CertManage(certificate2, student2, 2, "1", "1", java.time.LocalDate.of(2025, 1, 11), false, "1"));
            certMRepository.save(new CertManage(certificate4, student1, 1, "1", "1", java.time.LocalDate.of(2025, 1, 5), true, "2"));
            certMRepository.save(new CertManage(certificate5, student2, 1, "1", "2", java.time.LocalDate.of(2025, 1, 6), true, "2"));

            
            System.out.println("-----------------------------------------------------------------");
            System.out.println("Database initialization complete.");
            System.out.println("-----------------------------------------------------------------");
        
        }
        catch (Exception e) {
                // THIS WILL CATCH ANY FAILURE (like a constraint violation)
                log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.error("!!! FAILED TO INITIALIZE DATABASE !!!");
                log.error("Error: {}", e.getMessage());
                log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                // e.printStackTrace(); // Uncomment this for a full stack trace
    }
};
}
}
