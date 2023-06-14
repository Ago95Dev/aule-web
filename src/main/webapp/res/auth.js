$(document).ready(function() {
    
    const logout_send = $('#logoutButton');

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
          $('#logoutButton').show();
          var authToken = response; // Salva il token dalla risposta

          document.cookie = "token=" + response;
          sessionStorage.setItem('authToken', response);
          location.reload();

        },
        error: function() {
          alert('Errore durante l\'autenticazione. Riprova.');
        } 
      });
    });
    
    // Send logout request
    logout_send.click(function () {
        $.ajax({
            url: 'rest/auth/logout',
            type: 'DELETE',
            success: function () {
                // noinspection JSUnresolvedFunction
                sessionStorage.removeItem("authToken");
                document.cookie = "token=";
                alert('Logout effettuato con successo.');
                location.reload();
            },
            error: function (request, status, error) {
                handleError(request, status, error, "", "Errore in fase di logout.");
            },
            cache: false,
        });
    });

    
  });
