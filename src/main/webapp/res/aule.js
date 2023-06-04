// da sistemare, chiamate ajax non funzionano

$(document).ready(function() {
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
    $('#nav-eventi').click(function() {
      hideClassroomButtons();
      showEventButtons();
    });
    // Al click sulla navbar "Aule"
    $('#nav-aule').click(function() {
      hideEventButtons();
      showClassroomButtons();
    });


// id addAula on submit function 
$('#addAulaForm').submit(function(event) {
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
        data: JSON.stringify(formData),
        success: function() {
            message("Aula inserita", "success");
        },
        error: function() {
          console.log(JSON.stringify(formData));
          alert('Errore inserimento aula.');
        } 
      });
});

    function populateClassroomNames() {
      $.ajax({
        url: '/rest/classroom/all', 
        type: 'GET', 
        success: function(response) {
          var classroomNames = Object.keys(response); //array di nomi delle aule
  
          // choice box con nomi delle aule
          var classroomSelect = $('#classroomName');
          classroomNames.forEach(function(className) {
            classroomSelect.append('<option value="' + response[className] + '">' + className + '</option>');
          });
        },
        error: function() {
          console.log('Errore durante il recupero dei nomi delle aule');
        }
      });
    }
  
    function populateGroupNames() {
      $.ajax({
        url: '/rest/group/all', 
        type: 'GET',
        success: function(response) {
          var groupNames = Object.keys(response); // array nomi dei gruppi
  
          var groupSelect = $('#groupName');
          groupNames.forEach(function(groupName) {
            groupSelect.append('<option value="' + response[groupName] + '">' + groupName + '</option>');
          });
        },
        error: function() {
          console.log('Errore durante il recupero dei nomi dei gruppi');
        }
      });
    }
  
    //choicebox dei nomi delle aule e dei gruppi
    populateClassroomNames();
    populateGroupNames();
  });
  
  // 
  $('#addClassroomToGroupForm').submit(function(event) {
    event.preventDefault();
    
    var classroomId = $('#classroomId').val();
    var groupId = $('#groupId').val();
    
    var formData = {
      classroom_id: classroomId,
      group_id: groupId
    };
    
    $.ajax({
      url: '/rest/classroom/{classroomId}/group/{groupId}', 
      type: 'POST', 
      contentType: 'application/json',
      data: JSON.stringify(formData),
      success: function(response) {
        console.log('Aula aggiunta al gruppo con successo');
      },
      error: function() {
        console.log('Errore durante l\'aggiunta dell\'aula al gruppo');
      }
    });

    // Righe dinamiche per la tabella (WIP)
    $.ajax({
      url: '/rest/classroom/all', 
      type: 'GET',
      success: function(response) {
        //rimozione righe statiche
        $('#table-body').empty();
  
        
        response.forEach(function(data) {
         
          var row = '<tr>' +
            '<td><input class="form-check-input m-0 align-middle" type="checkbox" aria-label="Select invoice"></td>' +
            '<td><span class="text-muted">' + data.id + '</span></td>' +
            '<td><a href="invoice.html" class="text-reset" tabindex="-1">' + data.nome + '</a></td>' +
            '<td>' + data.idPosizione + '</td>' +
            '<td>' + data.capacita + '</td>' +
            '<td>' + data.email + '</td>' +
            '<td><span class="badge bg-success me-1"></span>' + data.attrezzature + '</td>' +
            '<td>' + data.caviEthernet + '</td>' +
            '<td class="text-end"></td>' +
            '</tr>';
  
          
          $('#table-body').append(row);
        });
      },
      error: function() {
        console.log('Errore durante il recupero dei dati dal database');
      }
    });
  });

