package com.example.app.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.entity.Student;
import com.example.app.security.JwtUtil;
import com.example.app.service.StudentService;

@RestController
@CrossOrigin(origins = "*") // Flutter からのアクセスを許可
public class ApiStudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtUtil jwtUtil;

    @org.springframework.beans.factory.annotation.Value("${file.upload-dir}")
    private String uploadDir;
    
    @GetMapping("/api/student-card")
    public ResponseEntity<?> getStudent(@RequestHeader("Authorization") String authHeader) {
        try {
            // ヘッダーからトークンを抽出
            String token = authHeader.replace("Bearer ", "");
            // トークンからユーザーIDを取得
            String userId = jwtUtil.extractUserId(token);
            // ユーザーIDを使って学生情報を取得
            Student student = studentService.getStudentById(userId);

            // 学生情報が存在しない場合の処理
            if (student == null) {
                //  デバックログの出力
                System.err.println("学生情報が見つかりません: UserId=" + userId + ", Token=" + token);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("学生情報が見つかりません");
            }

            // 画像パスの生成 (Webアクセス可能なAPIパスに変換)
            String imgPath  = student.getImg();

            // 日付フォーマットの設定
            String birthFormatted = "";
            try {
                LocalDate birthDate = student.getBirth();
                if (birthDate != null) {
                    birthFormatted = birthDate.format(DateTimeFormatter.ofPattern("yyyy年M月d日"));
                }
            } catch (Exception e) {
                birthFormatted = (student.getBirth() != null) ? student.getBirth().toString() : "";
            }

            // レスポンスデータをマップに格納
            Map<String, Object> response = new HashMap<>();
            response.put("userId", student.getUserId());
            response.put("userName", student.getUserName());
            response.put("furigana", student.getFurigana());
            response.put("courseName", student.getCourse().getCourseName());
            response.put("birth", birthFormatted);
            response.put("address", student.getAddress());
            response.put("img", imgPath);
            response.put("token", token);
            
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            // エラーログ
            System.err.println("学生証情報取得中にエラーが発生しました: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("エラーが発生しました: " + e.getMessage());
        }
    }

    @GetMapping("/api/student/image")
    public ResponseEntity<Resource> getStudentImage(@RequestHeader("Authorization") String authHeader) {
        try {
            // 1. トークンからユーザーを特定
            String token = authHeader.replace("Bearer ", "");
            String userId = jwtUtil.extractUserId(token);
            Student student = studentService.getStudentById(userId);

            if (student == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 2. DBのパスから「ファイル名」だけを取り出す
            // DBに "/uploads/students/photo.jpg" と入っていても "photo.jpg" だけ取り出す安全策
            String dbPath = student.getImg();
            if (dbPath == null || dbPath.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            String filename = Paths.get(dbPath).getFileName().toString();

            // 3. 実際の保存場所(uploadDir)と結合してファイルを読み込む
            // uploadDir が "C:/uploads/students" などを指している前提
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // 4. ファイルが存在すれば画像として返す
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        // 拡張子がjpgでもpngでも、ブラウザやFlutterは IMAGE_JPEG 指定で大体表示できます
                        // 厳密にやるならファイル拡張子判定が必要ですが、まずはこれでOK
                        .contentType(MediaType.IMAGE_JPEG) 
                        .body(resource);
            } else {
                System.err.println("ファイルが存在しません: " + filePath.toString());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            System.err.println("画像取得エラー: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}