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
var month = currentDate.getMonth() + 1; // 月は0から始まるため+1する
var day = currentDate.getDate();

// フォーマットを指定して日付を表示
var formattedDate = year + "/" + month + "/" + day;
document.getElementById("currentDate").textContent = formattedDate;

/*var xhr = new XMLHttpRequest();//XMLHttpRequestオブジェクトを生成し変数xhrにだいにゅう
xhr.open("POST", "/employment/startWork", true) //post,URL,リクエストを非同期と指定
xhr.setRequestHeader("Content-Type", "application/json"); //
xhr.onreadystatechange = function () {
  if (xhr.readyState === 4 && xhr.status === 200) {
    // 成功時の処理
    console.log("時刻が保存されました");
  }
};
var data = JSON.stringify({ startTime: currentTime });
xhr.send(data);*/
