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
    $('.recurrentEventDiv').hide();

    populateCourseNamesAddEvent();

    function populateEventNowTable() {
        console.log("YES");

        $.ajax({
          url: 'rest/event/now',
          type: 'GET',
          success: function(response) {

            $('#e-table-body').empty();
            Object.keys(response).forEach(function(key) {

              var event = response[key];
              var row = '<tr>' +
                '<td>' + event["name"] + '</td>' +
                '<td>' + event["date"] + '</td>' +
                '<td>' + event["start_time"] + '</td>' +
                '<td>' + event["end_time"] + '</td>' +
                '<td>' + event["description"] + '</td>' +
                '<td>' + event["type"] + '</td>' +
                //'<td>' + event["email"] + '</td>' +
                // Da fare getInformazioniEvento 
                // Da fare setUpdateEventoForm
                '<td class="text-center">' +'<a style="text-decoration: none;" href="#" onClick="#getInformazioniEvento(' + response[key]["id"] + ')" data-bs-toggle="modal" data-bs-target="#infoEventoModal" class="text-reset" tabindex="-1"> <button class="btn btn-secondary"><i class="fa-solid fa-circle-info fa-lg"></i></button>' + ' '
                +'<a style="text-decoration: none;" href="#" onClick="#updateEventForm(' + response[key]["id"] + ')" data-bs-toggle="modal" data-bs-target="#updateEventModal" class="text-reset" tabindex="-1"><button class="btn btn-warning" data-bs-toggle="modal" id="updateButtonShow" data-bs-target="#updateAulaModal"><i class="fa-solid fa-pen-to-square"></i></button>'+
                '</tr>' + '<td class="text-end"></td>';
              $('#e-table-body').append(row);
            });
          },
          error: function() {
            alert('Errore durante il recupero degli eventi.');
          }
        });
      }
      populateEventNowTable();

 /* Spostato in aule.js riga 272

    function populateClassroomNamesAddEvent() {
        $.ajax({
            url: 'rest/classroom/all',
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
                            //'<td>' + event["email"] + '</td>' +
                            '</tr>';
                    $('#e-table-body').append(row);
                });
            },
            error: function () {
                alert('Errore durante il recupero degli eventi.');
            }
        });
    }
    populateEventNowTable();

    /* Spostato in aule.js riga 272
     
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
     
     populateClassroomNamesAddEvent();
     */

});


$('#searchByClassAndDate').submit(function (event) {
    event.preventDefault();

       var classroomId = $('#eventClassroomName').val();
       var date = $('#settimanaPickerForClassroom').val();
    
    $.ajax({
        url: 'rest/event/'+ classroomId +'/week/'+ date,
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

$('#updateEventForm').submit(function (event) {
    event.preventDefault(); // Impedisce l'invio del form tramite il comportamento predefinito del browser

    var eventId = $('#eventId').val();

    var formData = {
        eventName: $('#eventNameUpdate').val(),
        eventDate: $('#eventDateUpdate').val(),
        startTime: $('#startTimeUpdate').val(),
        endTime: $('#endTimeUpdate').val(),
        eventDescription: $('#eventDescriptionUpdate').val(),
        eventType: $('#eventTypeUpdate').val(),
        eventEmail: $('#eventEmailUpdate').val(),
        classroomId: $('#classroomIdEventUpdate').val(),
        courseId: $('#courseIdEventUpdate').val()
    };

    console.log(formData);

    $.ajax({
        url: 'rest/event/updateEvent/' + eventId,
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

    if (value) {
        $('.recurrentEventDiv').show();
    } else {
        $('.recurrentEventDiv').hide();
    }
    console.log(value);
});

function getInformazioniEvento(event_id) {
    console.log(event_id);

$.ajax({
url: 'rest/event/' + event_id,
type: 'GET',
success: function (response) {
//rimozione righe statiche
$('#e-table-body-info').empty();
/*
var equipment = "";
var name = "Non assegnato";
response["Equipments"].forEach(element => equipment = equipment + "" + element + " - ");
equipment = equipment.slice(0, -2);
if (typeof response["Group"][0] !== "undefined") {
    name = response["Group"][0]["name"];

     id 	name 	date 	
     start_time 	end_time 	description 	
     type 	email 	course_id 	classroom_id 	
} */

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