
$(document).ready(function () {

    var authToken = sessionStorage.getItem('authToken'); // Recupera il token salvato
    // cursore 
    $(".icon-button").on("mouseenter", function () {
        var overlayText = $(this).data("overlay-text");
        var position = $(this).offset();
        var buttonWidth = $(this).outerWidth();
        var buttonHeight = $(this).outerHeight();

        $(".overlay-notification").text(overlayText).css({
            top: position.top + buttonHeight + 10,
            left: position.left + buttonWidth / 2,
            display: "block"
        });
    }).on("mouseleave", function () {
        $(".overlay-notification").hide();
    });

    function hideClassroomButtons() {
        $('#aule .btn').hide();
    }
    function showClassroomButtons() {
        $('#aule .btn').show();
    }

    function hideEventButtons() {
        $('#eventi .btn').hide();
    }
    function showEventButtons() {
        $('#eventi .btn').show();
    }

    // Al click sulla navbar "Eventi"
    $('#nav-eventi').click(function () {
        hideClassroomButtons();
        showEventButtons();
        removeNotAuthorizedButton();
        $("#aulaTabSection").hide();
        $("#eventTabSection").show();
    });

    // Al click sulla navbar "Aule"
    $('#nav-aule').click(function () {
        hideEventButtons();
        showClassroomButtons();
        removeNotAuthorizedButton();
        $("#eventTabSection").hide();
        $("#aulaTabSection").show();
    });

    function removeNotAuthorizedButton() {
        console.log(authToken);
        if (authToken === "undefined" || authToken === null) {
            $('#aule .btn-auth').hide();
            $('#loginButton').show();
            $('#logoutButton').hide();
            $('#importCSVLogged').hide();
            $('#exportCSVLogged').hide();
            $('#updateButtonShow').hide();

        } else {
            $('#aule .btn-auth').show();
            $('#loginButton').hide();
            $('#logoutButton').show();
            $('#importCSVLogged').show();
            $('#exportCSVLogged').show();
            $('#updateButtonShow').show();

        }
    }

    $('#importCSVForm').submit(function (event) {
        event.preventDefault();

        var formData = new FormData();
        formData.set("csvInputFile", $('#csvFile')[0].files[0]);

        $.ajax({
            url: 'rest/classroom/csv/import',
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function () {
                alert('Importazione CSV completata');
            },
            error: function () {
                alert('Errore durante l\'importazione CSV');
            }
        });
    });

    $('#exportCSVButton').click(function () {
        $.ajax({
            url: 'rest/classroom/csv/export',
            type: 'GET',
            success: function (data) {
                var blob = new Blob([data]);
                var link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = 'aule_export.csv';
                link.click();
            },
            error: function () {
                alert('Errore durante l\'esportazione CSV');
            }
        });
    });


// id addAula on submit function 
    $('#addAulaForm').submit(function (event) {
        event.preventDefault();
        // object building 
        var formData = {
            name: $('#name').val(),
            positionID: $('#positionID').val(),
            capacity: $('#capacity').val(),
            email: $('#email').val(),
            numberOfEthernet: $('#numberOfEthernet').val(),
            numberOfSockets: $('#numberOfSockets').val(),
            note: $('#note').val(),
            equipmentsId: $('#equipmentsId').val()
        };
        console.log(formData);

        $.ajax({
            url: 'rest/classroom/addClassroom',
            type: 'POST',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + authToken
            },
            data: JSON.stringify(formData),
            success: function () {
                $('#name').val(""),
                        $('#positionID').val(""),
                        $('#capacity').val(""),
                        $('#email').val(""),
                        $('#numberOfEthernet').val(""),
                        $('#numberOfSockets').val(""),
                        $('#note').val(""),
                        $('#equipmentsId').val(""),
                        alert('Aula Inserita con Successo');
            },
            error: function () {
                console.log(JSON.stringify(formData));
                alert('Errore inserimento aula.');
            }
        });
    });

    function populateClassroomNames() {
        $.ajax({
            url: 'rest/classroom/all',
            type: 'GET',
            success: function (response) {
                var classroomNames = Object.keys(response); //array di nomi delle aule

                // choice box con nomi delle aule
                var classroomSelect = $('#classroomName');
                classroomNames.forEach(function (className) {
                    classroomSelect.append('<option value="' + response[className] + '">' + className + '</option>');
                });
            },
            error: function () {
                console.log('Errore durante il recupero dei nomi delle aule');
            }
        });
    }

    function populateClassroomNames(label_Id) {
        $.ajax({
            url: 'rest/classroom/all',
            type: 'GET',
            success: function (response) {
                var classroomNames = Object.keys(response); //array di nomi delle aule
                // choice box con nomi delle aule
                var classroomSelect = $('#' + label_Id);
                //var classroomSelect = $('#classroomName');
                classroomNames.forEach(function (className) {
                    classroomSelect.append('<option value="' + response[className] + '">' + className + '</option>');
                });
            },
            error: function () {
                console.log('Errore durante il recupero dei nomi delle aule');
            }
        });
    }

    function populateGroupNames() {
        $.ajax({
            url: 'rest/classroom/group/all',
            type: 'GET',
            success: function (response) {
                var groupNames = Object.keys(response); // array nomi dei gruppi
                var groupSelect = $('#groupName');
                groupNames.forEach(function (groupName) {
                    groupSelect.append('<option value="' + response[groupName] + '">' + groupName + '</option>');
                });
            },
            error: function () {
                console.log('Errore durante il recupero dei nomi dei gruppi');
            }
        });
    }

    function populatePosition() {
        $.ajax({
            url: 'rest/classroom/position/all',
            type: 'GET',
            success: function (response) {
                console.log(response);

                var positionNames = Object.keys(response); // array nomi dei gruppi
                var positionSelect = $('#positionID');
                positionNames.forEach(function (positionName) {
                    positionSelect.append('<option value="' + response[positionName] + '">' + positionName + '</option>');
                });
            },
            error: function () {
                console.log('Errore durante il recupero delle posizione');
            }
        });
    }

    function populateEquipmentIDs() {
        $.ajax({
            url: 'rest/classroom/equipment/all',
            type: 'GET',
            success: function (response) {

                var equipmentNames = Object.keys(response); // array nomi dei gruppi
                var equipmentSelect = $('#equipmentsId');
                equipmentNames.forEach(function (equipmentName) {
                    equipmentSelect.append('<option value="' + response[equipmentName] + '">' + equipmentName + '</option>');
                });
            },
            error: function () {
                console.log('Errore durante il recupero delle posizione');
            }
        });
    }
    


    function populateTable() {
        $.ajax({
            url: 'rest/classroom/display/all',
            type: 'GET',

            success: function (response) {
                //rimozione righe statiche
                $('#table-body').empty();
                Object.keys(response).forEach(key => {

                    var row = '<tr>' +
                            '<td class="text-center"><a style="text-decoration: none;" href="#" onClick="getInformazioniAula(' + response[key]["id"] + ')" data-bs-toggle="modal" data-bs-target="#infoAulaModal" class="text-reset" tabindex="-1">' + key + '</a></td>' +
                            '<td class="text-center">' + response[key]["email"] + '</td>' +
                            '<td class="text-center">' + response[key]["capacity"] + '</td>' +
                            '<td class="text-center">' + response[key]["note"] + '</td>' +
                            '<td class="text-center">' + '<a style="text-decoration: none;" href="#" onClick="getInformazioniAula(' + response[key]["id"] + ')" data-bs-toggle="modal" data-bs-target="#infoAulaModal" class="text-reset" tabindex="-1"> <button class="btn btn-secondary"><i class="fa-solid fa-circle-info fa-lg"></i></button>' + ' '
                            +'<a style="text-decoration: none;" href="#" onClick="setUpdateAulaForm(' + response[key]["id"] + ')" data-bs-toggle="modal" data-bs-target="#updateAulaModal" class="text-reset" tabindex="-1"><button class="btn btn-warning" data-bs-toggle="modal" id="updateButtonShow" data-bs-target="#updateAulaModal"><i class="fa-solid fa-pen-to-square"></i></button>'+
                    '<td class="text-end"></td>' +
                            '</tr>';
                    $('#table-body').append(row);
                });

            },
            error: function () {
                console.log('Errore durante il recupero dei dati dal database');
            }
        });
    }


    removeNotAuthorizedButton();
    //choicebox dei nomi delle aule e dei gruppi
    populateClassroomNames('classroomName');
    populateClassroomNames('classroomIdEvent');
    populateClassroomNames('eventClassroomName');

    populatePosition();
    populateEquipmentIDs();
    //populateClassroomNames();
    populateGroupNames();
    populateTable();
});

function populateEquipmentUpdateIDs() {
        $.ajax({
            url: 'rest/classroom/equipment/all',
            type: 'GET',
            success: function (response) {

                var equipmentNames = Object.keys(response); // array nomi dei gruppi
                var equipmentSelect = $('#equipmentsIdUpdateRoom');
                equipmentNames.forEach(function (equipmentName) {
                    equipmentSelect.append('<option value="' + response[equipmentName] + '">' + equipmentName + '</option>');
                });
            },
            error: function () {
                console.log('Errore durante il recupero delle posizione');
            }
        });
}

    function populatePosition() {
        $.ajax({
            url: 'rest/classroom/position/all',
            type: 'GET',
            success: function (response) {
                console.log(response);

                var positionNames = Object.keys(response); // array nomi dei gruppi
                var positionSelect = $('#positionIDUpdateRoom');
                positionNames.forEach(function (positionName) {
                    positionSelect.append('<option value="' + response[positionName] + '">' + positionName + '</option>');
                });
            },
            error: function () {
                console.log('Errore durante il recupero delle posizione');
            }
        });
    }
function getInformazioniAula(classroom_id) {
    $.ajax({
        url: 'rest/classroom/' + classroom_id,
        type: 'GET',

        success: function (response) {
            //rimozione righe statiche
            $('#table-body-info').empty();

            var equipment = "";
            var name = "Non assegnato";
            response["Equipments"].forEach(element => equipment = equipment + "" + element + " - ");
            equipment = equipment.slice(0, -2);
            if (typeof response["Group"][0] !== "undefined") {
                name = response["Group"][0]["name"];
            }

            var view =
                    '<tr>' + '<td>' + 'Nome ' + '</td>' + '<td>' + response["name"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Location ' + '</td>' + '<td>' + response["location"] + " " + response["building"] + " Piano " + response["floor"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Referente ' + '</td>' + '<td>' + response["email"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Capacità ' + '</td>' + '<td>' + response["capacity"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Note ' + '</td>' + '<td>' + response["note"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Prese Elettriche ' + '</td>' + '<td>' + response["numberOfSockets"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Prese Ethernet ' + '</td>' + '<td>' + response["numberOfEthernet"] + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Dipartimento  ' + '</td>' + '<td>' + name + '</td>' + '</tr>' +
                    '<tr>' + '<td>' + 'Attrezzature  ' + '</td>' + '<td style="white-space:pre-wrap; word-wrap:break-word">' + equipment + '</td>' + '</tr>';
            $('#table-body-info').append(view);

        },
        error: function () {
            console.log('Errore durante il recupero dei dati dal database');
        }
    });
}

function setUpdateAulaForm(classroom_id) {
    $.ajax({
        url: 'rest/classroom/' + classroom_id,
        type: 'GET',

        success: function (response) {
            //rimozione righe statiche
            $('#table-body-info').empty();

            var equipment = "";
            var name = "Non assegnato";
            response["Equipments"].forEach(element => equipment = equipment + "" + element + " - ");
            equipment = equipment.slice(0, -2);
            if (typeof response["Group"][0] !== "undefined") {
                name = response["Group"][0]["name"];
            }
            
            $('#classroomIdUpdate').val(classroom_id);
            $('#nameUpdateRoom').val(response["name"]);
            $('#capacityUpdateRoom').val(response["capacity"]);
            $('#emailUpdateRoom').val(response["email"]);
            $('#numberOfEthernetUpdateRoom').val(response["numberOfEthernet"]);
            $('#numberOfSocketsUpdateRoom').val(response["numberOfSockets"]);
            $('#noteUpdateRoom').val(response["note"]);
            populateEquipmentUpdateIDs();
            populatePosition();
            
        },
        error: function () {
            console.log('Errore durante il recupero dei dati dal database');
        }
    });
}

// 
$('#addClassroomToGroupForm').submit(function (event) {
    event.preventDefault();
    var classroomId = $('#classroomName').val();
    var groupId = $('#groupName').val();

    var formData = {
        classroom_id: classroomId,
        group_id: groupId
    };

    $.ajax({
        url: 'rest/classroom/' + classroomId + '/group/' + groupId,
        type: 'POST',
        contentType: 'application/json',

        success: function (response) {
            console.log('Aula aggiunta al gruppo con successo');
        },
        error: function () {
            console.log('Errore durante l\'aggiunta dell\'aula al gruppo');
        }
    });
});



$('#updateAulaForm').submit(function (event) {
        event.preventDefault();

        var classroomId = $('#classroomIdUpdate').val();

        var formData = {
            name: $('#nameUpdateRoom').val(),
            positionID: $('#positionIDUpdateRoom').val(),
            capacity: $('#capacityUpdateRoom').val(),
            email: $('#emailUpdateRoom').val(),
            numberOfEthernet: $('#numberOfEthernetUpdateRoom').val(),
            numberOfSockets: $('#numberOfSocketsUpdateRoom').val(),
            note: $('#noteUpdateRoom').val(),
            equipmentsId: $('#equipmentsIdUpdateRoom').val()
        };

        //console.log(formData);

        $.ajax({
            url: 'rest/classroom/updateClassroom/' + classroomId,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function () {
                alert('Aula aggiornata');
            },
            error: function () {
                console.log(JSON.stringify(formData));
                alert('Errore durante aggiornamento');
            }
        });
    });