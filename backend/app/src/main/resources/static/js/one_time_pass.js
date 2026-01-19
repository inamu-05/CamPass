document.addEventListener("DOMContentLoaded", () => {
    // フォーム要素
    const otpFormCard = document.getElementById('otp-form-card');
    const subjectSelect = document.getElementById("subject");
    const passField = document.getElementById("generated-pass");
    const generateBtn = document.getElementById("generate-btn");
    const saveBtn = document.getElementById("save-btn");
    const displayBtn = document.getElementById("display-btn");
    const backBtn = document.getElementById("back-btn");
    const messageArea = document.getElementById("message-area"); 
    
    // 表示要素
    const displayContainer = document.getElementById('otp-display-container');
    const otpDisplayElement = document.getElementById('otp-code-display');
    const timerDisplayElement = document.getElementById('timer-display');
    const newSessionBtn = document.getElementById('new-session-btn');

    // 変数
    let countdownInterval;

    /**
     * 数字を4桁にゼロパディングする
     * @param {number} num 
     * @returns {string}
     */
    function formatToFourDigits(num) {
        return String(num).padStart(4, '0');
    }

    /**
     * メッセージ表示用の関数
     * @param {string} text - 表示するメッセージ
     * @param {boolean} isError - エラーメッセージかどうか
     */
    function showMessage(text, isError = false) {
        messageArea.textContent = text;
        // 既存のCSSクラスを使用
        messageArea.style.color = isError ? "red" : "green";
        messageArea.style.fontWeight = "bold";
    }

    // ✅ 1. ランダムな4桁の数字パス生成
    generateBtn.addEventListener("click", (e) => {
        e.preventDefault(); // フォーム送信を防ぐ
        const subject = subjectSelect.value;
        if (!subject) {
            showMessage("科目を選択してください。", true);
            return;
        }
        const pass = formatToFourDigits(Math.floor(Math.random() * 10000));
        passField.value = pass;
        showMessage("ワンタイムパスが作成されました。保存ボタンを押してセッションを開始してください。", false);
    });

    // ✅ 2. 保存処理（API送信とタイマー開始）
    saveBtn.addEventListener("click", async (e) => {
        e.preventDefault(); // フォーム送信を防ぐ

        const subjectId = subjectSelect.value;
        const pass = passField.value;

        // バリデーション
        if (!subjectId) {
            showMessage("科目を選択してください。", true);
            return;
        }
        if (!pass || pass.length !== 4) {
            showMessage("4桁のワンタイムパスを作成してください。", true);
            return;
        }

        // CSRFトークンを取得 (Spring Security対応)
        const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        // ボタンの無効化とメッセージ更新
        saveBtn.disabled = true;
        saveBtn.textContent = '保存中...';
        showMessage('サーバーにOTPを保存し、セッションを開始しています...', false);
        
        // 既存タイマーをクリア
        if (countdownInterval) {
            clearInterval(countdownInterval);
        }

        try {
            // API呼び出し
            const response = await fetch("/save-onetime-pass", {
                method: "POST",
                headers: { 
                    "Content-Type": "application/json",
                    [header]: token // CSRFトークンをヘッダーに追加
                },
                body: JSON.stringify({ subjectId: subjectId, pass: pass })
            });
            console.log(response);
            
            if (response.ok) {
                // 成功: JSONレスポンスをパース
                const otpData = await response.json(); 

                console.log(otpData);

                // --- NEW LOGIC: Use sessionStorage and open new window ---
    
                // 1. Save data to Session Storage so the new window can access it
                sessionStorage.setItem('currentOtpCode', otpData.otpCode);
                sessionStorage.setItem('currentExpirationTime', otpData.expirationTime);

                // 2. Open the new display page in a new window/tab
                // window.open('/attendance/otp/display', 'OTPCurrentDisplay', 'width=800,height=600,top=100,left=100');
                
                // // 3. OTP表示エリアの更新と表示
                // otpDisplayElement.textContent = otpData.otpCode;
                // displayContainer.classList.remove('hidden', 'expired'); 
                // otpFormCard.classList.add('hidden'); // フォームを非表示にする

                displayBtn.classList.remove('hidden'); // Keep the form hidden

                // 入力フィールドをクリア（セキュリティのため）
                // passField.value = ''; 

                // カウントダウン開始
                startCountdown(otpData.expirationTime);

                showMessage("成功: セッションが開始されました。", false);

            } else {
                // 失敗
                const errorText = await response.text();
                throw new Error(`サーバーエラー (${response.status}): ${errorText.substring(0, 100)}...`);
            }

        } catch (error) {
            console.error("OTP generation error:", error);
            showMessage('エラー: OTP発行に失敗しました。詳細: ' + error.message, true);
            
            // エラーが発生した場合、フォームを再表示
            otpFormCard.classList.remove('hidden');
            displayContainer.classList.add('hidden');
            
        } finally {
            // ボタンを再有効化
            saveBtn.disabled = false;
            saveBtn.textContent = '保存/セッション開始';
        }
    });

    displayBtn.addEventListener("click", () => {
        window.open('/attendance/otp/display', 'OTPCurrentDisplay'); // , 'width=800,height=600,top=100,left=100'
    });

    // ✅ 3. 戻る（メインメニューへ）
    backBtn.addEventListener("click", () => {
        window.location.href = "/main"; // 適切なメインメニューURLに修正
    });

    // ✅ 4. 新規セッション開始ボタン
    newSessionBtn.addEventListener('click', () => {
        if (countdownInterval) {
            clearInterval(countdownInterval);
        }
        displayContainer.classList.add('hidden');
        otpFormCard.classList.remove('hidden');
        showMessage('新しいセッションを開始するため、OTPを作成・保存してください。', false);
    });


    // 5. カウントダウンタイマー機能
    function startCountdown(expirationTimeString) {
        // 💡 CRITICAL FIX: Append 'Z' to treat the string as UTC/Zulu time.
        // This allows the browser to correctly calculate the difference 
        // regardless of the local time zone offset.
        const utcExpirationString = expirationTimeString + 'Z'; 
        const expiryDate = new Date(utcExpirationString); // Use the string with 'Z'

        // 1秒ごとにタイマーを実行
        countdownInterval = setInterval(() => {
            const now = new Date();
            // ミリ秒単位での差分
            const diffMs = expiryDate.getTime() - now.getTime(); 

            if (diffMs <= 0) {
                // 期限切れ
                clearInterval(countdownInterval);
                timerDisplayElement.textContent = '00:00';
                displayContainer.classList.add('expired'); // 期限切れのビジュアル効果を適用
                showMessage('期限切れ: パスワードは無効になりました。', true);
                
                // フォームを再表示
                otpFormCard.classList.remove('hidden');
                return;
            }

            // 残り時間を計算
            const secondsTotal = Math.floor((diffMs-85000) / 1000); // TODO
            const minutes = Math.floor(secondsTotal / 60);
            const seconds = secondsTotal % 60;

            const formattedTime = 
                String(minutes).padStart(2, '0') + ':' + 
                String(seconds).padStart(2, '0');

            timerDisplayElement.textContent = formattedTime;

            // 残り時間が1分未満の場合、色を変更（緊急性を示す）
            if (minutes === 0 && seconds <= 60) {
                timerDisplayElement.classList.add('text-red-700');
            } else {
                timerDisplayElement.classList.remove('text-red-700');
            }

        }, 1000); 
    }
});