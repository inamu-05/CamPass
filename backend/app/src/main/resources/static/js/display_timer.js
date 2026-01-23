document.addEventListener("DOMContentLoaded", () => {
    const otpDisplayElement = document.getElementById('otp-code-display');
    const timerDisplayElement = document.getElementById('timer-display');
    const displayContainer = document.getElementById('otp-display-container');
    
    let countdownInterval;

    // Retrieve data passed from the opener window's session storage
    const otpCode = sessionStorage.getItem('currentOtpCode');
    const expirationTime = sessionStorage.getItem('currentExpirationTime');

    if (!otpCode || !expirationTime) {
        otpDisplayElement.textContent = 'エラー';
        timerDisplayElement.textContent = 'データなし';
        return;
    }

    // 1. Display the Code
    otpDisplayElement.textContent = otpCode;
    
    // 2. Start the Timer
    startCountdown(expirationTime);

    // Countdown Timer Function (copied from your existing file, but isolated)
    function startCountdown(expirationTimeString) {
        // We still need the 'Z' fix here if the server time isn't explicitly UTC/local
        const utcExpirationString = expirationTimeString + 'Z';
        console.log("受け取る時間:" + expirationTimeString);  // 1
        // console.log(utcExpirationString);
        const expiryDate = new Date(expirationTimeString); 

        countdownInterval = setInterval(() => {
            const initialDate = new Date(); // Represents the current moment in the user's local time zone
            console.log(initialDate);  // 2
            const options = {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
                timeZone: 'Asia/Tokyo',
                hour12: false // Use 24-hour format
            };

            const japaneseTimeFormatter = new Intl.DateTimeFormat('ja-JP', options);

            const japaneseDateTimeString = japaneseTimeFormatter.format(initialDate);

            console.log(japaneseDateTimeString); // 3 
            const newDate = new Date(japaneseDateTimeString);
            console.log(expiryDate); // 4 
            console.log(newDate); // 5
            const diffMs = expiryDate.getTime() - newDate.getTime() - 85 * 60 * 1000; 

            if (diffMs <= 0) {
                clearInterval(countdownInterval);
                timerDisplayElement.textContent = '00:00';
                displayContainer.classList.add('expired'); 
                // Close the window after a few seconds, or keep it open
                // setTimeout(() => { window.close(); }, 5000); 
                return;
            }

            const secondsTotal = Math.floor(diffMs / 1000);
            const minutes = Math.floor(secondsTotal / 60);
            const seconds = secondsTotal % 60;

            const formattedTime = 
                String(minutes).padStart(2, '0') + ':' + 
                String(seconds).padStart(2, '0');

            timerDisplayElement.textContent = formattedTime;
        }, 1000); 
    }
});