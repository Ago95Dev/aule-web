$(document).ready(function () {

    function populateCourseNames(label_Id) {
        $.ajax({
            url: 'rest/course/all',
            type: 'GET',
            success: function (response) {
                var courseNames = Object.keys(response); //array di nomi delle aule

                // choice box con nomi delle aule
                var courseSelect = $('#' + label_Id);
                // var courseSelect = $('#courseIdEvent');
                courseNames.forEach(function (courseName) {
                    courseSelect.append('<option value="' + response[courseName] + '">' + courseName + '</option>');
                });
            },
            error: function () {
                console.log('Errore durante il recupero dei nomi delle aule');
            }
        });
    }


    $('.recurrentEventDiv').hide();

    // Id corso in add Event
    populateCourseNames('courseIdEvent');
    // Id corso in aggiorna aula 
    populateCourseNames('courseIdEventUpdate');

    function populateEventTodayTable() {
        $.ajax({
            url: 'rest/event/today',
            type: 'GET',
            success: function (response) {

                $('#e-table-body').empty();
                Object.keys(response).forEach(function (key) {
                    var event = response[key];

                    var row = '<tr>' +
                            '<td class="text-center">' + event["name"] + '</td>' +
                            '<td class="text-center">' + event["date"] + '</td>' +
                            '<td class="text-center">' + event["start_time"] + '</td>' +
                            '<td class="text-center">' + event["end_time"] + '</td>' +
                            '<td class="text-center">' + event["description"] + '</td>' +
                            '<td class="text-center">' + event["type"] + '</td>' +
                            '<td class="text-center">' + '<a style="text-decoration: none;" href="#" onClick="getInformazioniEvento(' + event["id"] + ')" data-bs-toggle="modal" data-bs-target="#infoEventoModal" class="text-reset" tabindex="-1"> <button class="btn btn-secondary"><i class="fa-solid fa-circle-info fa-lg"></i></button></a>' + ' '
                            + '<a style="text-decoration: none;" href="#" onClick="setUpdateEventForm(' + event["id"] + ')" data-bs-toggle="modal" data-bs-target="#updateEventModal" class="text-reset" tabindex="-1"><button class="btn btn-warning btn-auth" data-bs-toggle="modal" id="updateButtonShow" data-bs-target="#updateEventModal"><i class="fa-solid fa-pen-to-square"></i></button></a>'
                            + '<td class="text-end"></td>' + '</tr>';
                    $('#e-table-body').append(row);
                });
            },
            error: function () {
                alert('Errore durante il recupero degli eventi.');
            }
        });
    }
    populateEventTodayTable();

    function populateEventNowTable() {
        $.ajax({
            url: 'rest/event/now',
            type: 'GET',
            success: function (response) {

                $('#e-table-body').empty();
                Object.keys(response).forEach(function (key) {
                    var event = response[key];

                    var row = '<tr>' +
                            '<td class="text-center">' + event["name"] + '</td>' +
                            '<td class="text-center">' + event["date"] + '</td>' +
                            '<td class="text-center">' + event["start_time"] + '</td>' +
                            '<td class="text-center">' + event["end_time"] + '</td>' +
                            '<td class="text-center">' + event["description"] + '</td>' +
                            '<td class="text-center">' + event["type"] + '</td>' +
                            '<td class="text-center">' + '<a style="text-decoration: none;" href="#" onClick="getInformazioniEvento(' + event["id"] + ')" data-bs-toggle="modal" data-bs-target="#infoEventoModal" class="text-reset" tabindex="-1"> <button class="btn btn-secondary"><i class="fa-solid fa-circle-info fa-lg"></i></button>' + ' '
                            + '<a style="text-decoration: none;" href="#" onClick="setUpdateEventForm(' + event["id"] + ')" data-bs-toggle="modal" data-bs-target="#updateEventModal" class="text-reset" tabindex="-1"><button class="btn btn-warning btn-auth" data-bs-toggle="modal" id="updateButtonShow" data-bs-target="#updateEventModal"><i class="fa-solid fa-pen-to-square"></i></button>'
                            + '<td class="text-end"></td>' + '</tr>';
                    $('#e-table-body').append(row);
                });
            },
            error: function () {
                alert('Errore durante il recupero degli eventi.');
            }
        });
    }
    $('#recentEventsCheckbox').change(function () {
        if (this.checked) {
            populateEventNowTable();
        } else {
            populateEventTodayTable();
        }
    });

});


$('#searchByClassAndDate').submit(function (event) {
    event.preventDefault();

    var classroomId = $('#eventClassroomName').val();
    var dateFromInput = $('#settimanaPickerForClassroom').val();
    var date = new Date(dateFromInput).toJSON();

    var formData = {
        classroomId: classroomId,
        date: date
    };
    $.ajax({
        url: 'rest/event/week',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function (response) {

            $('#e-table-body').empty();
            Object.keys(response).forEach(function (key) {

                var event = response[key];
                var row = '<tr>' +
                        '<td>' + event["name"] + '</td>' +
                        '<td>' + event["date"] + '</td>' +
                        '<td>' + event["start_time"] + '</td>' +
                        '<td>' + event["end_time"] + '</td>' +
                        '<td>' + event["description"] + '</td>' +
                        '<td>' + event["type"] + '</td>' +
                        '</tr>';
                $('#e-table-body').append(row);
            });
        },
        error: function () {
            alert('Errore durante il recupero degli eventi.');
        }
    });
});


$('#searchByGroup').submit(function (event) {
    event.preventDefault();

    var group_id = $('#groupNameEvents').val();

    $.ajax({
        url: 'rest/event/group/' + group_id,
        type: 'GET',

        success: function (response) {

            $('#e-table-body').empty();
            Object.keys(response).forEach(function (key) {

                var event = response[key];
                var row = '<tr>' +
                        '<td>' + event["name"] + '</td>' +
                        '<td>' + event["date"] + '</td>' +
                        '<td>' + event["start_time"] + '</td>' +
                        '<td>' + event["end_time"] + '</td>' +
                        '<td>' + event["description"] + '</td>' +
                        '<td>' + event["type"] + '</td>' +
                        '</tr>';
                $('#e-table-body').append(row);
            });
        },
        error: function () {
            alert('Errore durante il recupero degli eventi.');
        }
    });
});

function setUpdateEventForm(event_id) {
    $.ajax({
        url: 'rest/event/' + event_id,
        type: 'GET',

        
        success: function (response) {

            // Altri aggiornamenti dei campi del form
            $('#eventIdUpdate').val(event_id);
            $('#eventNameUpdate').val(response["name"]);
            $('#eventDateUpdate').val(response["date"]);
            $('#startTimeUpdate').val(response["start_time"]);
            $('#endTimeUpdate').val(response["end_time"]);
            $('#eventDescriptionUpdate').val(response["description"]);
            $('#eventTypeUpdate').val(response["type"]);
            $('#eventEmailUpdate').val(response["email"]);
            $('#classroomIdEventUpdate').val(response["classroom_id"]);
            $('#courseIdEventUpdate').val(response["course_id"]);

        },
        error: function () {
            console.log('Errore durante il recupero dei dati dal database');
        }
    });
}

// Update Event 
$('#updateEventForm').submit(function (event) {
    event.preventDefault();
    var eventId = $('#eventIdUpdate').val();
    var recurrentForUpdate;
    var startTime = $('#startTimeUpdate').val();
    var endTime = $('#endTimeUpdate').val();

    if ($('#recurrentUpdateCheckbox').prop('checked')) {
        recurrentForUpdate = 1;
    } else {
        recurrentForUpdate = 0;
    }

    if (startTime.length > 5) {
        startTime = startTime.slice(0,5);
    }

    if (endTime.length > 5) {
        endTime = endTime.slice(0,5);
    }
    
    var formData = {
        name: $('#eventNameUpdate').val(),
        date: $('#eventDateUpdate').val(),
        start_time: startTime,
        end_time: endTime,
        description: $('#eventDescriptionUpdate').val(),
        type: $('#eventTypeUpdate').val(),
        email: $('#eventEmailUpdate').val(),
        classroom_id: $('#classroomIdEventUpdate').val(),
        course_id: $('#courseIdEventUpdate').val(),
        recurrent: recurrentForUpdate
    };

    $.ajax({
        url: 'rest/event/update/' + eventId,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function () {
            alert('Evento aggiornato');
        },
        error: function () {
            console.log(JSON.stringify(formData));
            alert('Errore durante l\'aggiornamento');
        }
    });
});

$('#exportToICalendar').submit(function (event) {
    event.preventDefault();
    var startDate = $('#startTimeForCalendar').val();
    var endDate = $('#endTimeForCalendar').val();
    const startDateArray = startDate.split("-");
    const endDateArray = endDate.split("-");

    var start = new Date(startDate).toJSON();
    var end = new Date(endDate).toJSON();

    var formData = {
        startDate: start,
        endDate: end
    };

    $.ajax({
        url: 'rest/event/icalendar/export',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function (data) {
            var blob = new Blob([data]);
            var link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = 'events.ics';
            link.click();
        },
        error: function () {
            console.log(JSON.stringify(formData));
            alert('Errore durante aggiornamento');
        }
    });


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


/*
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

$('#eventTypeUpdate').change(function (event) {
    var value = $('#eventTypeUpdate').val();
    if (value === "LEZIONE" || value === "PARZIALE" || value === "ESAME") {
        $('#courseIdEventUpdate').val("");
        $('#courseIdEventUpdate').prop("disabled", false);
    } else {
        $('#courseIdEventUpdate').val("");

        $('#courseIdEventUpdate').prop("disabled", true);

    }
});

$('#recurrentCheckbox').change(function (event) {
    var value = $('#recurrentCheckbox').prop('checked');
    $('#untilDate').val("");
    $('#recurrencyType').val("");

    if (value) {
        $('.recurrentEventDiv').show();
    } else {
        $('.recurrentEventDiv').hide();
    } 
});
*/


function getInformazioniEvento(event_id) {
    console.log(event_id);

    $.ajax({
        url: 'rest/event/' + event_id,
        type: 'GET',
        success: function (response) {
            $('#e-table-body-info').empty();

            var view =
                    '<tr>' + '<td>' + 'Nome ' + '</td>' + '<td>' + response["name"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Data ' + '</td>' + '<td>' + response["date"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Data Inizio ' + '</td>' + '<td>' + response["start_time"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Data Fine ' + '</td>' + '<td>' + response["end_time"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Descrizione ' + '</td>' + '<td>' + response["description"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Tipo ' + '</td>' + '<td>' + response["type"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Email ' + '</td>' + '<td>' + response["email"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Id Corso  ' + '</td>' + '<td>' + response["course_id"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Id Classe  ' + '</td>' + '<td style="white-space:pre-wrap; word-wrap:break-word">' + response["classroom_id"] + '</td>' + '</tr>';
            $('#e-table-body-info').append(view);

        },
        error: function () {
            console.log('Errore durante il recupero dei dati dal database');
        }
    });
}