document.addEventListener('DOMContentLoaded', function() {
  var calendarEl = document.getElementById('calendar');
  var calendar = new FullCalendar.Calendar(calendarEl, {
    plugins: [ ],
    headerToolbar: {
      start: 'prevYear,nextYear',
      center: 'title',
      end: 'dayGridMonth,listMonth,prev,next'
    },
    locale: 'ja',
    businessHours: true,
    contentHeight: "auto",
    initialView: 'dayGridMonth'
  });
    calendar.render();
});