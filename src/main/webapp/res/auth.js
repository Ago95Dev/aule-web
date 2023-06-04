$(document).ready(function() {
    $('.dropdown-menu').submit(function(event) {
      event.preventDefault();
      
      // valori campi del form
      var email = $('#exampleDropdownFormEmail2').val();
      var password = $('#exampleDropdownFormPassword2').val();
     
      
      //l'oggetto dati da inviare al server
      var formData = {
        email: email,
        password: password
      };
      
      $.ajax({
        url: 'rest/auth/login',
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        data: formData,
        success: function(response) {
          $('#dropdownMenuButton1').hide();
          var authToken = response.token; // Salva il token dalla risposta
        // salvataggio del token in una variabile
        localStorage.setItem('authToken', authToken);
          console.log('Autenticazione riuscita');
        },
        error: function() {
          alert('Errore durante l\'autenticazione. Riprova.');
        } 
      });
    });
  });
  