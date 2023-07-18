function showClock1() {
    var nowTime = new Date(); //  現在日時を得る
    var nowHour = nowTime.getHours(); // 時を抜き出す
    var nowMin  = nowTime.getMinutes(); // 分を抜き出す
    var nowSec  = nowTime.getSeconds(); // 秒を抜き出す
    var msg = nowHour + ":" + nowMin + ":" + nowSec;
    document.getElementById("RealtimeClockArea").innerHTML = msg;
}
setInterval(showClock1,1000);

// 現在の日付を取得
var currentDate = new Date();

// 年、月、日を取得
var year = currentDate.getFullYear();
var month = currentDate.getMonth() + 1; // 月は0から始まるため+1
var day = currentDate.getDate();

// フォーマットを指定して日付を表示
var formattedDate = year + "/" + month + "/" + day;
document.getElementById("currentDate").textContent = formattedDate;