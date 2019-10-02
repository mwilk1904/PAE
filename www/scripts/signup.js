  /**
   * Messages d'erreur affichés à l'écran avant tout envoi du formulaire
   */
  const errorMessages = {
      differentPasswords: "Mots de passe non indentiques.",
      emptyField: "Ce champ ne peut pas être vide.",
      tooShort: (min) => `Ce champ doit contenir au moins ${min} caractères.`,
      wrongEmailFormat: "Veuillez fournir une adresse e-mail valide.",
      wrongWebSiteFormat: "Veuillez fournir un nom de site valide."
  }

  let signUpValidator = null;

  $(function() {

      /**
       * Gestion du bouton retour du formulaire d'inscription
       */
      $('#bt_back_login').on('click', function() {
          $('.formSignUp').trigger('reset');
          $(".formSignUp").bootstrapValidator("destroy");
          show_login_page();
      });

      /**
       * Gestion du bouton d'inscription
       */
      $('#bt_signUp').on('click', function() {

          let length = $('.formSignUp').bootstrapValidator('validate').has('.has-error').length; // = 0 si le formulaire est valide

          if (signUpValidator != null && length === 0) {

              $.post("/mobilite", {
                      action: "signUp",
                      data: JSON.stringify(formToJSON("formSignUp"))
                  },
                  function(resp) {
                      if (resp.role === "student") {
                          loggedIn_student(resp);
                      } else {
                          loggedIn_teacher(resp);
                      }
                  }
              ).fail(function(jqXHR, textStatus, error) {
                  const errorData = jqXHR.responseJSON;
                  console.log(errorData.message);
                  error_msg_generator("error_" + (errorData.target || "global") + "_S", errorData.message);
              });
          }
      });



  });

  /**
   * Validateur de formulaire pour l'inscription
   */
  function initSignUpValidator() {
      return $('.formSignUp').bootstrapValidator({
          fields: {
              pseudo: {
                  validators: {
                      notEmpty: {
                          message: errorMessages.emptyField
                      },
                      stringLength: {
                          min: 2,
                          message: errorMessages.tooShort(2)
                      }
                  }
              },
              lastName: {
                  validators: {
                      notEmpty: {
                          message: errorMessages.emptyField
                      },
                      stringLength: {
                          min: 2,
                          message: errorMessages.tooShort(2)
                      }
                  }
              },
              firstName: {
                  validators: {
                      notEmpty: {
                          message: errorMessages.emptyField
                      },
                      stringLength: {
                          min: 2,
                          message: errorMessages.tooShort(2)
                      }
                  }
              },
              email: {
                  validators: {
                      notEmpty: {
                          message: errorMessages.emptyField
                      },
                      emailAddress: {
                          message: errorMessages.wrongEmailFormat
                      }
                  }
              },
              password: {
                  validators: {
                      notEmpty: {
                          message: errorMessages.emptyField
                      },
                      stringLength: {
                          min: 5,
                          message: errorMessages.tooShort(5)
                      }
                  }
              },
              passwordConfirm: {
                  validators: {
                      notEmpty: {
                          message: errorMessages.emptyField
                      },
                      identical: {
                          field: "password",
                          message: errorMessages.differentPasswords
                      }
                  }
              }
          }
      });
  }