$(document).ready(function() {
    $('.dropdown-menu').submit(function(event) {
      event.preventDefault();
      
      // valori campi del form
      var email = $('#emailDropdown').val();
      var password = $('#passwordDropdown').val();
     
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
          $('#loginButton').hide();
          var authToken = response; // Salva il token dalla risposta

          document.cookie = "token=" + response;
          sessionStorage.setItem('authToken', response);
          console.log('Autenticazione riuscita');
        },
        error: function() {
          alert('Errore durante l\'autenticazione. Riprova.');
        } 
      });
    });
  });
  