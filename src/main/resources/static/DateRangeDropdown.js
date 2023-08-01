function showSelectedDates() {
    var selectedOption = document.querySelector("select[name='selectedPeriod'] option:checked");
    var startDate = new Date(selectedOption.textContent.slice(0, 10));
    var endDate = new Date(selectedOption.textContent.slice(14));

    var dateList = document.getElementById("selectedDatesList");
    dateList.innerHTML = ""; // リストをクリア

    var currentDate = new Date(startDate);
    while (currentDate <= endDate) {
        var li = document.createElement("li");
        li.textContent = currentDate.toISOString().slice(0, 10);
        dateList.appendChild(li);
        currentDate.setDate(currentDate.getDate() + 1);
    }

    // ここでdutyテーブルのレコードを表示
    var dutyList = /* ここにdutyテーブルのレコードを取得する処理を記述 */;
    var table = document.querySelector("table");

    for (var i = 0; i < dutyList.length; i++) {
        var duty = dutyList[i];
        var workDate = new Date(duty.workDate);

        // 勤務日が該当期間内の場合
        if (workDate >= startDate && workDate <= endDate) {
            var tr = document.createElement("tr");

            // 日付
            var dateTd = document.createElement("td");
            dateTd.textContent = workDate.toISOString().slice(0, 10);
            tr.appendChild(dateTd);

            // 勤務日
            var workDateTd = document.createElement("td");
            workDateTd.textContent = workDate.toISOString().slice(0, 10);
            tr.appendChild(workDateTd);

            // 勤務状況
            var attendanceStatusTd = document.createElement("td");
            var dayOfWeek = workDate.getDay();
            if (dayOfWeek === 0 || dayOfWeek === 6) {
                // 土曜日または日曜日の場合は休日と表示
                attendanceStatusTd.textContent = "休日";
            } else {
                // 勤務日の場合は出勤と表示
                attendanceStatusTd.textContent = "出勤";
            }
            tr.appendChild(attendanceStatusTd);

            // 出勤時刻、退勤時刻、休憩時間、残業時間は何も表示しない

            table.appendChild(tr);
        }
    }
}

// 初期表示時に実行
showSelectedDates();