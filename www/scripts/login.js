let whoami = null;

$(function() {

    $(document).ajaxStart(function() {
        // Show image container

        $(".loader").show();

    });
    $(document).ajaxComplete(function() {
        // Hide image container
        setTimeout(function() {
            $(".loader").hide();
        }, 500);
    });

    /**
     * Validateur de formulaire pour la connexion
     */
    let loginValidator = $('.formLogin').bootstrapValidator({
        fields: {
            pseudo: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: errorMessages.emptyField
                    }
                }
            }
        }
    });

    /**
     * Trigger le bouton login lorsqu'on appuie sur enter
     */
    $(document).on('keypress', function(e) {
        if (e.which == 13) {
            $('#bt_login').click();
        }
    });

    /**
     * Vérifier si un prof est connecté en tant qu'étuidant
     */
    if ($.cookie("teacherToken") !== undefined) {
        teacherIsConnectedAsStudent = true;
        $('#bt_logout_student').text("Revenir");
    }


    /**
     * Appel AJAX pour vérifier si l'utilisateur est connecté
     */
    $.post("/mobilite", {
            action: "isconnected"

        },
        function(resp) {
            if (resp == "ko") {
                loggedOut();
            } else {
                if (resp.role === "student") {
                    loggedIn_student(resp);
                } else {
                    loggedIn_teacher(resp);
                }
            }
        }
    );

    /**
     * Trigger le reset des messages d'erreurs lorsqu'on appuie sur le bouton de login
     */
    $('#bt_login').click(function() {
        reset_errors("formLogin");
    });

    /**
     * Trigger le reset des messages d'erreurs lorsqu'on appuie sur le bouton de login
     */
    $('#bt_signUp').click(function() {
        reset_errors("formSignUp");
    });



    /**
     * Affichage de la page d'inscription
     */
    $('#click_here').on("click", function(event) {
        event.preventDefault(); // empêche de suivre l'hyperlien
        signUpValidator = initSignUpValidator();
        show_signUp_page();
    });

    /**
     * Gestion du bouton de login
     */
    $('#bt_login').on("click", function() {
        let length = $('.formLogin').bootstrapValidator('validate').has('.has-error').length; // = 0 si le formulaire est valide

        if (loginValidator != null && length === 0) {
            $.post("/mobilite", {
                    action: "login",
                    data: JSON.stringify(formToJSON("formLogin"))
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
                error_msg_generator("error_" + (errorData.target || "global") + "_L", errorData.message);
            });
        }
    });



});


/**
 * Fonction qui transforme un formulaire en un JSON
 * @param {*} formClass 
 */
function formToJSON(formClass) {
    var data = {};
    $('.' + formClass).find("input, select").each(function() {
        if ($(this).is('select')) {
            let value = $(this).find('option:selected').text();
            data[$(this).attr("name")] = value;
            return;
        }
        if ($(this).is(':radio')) {
            if ($(this).is(':checked')) {
                data[$(this).attr("name")] = $(this).val();
            }
            return;
        }
        if ($(this).val() === "") {
            return;
        }

        data[$(this).attr("name")] = $(this).val();
    });
    return data;
}

/**
 * Fonction qui s'exécute lorsqu'un étudiant est connecté / se connecte / s'inscrit
 * @param {*} user 
 */
function loggedIn_student(user) {
    whoami = user;
    $('#welcome_student').text(`Bienvenue ${user.pseudo} [Etudiant] !`);
    $('#pg_home_student').show();
    $('#pg_login').hide();
    $('#pg_signUp').hide();
    reset_errors("formLogin");

    ajaxCallsStudentConnection();
}

/**
 * Fonction qui s'exécute lorsqu'un professeur est connecté / se connecte / s'inscrit
 * @param {*} user 
 */
function loggedIn_teacher(user) {
    whoami = user;
    $('#welcome_teacher').text(`Bienvenue ${user.pseudo} [Professeur] !`);
    $('#pg_home_teacher').show();
    $('#pg_login').hide();
    $('#pg_signUp').hide();
    reset_errors("formLogin");

    ajaxCallsStudentConnection();
    ajaxCallsTeacherConnection();
}

/**
 * Fonction qui s'execute lorsqu'un utilisateurs est déconnecté / se déconnecte
 */
function loggedOut() {
    whoami = null;
    reset_errors("formLogin");
    show_login_page();
}

/**
 * Fonction qui met tous les messages d'erreur a zéro 
 * @param {*} formClass 
 */
function reset_errors(formClass) {
    $('.' + formClass).find("p").each(function() {
        $(this).empty();
        $(this).siblings().removeClass("border-danger");
    });
}

/**
 * Fonction qui affiche une erreur à l'écran
 * @param {*} p_id 
 */
function error_msg_generator(p_id, message) {
    const p = $('#' + p_id);
    p.text(message);
    p.siblings().addClass("border-danger");
}

/**
 * Fonction qui cache plusieurs éléments à la fois
 */
function multi_hide() {
    for (let index = 0; index < arguments.length; index++) {
        $(arguments[index]).hide();
    }
}

/**
 * Fonction qui montre plusieurs éléments à la fois
 */
function multi_show() {
    for (let index = 0; index < arguments.length; index++) {
        $(arguments[index]).show();
    }
}

/**
 * Fonction qui affiche la page de login
 */
function show_login_page() {
    $('#pg_login').show();
    multi_hide("#pg_home_student", "#pg_signUp", "#pg_home_teacher", "#pg_home_student");
}

/**
 * Fonction qui affiche la page d'inscription
 */
function show_signUp_page() {
    $('#pg_signUp').show();
    multi_hide("#pg_home_student", "#pg_login", "#pg_home_teacher", "#pg_home_student");
}