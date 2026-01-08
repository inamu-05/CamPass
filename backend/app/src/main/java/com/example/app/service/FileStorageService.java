package com.example.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.*;

// 画像の保存や管理を行うサービスクラス　
@Service
public class FileStorageService {

    private static final String WEB_ACCESS_PREFIX = "/api/images/students";

    // application.propertiesからアップロード先のパスを読み込む
    @Value("${file.upload-dir}")
    private String uploadDirPath;

    // 画像ファイルを保存するメソッド
    public String storeFile(MultipartFile file, String filename) throws IOException {
        // ファイルが空の場合、デフォルト画像のパスを返す
        if (file.isEmpty()) {
            return WEB_ACCESS_PREFIX + "/default.png";
        }

        // 相対パスを絶対パスに変換
        Path uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
        // アップロードディレクトリが存在しない場合は作成する
        Files.createDirectories(uploadDir);

        // ファイル名を正規化
        filename = StringUtils.cleanPath(filename);

        // 保存先のパスを決定し、ファイルをコピー
        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // DBに保存するwebアクセス用のパスを生成
        // パスとファイル名を結合する
        return WEB_ACCESS_PREFIX + "/" + filename;
    }
}