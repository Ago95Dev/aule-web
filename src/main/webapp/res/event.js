$(document).ready(function () {

    function populateCourseNamesAddEvent() {
        $.ajax({
            url: 'rest/course/all',
            type: 'GET',
            success: function (response) {
                var courseNames = Object.keys(response); //array di nomi delle aule

                // choice box con nomi delle aule
                var courseSelect = $('#courseIdEvent');
                courseNames.forEach(function (courseName) {
                    courseSelect.append('<option value="' + response[courseName] + '">' + courseName + '</option>');
                });
            },
            error: function () {
                console.log('Errore durante il recupero dei nomi delle aule');
            }
        });
    }

    function populateClassroomNamesAddEvent() {
        $.ajax({
            url: 'rest/classroom/all',
            type: 'GET',
            success: function (response) {
                var classroomNames = Object.keys(response); //array di nomi delle aule

                // choice box con nomi delle aule
                var classroomSelect = $('#classroomIdEvent');
                classroomNames.forEach(function (className) {
                    classroomSelect.append('<option value="' + response[className] + '">' + className + '</option>');
                });
            },
            error: function () {
                console.log('Errore durante il recupero dei nomi delle aule');
            }
        });
    }

    $('.recurrentEventDiv').hide();

    populateClassroomNamesAddEvent();
    populateCourseNamesAddEvent();
});

$('#addEventForm').submit(function (event) {
    event.preventDefault();

    var formData = {
        name: $('#eventName').val(),
        date: $('#eventDate').val(),
        start_time: $('#startTime').val(),
        end_time: $('#endTime').val(),
        description: $('#eventDescription').val(),
        type: $('#eventType').val(),
        email: $('#eventEmail').val(),
        classroom_id: $('#classroomIdEvent').val(),
        course_id: $('#courseIdEvent').val(),
        until_date: $('#untilDate').val(),
        typeOfRecurrency: $('#recurrencyType').val()
    };

    $.ajax({
        url: 'rest/event/add',
        type: 'POST',
        contentType: 'application/json',
        /* headers: {
         'Authorization': 'Bearer ' + authToken
         }, */
        data: JSON.stringify(formData),
        success: function (response) {
            console.log('Evento inserito correttamente');
            $('#eventName').val(""),
            $('#eventDate').val(""),
            $('#startTime').val(""),
            $('#endTime').val(""),
            $('#eventDescription').val(""),
            $('#eventType').val(""),
            $('#eventEmail').val(""),
            $('#classroomIdEvent').val(""),
            $('#courseIdEvent').val(""),
            $('#untilDate').val(""),
            $('#recurrencyType').val("")
        },
        error: function (xhr, textStatus, errorThrown) {
            console.log(JSON.stringify(formData));
            console.log('Errore durante l\'inserimento dell\'evento. Riprova.');
        }
    });
});

$('#eventType').change(function (event) {
    console.log("z");
    var value = $('#eventType').val();
    if (value === "LEZIONE" || value === "PARZIALE" || value === "ESAME") {
        $('#courseIdEvent').val("");
        $('#courseIdEvent').prop("disabled", false);
    } else {        
        $('#courseIdEvent').val("");
        
        $('#courseIdEvent').prop("disabled", true);

    }
});

$('#recurrentCheckbox').change(function (event) {
    var value = $('#recurrentCheckbox').prop('checked');
    $('#untilDate').val("");
    $('#recurrencyType').val("");

    if(value){
        $('.recurrentEventDiv').show();
    } else {
       $('.recurrentEventDiv').hide();
    }
    console.log(value);
});