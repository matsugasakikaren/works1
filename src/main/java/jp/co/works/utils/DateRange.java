package jp.co.works.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateRange {
	
	 private LocalDate startDate;
	    private LocalDate endDate;

	    public DateRange(LocalDate startDate, LocalDate endDate) {
	        this.startDate = startDate;
	        this.endDate = endDate;
	    }

	    public LocalDate getStartDate() {
	        return startDate;
	    }

	    public LocalDate getEndDate() {
	        return endDate;
	    }
	    
	public static List<String> generateDateRanges() {
		List<String> dateRanges = new ArrayList<>();
		
		//現在の日付けを取得
		//LocalDate currentDate = LocalDate.now();
		
		//期間の開始日を指定
		//LocalDate startDate = currentDate.withDayOfMonth(16);
		LocalDate startDate = LocalDate.of(2022, 12, 16);

		//DateTimeFormatterを定義
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

		//期間の生成(12か月分)
		for (int i = 0; i < 12; i++) {
			//期間の終了日を指定
			LocalDate endDate = startDate.plusMonths(1).minusDays(1);

			//期間追加
			String dateRange = startDate.format(formatter) + "～" + endDate.format(formatter);
			dateRanges.add(dateRange);

			//次の期間の開始日を指定
			startDate = startDate.plusMonths(1);
		}
		return dateRanges;
	}

	//現在の日付を基準に、当月16日から翌月15日までの期間の日付リストを返す
	public static List<LocalDate> getCurrentDateRange() {
		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = currentDate.withDayOfMonth(16);
		LocalDate endDate = startDate.plusMonths(1).minusDays(1);
		List<LocalDate> currentDateRange = new ArrayList<>();
		while (currentDate.isAfter(endDate)) {
			currentDateRange.add(startDate);
			startDate = startDate.plusDays(1);
		}
		return currentDateRange;
	}

	// 選択した期間の日付リストを取得　開始日から終了日までの日付を1日ずつリストに追加
	public static List<LocalDate> getSelectedDateList(String selectedPeriod) {
		List<LocalDate> selectedDateList = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String[] dates = selectedPeriod.split("～");
		LocalDate startDate = LocalDate.parse(dates[0], formatter);
		LocalDate endDate = LocalDate.parse(dates[1], formatter);

		while (!startDate.isAfter(endDate)) {
			selectedDateList.add(startDate);
			startDate = startDate.plusDays(1);
		}
		return selectedDateList;
	}
}