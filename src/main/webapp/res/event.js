$('#addEventForm').submit(function(event) {
  event.preventDefault();

  var formData = {
    name: $('#eventName').val(),
    date: $('#eventDate').val(),
    start_time: $('#startTime').val(),
    end_time: $('#endTime').val(),
    description: $('#eventDescription').val(),
    type: $('#eventType').val(),
    email: $('#eventEmail').val(),
    classroom_id: $('#classroomId').val(),
    course_id: $('#courseId').val(),
    until_date: $('#untilDate').val(),
    typeOfRecurrency: $('#recurrencyType').val()
  };

  console.log(formData);

  $.ajax({
    url: 'rest/event/add',
    type: 'POST',
    contentType: 'application/json',
    /* headers: {
      'Authorization': 'Bearer ' + authToken
    }, */
    data: JSON.stringify(formData),
    success: function(response) {
      console.log('Evento inserito correttamente');
    },
    error: function(xhr, textStatus, errorThrown) {
      console.log(JSON.stringify(formData));
      console.log('Errore durante l\'inserimento dell\'evento. Riprova.');
    }
  });
});