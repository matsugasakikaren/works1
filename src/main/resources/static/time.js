function showClock1() {
    var nowTime = new Date(); //  現在日時を得る
    var nowHour = nowTime.getHours(); // 時を抜き出す
    var nowMin  = nowTime.getMinutes(); // 分を抜き出す
    var nowSec  = nowTime.getSeconds(); // 秒を抜き出す
    var msg = nowHour + ":" + nowMin + ":" + nowSec;
    document.getElementById("RealtimeClockArea").innerHTML = msg;
}

function updateClock() {
    showClock1();
    setInterval(showClock1, 1000); // 1秒ごとに更新
}

updateClock(); // 最初に実行

// 現在の日付を取得
var currentDate = new Date();

// 年、月、日を取得
var year = currentDate.getFullYear();
var month = currentDate.getMonth() + 1; // 月は0から始まるため+1
var day = currentDate.getDate();

// フォーマットを指定して日付を表示
var formattedDate = year + "/" + month + "/" + day;
document.getElementById("currentDate").textContent = formattedDate;

function validateForm() {
    var userIdInput = document.getElementById("userId");
    var userIdValue = userIdInput.value.trim();

    // 全角文字が含まれている場合はエラーメッセージを表示してフォームの送信をキャンセル
    if (userIdValue.match(/[^\x01-\x7E]/)) {
        alert("社員IDは半角英数字で入力してください。");
        return false;
    }
    
    return true; // フォームの送信を許可
}

